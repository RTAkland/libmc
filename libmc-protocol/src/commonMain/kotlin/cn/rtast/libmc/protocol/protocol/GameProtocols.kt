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
            register(0x05, ClientboundPingConfigurationPacket)
            register(0x0e, ClientboundSelectKnownPacksPacket)
        }
        register(ProtocolState.LOGIN) {
            register(0x00, ClientboundDisconnectLoginPacket)
            register(0x02, ClientboundLoginSuccessPacket)
            register(0x03, ClientboundSetCompressionPacket)
        }
        register(ProtocolState.PLAY) {
            register(0x20, ClientboundDisconnectPlayPacket)
            register(0x2c, ClientboundKeepAlivePlayPacket)
            register(0x31, ClientboundLoginPlayPacket)
            register(0x3d, ClientboundPingPlayPacket)
            register(0x41, ClientboundPlayerChatMessagePacket)
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
            register(0x05, ServerboundPongConfigurationPacket)
            register(0x07, ServerboundSelectKnownPacksPacket)
        }
        register(ProtocolState.LOGIN) {
            register(0x00, ServerboundLoginStartPacket)
            register(0x03, ServerboundLoginAcknowledgedPacket)
        }
        register(ProtocolState.PLAY) {
            register(0x09, ServerboundChatMessagePacket)
            register(0x2d, ServerboundPongPlayPacket)
            register(0x10, ServerboundConfigurationAcknowledgedPacket)
            register(0x1c, ServerboundKeepAlivePlayPacket)
        }
    }
}