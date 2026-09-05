/*
 * Copyright © 2026 RTAkland
 * Author: RTAkland
 * Date: 2026/9/5
 */

package cn.rtast.libmc.protocol.client

import cn.rtast.libmc.common.LibMCContext
import cn.rtast.libmc.common.packet.MinecraftPacket
import cn.rtast.libmc.common.packet.UnknownPacket
import cn.rtast.libmc.protocol.network.NetworkChannel
import cn.rtast.libmc.protocol.packet.configuration.*
import cn.rtast.libmc.protocol.packet.handshake.ServerboundHandshakePacket
import cn.rtast.libmc.protocol.packet.login.ClientboundDisconnectLoginPacket
import cn.rtast.libmc.protocol.packet.login.ClientboundLoginSuccessPacket
import cn.rtast.libmc.protocol.packet.login.ServerboundLoginAcknowledgedPacket
import cn.rtast.libmc.protocol.packet.login.ServerboundLoginStartPacket
import cn.rtast.libmc.protocol.packet.play.*
import cn.rtast.libmc.protocol.protocol.GameProtocols
import cn.rtast.libmc.protocol.protocol.state.HandshakeIntent
import cn.rtast.libmc.protocol.protocol.state.ProtocolState
import cn.rtast.libmc.protocol.util.generateOfflineUuid
import kotlinx.coroutines.*
import kotlin.uuid.Uuid

public class MinecraftClient(
    private val host: String,
    private val port: Int = 25565,
    private val username: String,
    private val uuid: Uuid = generateOfflineUuid(username),
    private val context: LibMCContext = LibMCContext(),
) {
    private val stateMachine = ClientStateMachine()
    private val networkChannel = NetworkChannel(host, port, context, stateMachine)
    private val listeners = mutableListOf<(MinecraftPacket) -> Unit>()
    private var listenJob: Job? = null

    public suspend fun connect(protocolVersion: Int = 776) {
        networkChannel.connect()
        startListening()
        networkChannel.sendPacket(
            ServerboundHandshakePacket(
                protocolVersion, host,
                port.toUShort(),
                HandshakeIntent.LOGIN
            )
        )
        stateMachine.transitionTo(ProtocolState.LOGIN)
        networkChannel.sendPacket(ServerboundLoginStartPacket(username, uuid))
        listenJob?.join()
    }

    private fun startListening() {
        listenJob = CoroutineScope(Dispatchers.IO).launch {
            try {
                while (isActive) {
                    val packet = networkChannel.readNextPacket()
                    handleIncomingPackets(packet)
                    listeners.forEach { it.invoke(packet) }
                }
            } catch (e: Exception) {
                e.printStackTrace()
                if (isActive) {
                    println("Network read loop exception: ${e.message}")
                    close()
                }
            }
        }
    }

    private fun handleIncomingPackets(packet: MinecraftPacket) {
        when (packet) {
            is ClientboundLoginSuccessPacket -> {
                networkChannel.sendPacket(ServerboundLoginAcknowledgedPacket)
                stateMachine.transitionTo(ProtocolState.CONFIGURATION)
            }

            is ClientboundDisconnectLoginPacket -> {
                println("Login denied: ${packet.reason}")
                close()
            }

            is ClientboundSelectKnownPacksPacket -> {
                networkChannel.sendPacket(ServerboundSelectKnownPacksPacket(emptyList()))  // TODO empty resource packs list
            }

            is ClientboundPingPacket -> networkChannel.sendPacket(ServerboundPongPacket(packet.id))

            is ClientboundKeepAliveConfigurationPacket -> {
                networkChannel.sendPacket(ServerboundKeepAliveConfigurationPacket(packet.id))
            }

            is ClientboundFinishConfigurationPacket -> {
                networkChannel.sendPacket(ServerboundAckFinishConfigurationPacket)
                stateMachine.transitionTo(ProtocolState.PLAY)
            }

            is ClientboundDisconnectConfigurationPacket -> {
                println("Configuration disconnected: ${packet.reason}")
                close()
            }

            is ClientboundLoginPlayPacket -> {
                println("Successfully joined world! Entity ID: ${packet.entityId}")
            }

            is ClientboundKeepAlivePlayPacket -> {
                networkChannel.sendPacket(ServerboundKeepAlivePlayPacket(id = packet.id))
            }

            is ClientboundStartConfigurationPacket -> {
                networkChannel.sendPacket(ServerboundConfigurationAcknowledgedPacket)
                stateMachine.transitionTo(ProtocolState.CONFIGURATION)
            }

            is ClientboundDisconnectPlayPacket -> {
                println("Disconnected from play session: ${packet.reason}")
                close()
            }
//            else -> println((packet as? UnknownPacket)?.data?.contentToString() ?: packet)
        }
    }

    public fun onPacket(listener: (MinecraftPacket) -> Unit) {
        listeners.add(listener)
    }

    public fun close() {
        listenJob?.cancel()
        networkChannel.close()
    }
}