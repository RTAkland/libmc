/*
 * Copyright © 2026 RTAkland
 * Author: RTAkland
 * Date: 2026/9/5
 */


package cn.rtast.libmc.protocol.event

import cn.rtast.libmc.packet.MinecraftPacket
import cn.rtast.libmc.protocol.client.MinecraftClient
import cn.rtast.libmc.protocol.packet.configuration.clientbound.*
import cn.rtast.libmc.protocol.packet.configuration.serverbound.*
import cn.rtast.libmc.protocol.packet.login.clientbound.ClientboundDisconnectLoginPacket
import cn.rtast.libmc.protocol.packet.login.clientbound.ClientboundHelloPacket
import cn.rtast.libmc.protocol.packet.login.clientbound.ClientboundLoginSuccessPacket
import cn.rtast.libmc.protocol.packet.login.clientbound.ClientboundSetCompressionPacket
import cn.rtast.libmc.protocol.packet.login.serverbound.ServerboundKeyPacket
import cn.rtast.libmc.protocol.packet.login.serverbound.ServerboundLoginAcknowledgedPacket
import cn.rtast.libmc.protocol.packet.play.clientbound.ClientboundDisconnectPlayPacket
import cn.rtast.libmc.protocol.packet.play.clientbound.ClientboundKeepAlivePlayPacket
import cn.rtast.libmc.protocol.packet.play.clientbound.ClientboundPingPacket
import cn.rtast.libmc.protocol.packet.play.clientbound.ClientboundStartConfigurationPacket
import cn.rtast.libmc.protocol.packet.play.serverbound.ServerboundConfigurationAcknowledgedPacket
import cn.rtast.libmc.protocol.packet.play.serverbound.ServerboundKeepAlivePlayPacket
import cn.rtast.libmc.protocol.packet.play.serverbound.ServerboundPongPlayPacket
import cn.rtast.libmc.protocol.protocol.state.ProtocolState
import cn.rtast.libmc.protocol.util.generateRandom16Bytes

/**
 * Internal simple state machine trigger,
 * Auto respond packets the server needed.
 * Only including `Handshake`, `Login` and `Configuration` State
 */
public class InternalPacketDispatcher(private val client: MinecraftClient) {
    public suspend fun handleIncomingPackets(packet: MinecraftPacket) {
        when (packet) {
            // login
            is ClientboundDisconnectLoginPacket -> client.close()
            is ClientboundSetCompressionPacket -> client.networkChannel.setCompression(packet.threshold)
            is ClientboundLoginSuccessPacket -> {
                client.networkChannel.sendPacket(ServerboundLoginAcknowledgedPacket)
                client.stateMachine.transitionTo(ProtocolState.CONFIGURATION)
            }

            is ClientboundHelloPacket -> {
                val sharedSecret = generateRandom16Bytes()
                if (client.isOnlineMode) {
                    val serverHash = client.protocolContext.sha1Hasher!!
                        .hash(packet.serverId, sharedSecret, packet.publicKey)
                    client.protocolContext.authProvider!!.joinServer(
                        "https://sessionserver.mojang.com/session/minecraft/join",
                        client.accessToken!!,
                        client.uuid.toString().replace("-", ""),
                        serverHash
                    )
                }
                val encryptedSecret = client.protocolContext.rsaEncryptor!!.encrypt(packet.publicKey, sharedSecret)
                val encryptedVerifyToken =
                    client.protocolContext.rsaEncryptor!!.encrypt(packet.publicKey, packet.verifyToken)
                client.networkChannel.sendPacket(ServerboundKeyPacket(encryptedSecret, encryptedVerifyToken))
                client.networkChannel.session.enableEncryption(sharedSecret)
            }

            // configuration
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

            is ClientboundSelectKnownPacksPacket -> client.networkChannel.sendPacket(
                ServerboundSelectKnownPacksPacket(emptyList())
            )  // TODO empty resource packs list
            is ClientboundCodeOfConductPacket -> client.networkChannel.sendPacket(ServerboundAcceptCodeOfConductPacket)

            // play
            is ClientboundDisconnectPlayPacket -> client.close()
            is ClientboundKeepAlivePlayPacket -> client.networkChannel.sendPacket(ServerboundKeepAlivePlayPacket(id = packet.id))
            is ClientboundPingPacket -> client.networkChannel.sendPacket(ServerboundPongPlayPacket(packet.id))
            ClientboundStartConfigurationPacket -> {
                client.networkChannel.sendPacket(ServerboundConfigurationAcknowledgedPacket)
                client.stateMachine.transitionTo(ProtocolState.CONFIGURATION)
            }

            else -> {}
        }
    }
}