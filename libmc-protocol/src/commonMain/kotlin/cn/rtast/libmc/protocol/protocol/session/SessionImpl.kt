package cn.rtast.libmc.protocol.protocol.session

import cn.rtast.libmc.packet.MinecraftPacket
import cn.rtast.libmc.protocol.client.MinecraftClient
import cn.rtast.libmc.protocol.crypto.minecraftServerIdHash
import cn.rtast.libmc.protocol.crypto.rsaEncrypt
import cn.rtast.libmc.protocol.packet.configuration.clientbound.*
import cn.rtast.libmc.protocol.packet.configuration.serverbound.ServerboundAckFinishConfigurationPacket
import cn.rtast.libmc.protocol.packet.configuration.serverbound.ServerboundKeepAliveConfigurationPacket
import cn.rtast.libmc.protocol.packet.configuration.serverbound.ServerboundPongConfigurationPacket
import cn.rtast.libmc.protocol.packet.configuration.serverbound.ServerboundSelectKnownPacksPacket
import cn.rtast.libmc.protocol.packet.handshake.ServerboundHandshakePacket
import cn.rtast.libmc.protocol.packet.login.clientbound.ClientboundDisconnectLoginPacket
import cn.rtast.libmc.protocol.packet.login.clientbound.ClientboundHelloPacket
import cn.rtast.libmc.protocol.packet.login.clientbound.ClientboundLoginSuccessPacket
import cn.rtast.libmc.protocol.packet.login.clientbound.ClientboundSetCompressionPacket
import cn.rtast.libmc.protocol.packet.login.serverbound.ServerboundKeyPacket
import cn.rtast.libmc.protocol.packet.login.serverbound.ServerboundLoginAcknowledgedPacket
import cn.rtast.libmc.protocol.packet.login.serverbound.ServerboundLoginStartPacket
import cn.rtast.libmc.protocol.packet.play.clientbound.ClientboundDisconnectPlayPacket
import cn.rtast.libmc.protocol.packet.play.clientbound.ClientboundKeepAlivePlayPacket
import cn.rtast.libmc.protocol.packet.play.clientbound.ClientboundPingPacket
import cn.rtast.libmc.protocol.packet.play.serverbound.ServerboundKeepAlivePlayPacket
import cn.rtast.libmc.protocol.packet.play.serverbound.ServerboundPongPlayPacket
import cn.rtast.libmc.protocol.packet.status.clientbound.ClientboundStatusResponsePacket
import cn.rtast.libmc.protocol.packet.status.serverbound.ServerboundStatusRequestPacket
import cn.rtast.libmc.protocol.protocol.event.ListenerRegistration
import cn.rtast.libmc.protocol.protocol.game.chat.TextComponent
import cn.rtast.libmc.protocol.protocol.state.HandshakeIntent
import cn.rtast.libmc.protocol.protocol.state.ProtocolState
import cn.rtast.libmc.protocol.threads.coroutines.runSuspend
import cn.rtast.libmc.protocol.threads.coroutines.runAsCompletable
import cn.rtast.libmc.protocol.util.generateRandom16Bytes
import kotlin.reflect.KClass

private typealias EventHandler = Session.(SessionEvent) -> Unit

public class SessionImpl internal constructor() : Session {
    private lateinit var client: MinecraftClient
    internal fun attachClient(client: MinecraftClient) {
        this.client = client
    }

    private val stateMachine get() = client.stateMachine
    private val networkChannel get() = client.networkChannel

    @PublishedApi
    internal val eventListener: HashMap<KClass<out SessionEvent>, MutableList<EventHandler>> = hashMapOf()

    override fun init() {
        client.onPacket<ClientboundDisconnectLoginPacket> {
            emitEvent(SessionEvent.DisconnectedEvent(it.reason, stateMachine.currentState))
        }
        client.onPacket<ClientboundDisconnectPlayPacket> {
            emitEvent(SessionEvent.DisconnectedEvent(it.reason, stateMachine.currentState))
        }
        client.onPacket<ClientboundDisconnectConfigurationPacket> {
            emitEvent(SessionEvent.DisconnectedEvent(it.reason, stateMachine.currentState))
        }

        client.onPacket<ClientboundHelloPacket> { acceptEncryption(it) }
        client.onPacket<ClientboundSetCompressionPacket> { setCompression(it.threshold) }
        client.onPacket<ClientboundLoginSuccessPacket> { acknowledgeLogin() }
        client.onPacket<ClientboundFinishConfigurationPacket> { finishConfiguration() }
        client.onPacket<ClientboundPingPacket> { networkChannel.sendPacket(ServerboundPongPlayPacket(it.id)) }
        client.onPacket<ClientboundKeepAlivePlayPacket> { networkChannel.sendPacket(ServerboundKeepAlivePlayPacket(it.id)) }
        client.onPacket<ClientboundKeepAliveConfigurationPacket> {
            networkChannel.sendPacket(ServerboundKeepAliveConfigurationPacket(it.id))
        }
        client.onPacket<ClientboundPingConfigurationPacket> {
            networkChannel.sendPacket(ServerboundPongConfigurationPacket(it.id))
        }
        client.onPacket<ClientboundSelectKnownPacksPacket> {
            networkChannel.sendPacket(ServerboundSelectKnownPacksPacket(emptyList()))
        }
    }

    public override fun sendPacket(packet: MinecraftPacket): Unit = client.networkChannel.sendPacket(packet)

    override fun <T : SessionEvent> _onEvent(clazz: KClass<T>, block: Session.(T) -> Unit) {
        @Suppress("UNCHECKED_CAST")
        this.eventListener.getOrPut(clazz) { mutableListOf() }.add { block(it as T) }
    }

    override fun emitEvent(event: SessionEvent): Unit? =
        eventListener[event::class]?.forEach { handler -> handler(event) }

    override fun login(protocolVersion: Int) {
        handshake(protocolVersion, HandshakeIntent.LOGIN)
        loginStart()
    }

    override fun handshake(protocolVersion: Int, intent: HandshakeIntent) {
        ensureState(ProtocolState.HANDSHAKE)
        client.networkChannel.sendPacket(
            ServerboundHandshakePacket(protocolVersion, client.host, client.port.toUShort(), intent)
        )
        val targetState = if (intent == HandshakeIntent.LOGIN) ProtocolState.LOGIN else {
            if (intent == HandshakeIntent.TRANSFER) ProtocolState.HANDSHAKE else ProtocolState.STATUS
        }
        stateMachine.transitionTo(targetState)
    }

    override fun status(protocolVersion: Int): String =
        runAsCompletable { resumable ->
            client.executor.submit {
                try {
                    var reg: ListenerRegistration? = null
                    reg = client.onPacket<ClientboundStatusResponsePacket> { packet ->
                        reg?.unregister()
                        resumable.resume(packet.jsonResponse)
                    }
                    handshake(protocolVersion, HandshakeIntent.STATUS)
                    networkChannel.sendPacket(ServerboundStatusRequestPacket)
                } catch (e: Exception) {
                    resumable.resumeWithException(e)
                }
            }
        }

    internal fun loginStart() {
        ensureState(ProtocolState.LOGIN)
        networkChannel.sendPacket(ServerboundLoginStartPacket(client.username, client.uuid))
    }

    internal fun acceptEncryption(context: ClientboundHelloPacket) {
        ensureState(ProtocolState.LOGIN)
        val sharedSecret = generateRandom16Bytes()
        val serverHash = minecraftServerIdHash(context.serverId, sharedSecret, context.publicKey)
        val postPayload =
            """{"accessToken":"${client.accessToken}", 
                |"selectedProfile":"${client.uuid.toString().replace("-", "")}",
                |"serverId":"$serverHash"}""".trimMargin().trimIndent()
        runSuspend {
            client.protocolContext.httpClient!!.post(
                "https://sessionserver.mojang.com/session/minecraft/join",
                postPayload, emptyMap()
            )
        }
        val encryptedSecret = rsaEncrypt(context.publicKey, sharedSecret)
        val encryptedVerifyToken = rsaEncrypt(context.publicKey, context.verifyToken)
        networkChannel.sendPacket(ServerboundKeyPacket(encryptedSecret, encryptedVerifyToken))
        networkChannel.networkSession.enableEncryption(sharedSecret)
    }

    internal fun acknowledgeLogin() {
        ensureState(ProtocolState.LOGIN)
        networkChannel.sendPacket(ServerboundLoginAcknowledgedPacket)
        stateMachine.transitionTo(ProtocolState.CONFIGURATION)
    }

    internal fun finishConfiguration() {
        ensureState(ProtocolState.CONFIGURATION)
        networkChannel.sendPacket(ServerboundAckFinishConfigurationPacket)
        stateMachine.transitionTo(ProtocolState.PLAY)
    }

    internal fun setCompression(threshold: Int) {
        ensureState(ProtocolState.LOGIN, ProtocolState.CONFIGURATION)
        networkChannel.setCompression(threshold)
    }

    override fun disconnect(): Unit = client.close().apply {
        emitEvent(
            SessionEvent.DisconnectedEvent(
                TextComponent(TextComponent.Content.PlainText("LibMC-Disconnected by calling disconnect()")),
                stateMachine.currentState
            )
        )
    }

    private fun ensureState(vararg allowedStates: ProtocolState) {
        if (stateMachine.currentState !in allowedStates) throw IllegalStateException(
            "Cannot perform this action in state ${stateMachine.currentState}. Expected state: ${
                allowedStates.joinToString(",").removeSuffix(",")
            }."
        )
    }
}