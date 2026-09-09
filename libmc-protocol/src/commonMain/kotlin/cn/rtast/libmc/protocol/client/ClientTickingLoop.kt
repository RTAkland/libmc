/*
 * Copyright © 2026 RTAkland
 * Author: RTAkland
 * Date: 2026/9/9
 */

package cn.rtast.libmc.protocol.client

import cn.rtast.libmc.protocol.crypto.minecraftServerIdHash
import cn.rtast.libmc.protocol.crypto.rsaEncrypt
import cn.rtast.libmc.protocol.packet.configuration.clientbound.*
import cn.rtast.libmc.protocol.packet.configuration.serverbound.*
import cn.rtast.libmc.protocol.packet.login.clientbound.ClientboundDisconnectLoginPacket
import cn.rtast.libmc.protocol.packet.login.clientbound.ClientboundHelloPacket
import cn.rtast.libmc.protocol.packet.login.clientbound.ClientboundLoginSuccessPacket
import cn.rtast.libmc.protocol.packet.login.clientbound.ClientboundSetCompressionPacket
import cn.rtast.libmc.protocol.packet.login.serverbound.ServerboundKeyPacket
import cn.rtast.libmc.protocol.packet.login.serverbound.ServerboundLoginAcknowledgedPacket
import cn.rtast.libmc.protocol.packet.play.clientbound.*
import cn.rtast.libmc.protocol.packet.play.serverbound.*
import cn.rtast.libmc.protocol.protocol.state.ProtocolState
import cn.rtast.libmc.protocol.util.generateRandom16Bytes
import kotlinx.coroutines.*
import kotlin.concurrent.Volatile
import kotlin.math.abs
import kotlin.time.Clock
import kotlin.time.Duration.Companion.milliseconds

public class ClientTickingLoop internal constructor(private val client: MinecraftClient) {
    private var tickJob: Job? = null
    private val tickIntervalMs = 50L
    public var currentTick: Long = 0L
        private set

    @Volatile
    public var isRunning: Boolean = false
        private set

    private val listeners = mutableListOf<suspend (Long) -> Unit>()
    internal fun registerListener(action: suspend (Long) -> Unit) = listeners.add(action)

    init {
        client.onPacket<ClientboundLoginSuccessPacket> { handleLoginSuccess() }
        client.onPacket<ClientboundSetCompressionPacket> { client.networkChannel.setCompression(it.threshold) }
        client.onPacket<ClientboundHelloPacket> { handleEncryptRequest(it) }
        client.onPacket<ClientboundDisconnectLoginPacket> { client.close() }
        client.onPacket<ClientboundDisconnectPlayPacket> { client.close() }
        client.onPacket<ClientboundDisconnectConfigurationPacket> { client.close() }
        client.onPacket<ClientboundSetTimePacket> { syncServerTick(it.worldAge) }
        client.onPacket<ClientboundPingPacket> { client.networkChannel.sendPacket(ServerboundPongPlayPacket(it.id)) }
        client.onPacket<ClientboundSelectKnownPacksPacket> {
            client.networkChannel.sendPacket(ServerboundSelectKnownPacksPacket(emptyList()))
        }
        client.onPacket<ClientboundCodeOfConductPacket> {
            client.networkChannel.sendPacket(ServerboundAcceptCodeOfConductPacket)
        }
        client.onPacket<ClientboundKeepAlivePlayPacket> {
            client.networkChannel.sendPacket(ServerboundKeepAlivePlayPacket(it.id))
        }
        client.onPacket<ClientboundPingConfigurationPacket> {
            client.networkChannel.sendPacket(ServerboundPongConfigurationPacket(it.id))
        }
        client.onPacket<ClientboundStartConfigurationPacket> {
            client.networkChannel.sendPacket(ServerboundConfigurationAcknowledgedPacket)
            client.stateMachine.transitionTo(ProtocolState.CONFIGURATION)
        }
        client.onPacket<ClientboundKeepAliveConfigurationPacket> {
            client.networkChannel.sendPacket(ServerboundKeepAliveConfigurationPacket(it.id))
        }
        client.onPacket<ClientboundFinishConfigurationPacket> {
            client.networkChannel.sendPacket(ServerboundAckFinishConfigurationPacket)
            client.stateMachine.transitionTo(ProtocolState.PLAY)
        }
    }

    internal suspend fun handleLoginSuccess() {
        client.networkChannel.sendPacket(ServerboundLoginAcknowledgedPacket)
        client.stateMachine.transitionTo(ProtocolState.CONFIGURATION)
    }

    internal suspend fun handleEncryptRequest(packet: ClientboundHelloPacket) {
        val sharedSecret = generateRandom16Bytes()
        if (client.isOnlineMode) {
            val serverHash = minecraftServerIdHash(packet.serverId, sharedSecret, packet.publicKey)
            client.protocolContext.authProvider!!.joinServer(
                "https://sessionserver.mojang.com/session/minecraft/join",
                client.accessToken!!, client.uuid.toString().replace("-", ""), serverHash
            )
        }
        val encryptedSecret = rsaEncrypt(packet.publicKey, sharedSecret)
        val encryptedVerifyToken = rsaEncrypt(packet.publicKey, packet.verifyToken)
        client.networkChannel.sendPacket(ServerboundKeyPacket(encryptedSecret, encryptedVerifyToken))
        client.networkChannel.session.enableEncryption(sharedSecret)
    }

    internal fun syncServerTick(serverWorldAge: Long) {
        if (abs(this.currentTick - serverWorldAge) > 2) this.currentTick = serverWorldAge
    }

    internal fun start() {
        if (isRunning) return
        isRunning = true
        tickJob = client.launch(CoroutineName("LibMC-ClientTickingLoop")) {
            var nextTickTime = Clock.System.now().toEpochMilliseconds()
            while (isActive && isRunning) {
                val now = Clock.System.now().toEpochMilliseconds()
                if (now >= nextTickTime) {
                    try {
                        listeners.forEach { it.invoke(currentTick) }
                    } catch (e: Exception) {
                        if (e is CancellationException) throw e
                        e.printStackTrace()
                    }
                    currentTick++
                    nextTickTime += tickIntervalMs
                    if (now - nextTickTime > tickIntervalMs * 5) nextTickTime = now + tickIntervalMs
                } else delay((nextTickTime - now).milliseconds)
            }
        }
    }

    internal fun stop() {
        isRunning = false
        tickJob?.cancel()
        tickJob = null
    }
}