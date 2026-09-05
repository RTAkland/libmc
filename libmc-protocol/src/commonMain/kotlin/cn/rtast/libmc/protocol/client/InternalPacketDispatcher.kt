/*
 * Copyright © 2026 RTAkland
 * Author: RTAkland
 * Date: 2026/9/5
 */


package cn.rtast.libmc.protocol.client

import cn.rtast.libmc.common.packet.MinecraftPacket
import cn.rtast.libmc.protocol.packet.configuration.*
import cn.rtast.libmc.protocol.packet.login.ClientboundDisconnectLoginPacket
import cn.rtast.libmc.protocol.packet.login.ClientboundLoginSuccessPacket
import cn.rtast.libmc.protocol.packet.login.ServerboundLoginAcknowledgedPacket
import cn.rtast.libmc.protocol.packet.play.*
import cn.rtast.libmc.protocol.protocol.state.ProtocolState

internal class InternalPacketDispatcher(private val client: MinecraftClient) {
    suspend fun dispatchEvent(packet: MinecraftPacket) = client.dispatch(packet)

    suspend fun handleIncomingPackets(packet: MinecraftPacket) {
        this.dispatchEvent(packet)
        when (packet) {
            is ClientboundLoginSuccessPacket -> {
                client.networkChannel.sendPacket(ServerboundLoginAcknowledgedPacket)
                client.stateMachine.transitionTo(ProtocolState.CONFIGURATION)
            }

            is ClientboundDisconnectLoginPacket -> {
                println("Login denied: ${packet.reason}")
//                close()
            }

            is ClientboundSelectKnownPacksPacket -> {
                client.networkChannel.sendPacket(ServerboundSelectKnownPacksPacket(emptyList()))  // TODO empty resource packs list
            }

            is ClientboundPingPacket -> client.networkChannel.sendPacket(ServerboundPongPacket(packet.id))

            is ClientboundKeepAliveConfigurationPacket -> {
                client.networkChannel.sendPacket(ServerboundKeepAliveConfigurationPacket(packet.id))
            }

            is ClientboundFinishConfigurationPacket -> {
                client.networkChannel.sendPacket(ServerboundAckFinishConfigurationPacket)
                client.stateMachine.transitionTo(ProtocolState.PLAY)
            }

            is ClientboundDisconnectConfigurationPacket -> {
                println("Configuration disconnected: ${packet.reason}")
//                close()
            }

            is ClientboundLoginPlayPacket -> {
                println("Successfully joined world! Entity ID: ${packet.entityId}")
            }

            is ClientboundKeepAlivePlayPacket -> {
                client.networkChannel.sendPacket(ServerboundKeepAlivePlayPacket(id = packet.id))
            }

            is ClientboundStartConfigurationPacket -> {
                client.networkChannel.sendPacket(ServerboundConfigurationAcknowledgedPacket)
                client.stateMachine.transitionTo(ProtocolState.CONFIGURATION)
            }

            is ClientboundDisconnectPlayPacket -> {
                println("Disconnected from play session: ${packet.reason}")
//                close()
            }
        }
    }
}