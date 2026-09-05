/*
 * Copyright © 2026 RTAkland
 * Author: RTAkland
 * Date: 2026/9/5
 */

package cn.rtast.libmc.protocol.protocol

import cn.rtast.libmc.protocol.packet.configuration.*
import cn.rtast.libmc.protocol.packet.handshake.ServerboundHandshakePacket
import cn.rtast.libmc.protocol.packet.login.ClientboundDisconnectLoginPacket
import cn.rtast.libmc.protocol.packet.login.ClientboundLoginSuccessPacket
import cn.rtast.libmc.protocol.packet.login.ClientboundSetCompressionPacket
import cn.rtast.libmc.protocol.packet.login.ServerboundLoginAcknowledgedPacket
import cn.rtast.libmc.protocol.packet.login.ServerboundLoginStartPacket
import cn.rtast.libmc.protocol.packet.play.*
import cn.rtast.libmc.protocol.protocol.state.ProtocolState
import cn.rtast.libmc.protocol.protocol.state.ProtocolStateRegistry

internal object GameProtocols {
    val clientboundGameProtocols = ProtocolStateRegistry().apply {
        register(ProtocolState.CONFIGURATION) {
            register(0x00, ClientboundCookieRequestPacket)
            register(0x01, ClientboundCustomPayloadPacket)
            register(0x02, ClientboundDisconnectConfigurationPacket)
            register(0x03, ClientboundFinishConfigurationPacket)
            register(0x04, ClientboundKeepAliveConfigurationPacket)
            register(0x05, ClientboundPingPacket)
            register(0x0E, ClientboundSelectKnownPacksPacket)
        }
        register(ProtocolState.LOGIN) {
            register(0x00, ClientboundDisconnectLoginPacket)
            register(0x02, ClientboundLoginSuccessPacket)
            register(0x03, ClientboundSetCompressionPacket)
        }
        register(ProtocolState.PLAY) {
            register(0x2C, ClientboundKeepAlivePlayPacket)
            register(0x2E, ClientboundLoginPlayPacket)
            register(0x3A, ClientboundPingPlayPacket)
            register(0x3D, ClientboundPlayerChatMessagePacket)
            register(0x76, ClientboundStartConfigurationPacket)
        }
    }

    val serverboundGameProtocols = ProtocolStateRegistry().apply {
        register(ProtocolState.HANDSHAKE) {
            register(0x00, ServerboundHandshakePacket)
        }
        register(ProtocolState.CONFIGURATION) {
            register(0x03, ServerboundAckFinishConfigurationPacket)
            register(0x04, ServerboundKeepAliveConfigurationPacket)
            register(0x05, ServerboundPongPacket)
            register(0x07, ServerboundSelectKnownPacksPacket)
        }
        register(ProtocolState.LOGIN) {
            register(0x00, ServerboundLoginStartPacket)
            register(0x03, ServerboundLoginAcknowledgedPacket)
        }
        register(ProtocolState.PLAY) {
            register(0x09, ServerboundChatMessagePacket)
            register(0x0B, ServerboundPongPlayPacket)
            register(0x0D, ServerboundConfigurationAcknowledgedPacket)
            register(0x1C, ServerboundKeepAlivePlayPacket)
        }
    }
}