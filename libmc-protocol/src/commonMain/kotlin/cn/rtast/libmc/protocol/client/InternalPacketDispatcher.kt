/*
 * Copyright © 2026 RTAkland
 * Author: RTAkland
 * Date: 2026/9/5
 */


package cn.rtast.libmc.protocol.client

import cn.rtast.libmc.common.packet.MinecraftPacket
import cn.rtast.libmc.protocol.packet.configuration.clientbound.ClientboundConfigurationPacket
import cn.rtast.libmc.protocol.packet.configuration.clientbound.ClientboundCookieRequestPacket
import cn.rtast.libmc.protocol.packet.configuration.clientbound.ClientboundCustomPayloadPacket
import cn.rtast.libmc.protocol.packet.configuration.clientbound.ClientboundDisconnectConfigurationPacket
import cn.rtast.libmc.protocol.packet.configuration.clientbound.ClientboundFinishConfigurationPacket
import cn.rtast.libmc.protocol.packet.configuration.clientbound.ClientboundKeepAliveConfigurationPacket
import cn.rtast.libmc.protocol.packet.configuration.clientbound.ClientboundPingConfigurationPacket
import cn.rtast.libmc.protocol.packet.configuration.clientbound.ClientboundSelectKnownPacksPacket
import cn.rtast.libmc.protocol.packet.configuration.serverbound.ServerboundAckFinishConfigurationPacket
import cn.rtast.libmc.protocol.packet.configuration.serverbound.ServerboundKeepAliveConfigurationPacket
import cn.rtast.libmc.protocol.packet.configuration.serverbound.ServerboundPongConfigurationPacket
import cn.rtast.libmc.protocol.packet.configuration.serverbound.ServerboundSelectKnownPacksPacket
import cn.rtast.libmc.protocol.packet.login.clientbound.ClientboundDisconnectLoginPacket
import cn.rtast.libmc.protocol.packet.login.clientbound.ClientboundLoginPacket
import cn.rtast.libmc.protocol.packet.login.clientbound.ClientboundLoginSuccessPacket
import cn.rtast.libmc.protocol.packet.login.clientbound.ClientboundSetCompressionPacket
import cn.rtast.libmc.protocol.packet.login.serverbound.ServerboundLoginAcknowledgedPacket
import cn.rtast.libmc.protocol.packet.play.clientbound.ClientboundAcknowledgeBlockChangePacket
import cn.rtast.libmc.protocol.packet.play.clientbound.ClientboundAwardStatisticsPacket
import cn.rtast.libmc.protocol.packet.play.clientbound.ClientboundBlockDestructionPacket
import cn.rtast.libmc.protocol.packet.play.clientbound.ClientboundBlockEntityDataPacket
import cn.rtast.libmc.protocol.packet.play.clientbound.ClientboundDelimiterPacket
import cn.rtast.libmc.protocol.packet.play.clientbound.ClientboundDisconnectPlayPacket
import cn.rtast.libmc.protocol.packet.play.clientbound.ClientboundEntityAnimationPacket
import cn.rtast.libmc.protocol.packet.play.clientbound.ClientboundKeepAlivePlayPacket
import cn.rtast.libmc.protocol.packet.play.clientbound.ClientboundLoginPlayPacket
import cn.rtast.libmc.protocol.packet.play.clientbound.ClientboundPingPlayPacket
import cn.rtast.libmc.protocol.packet.play.clientbound.ClientboundPlayPacket
import cn.rtast.libmc.protocol.packet.play.clientbound.ClientboundPlayerChatMessagePacket
import cn.rtast.libmc.protocol.packet.play.clientbound.ClientboundSpawnEntityPacket
import cn.rtast.libmc.protocol.packet.play.clientbound.ClientboundStartConfigurationPacket
import cn.rtast.libmc.protocol.packet.play.clientbound.ClientboundSystemChatMessagePacket
import cn.rtast.libmc.protocol.packet.play.serverbound.ServerboundConfigurationAcknowledgedPacket
import cn.rtast.libmc.protocol.packet.play.serverbound.ServerboundKeepAlivePlayPacket
import cn.rtast.libmc.protocol.packet.play.serverbound.ServerboundPongPlayPacket
import cn.rtast.libmc.protocol.protocol.state.ProtocolState

internal class InternalPacketDispatcher(private val client: MinecraftClient) {
    suspend fun dispatchEvent(packet: MinecraftPacket) = client.dispatch(packet)

    suspend fun handleIncomingPackets(packet: MinecraftPacket) {
        this.dispatchEvent(packet)
        when (packet) {
            is ClientboundLoginPacket -> this.handleLoginPackets(packet)
            is ClientboundConfigurationPacket -> this.handleConfigurationPackets(packet)
            is ClientboundPlayPacket -> this.handlePlayPackets(packet)
        }
    }

    private fun handleLoginPackets(packet: ClientboundLoginPacket) {
        when (packet) {
            is ClientboundDisconnectLoginPacket -> {
                println("Login denied: ${packet.reason}")
                client.close()
            }

            is ClientboundSetCompressionPacket -> client.networkChannel.setCompression(packet.threshold)
            is ClientboundLoginSuccessPacket -> {
                client.networkChannel.sendPacket(ServerboundLoginAcknowledgedPacket)
                client.stateMachine.transitionTo(ProtocolState.CONFIGURATION)
            }
        }
    }

    private fun handleConfigurationPackets(packet: ClientboundConfigurationPacket) {
        when (packet) {
            is ClientboundCookieRequestPacket -> {
                // TODO
            }

            is ClientboundCustomPayloadPacket -> {
                // TODO
            }

            is ClientboundDisconnectConfigurationPacket -> {
                println("Configuration disconnected: ${packet.reason}")
            }

            ClientboundFinishConfigurationPacket -> {
                client.networkChannel.sendPacket(ServerboundAckFinishConfigurationPacket)
                client.stateMachine.transitionTo(ProtocolState.PLAY)
            }

            is ClientboundKeepAliveConfigurationPacket -> client.networkChannel.sendPacket(
                ServerboundKeepAliveConfigurationPacket(packet.id)
            )

            is ClientboundPingConfigurationPacket -> client.networkChannel.sendPacket(
                ServerboundPongConfigurationPacket(packet.id)
            )

            is ClientboundSelectKnownPacksPacket -> {
                client.networkChannel.sendPacket(ServerboundSelectKnownPacksPacket(emptyList()))  // TODO empty resource packs list
            }
        }
    }

    private fun handlePlayPackets(packet: ClientboundPlayPacket) {
        when (packet) {
            is ClientboundDisconnectPlayPacket -> client.close()
            is ClientboundKeepAlivePlayPacket -> client.networkChannel.sendPacket(ServerboundKeepAlivePlayPacket(id = packet.id))
            is ClientboundLoginPlayPacket -> println("Successfully joined world Entity ID: ${packet.entityId}")
            is ClientboundPingPlayPacket -> client.networkChannel.sendPacket(ServerboundPongPlayPacket(packet.id))
            is ClientboundPlayerChatMessagePacket -> {
                println("Received player chat message $packet")
                // TODO
            }

            ClientboundStartConfigurationPacket -> {
                client.networkChannel.sendPacket(ServerboundConfigurationAcknowledgedPacket)
                client.stateMachine.transitionTo(ProtocolState.CONFIGURATION)
            }

            is ClientboundSystemChatMessagePacket -> {}
            is ClientboundAcknowledgeBlockChangePacket -> {}
            is ClientboundAwardStatisticsPacket -> {}
            is ClientboundBlockDestructionPacket -> {}
            is ClientboundBlockEntityDataPacket -> {}
            ClientboundDelimiterPacket -> {}
            is ClientboundEntityAnimationPacket -> {}
            is ClientboundSpawnEntityPacket -> {}
        }
    }
}