/*
 * Copyright © 2026 RTAkland
 * Author: RTAkland
 * Date: 2026/9/5
 */


package cn.rtast.libmc.protocol.packet.configuration.clientbound

import cn.rtast.libmc.common.BytesBuffer
import cn.rtast.libmc.common.PacketCodec
import cn.rtast.libmc.protocol.protocol.game.Identifier
import cn.rtast.libmc.protocol.protocol.game.readIdentifier
import cn.rtast.libmc.protocol.protocol.game.writeIdentifier

public data class ClientboundCookieRequestPacket(val key: Identifier) : ClientboundConfigurationPacket {
    public companion object Codec : PacketCodec<ClientboundCookieRequestPacket> {
        override fun encode(buffer: BytesBuffer, value: ClientboundCookieRequestPacket) {
            buffer.writeIdentifier(value.key)
        }

        override fun decode(buffer: BytesBuffer): ClientboundCookieRequestPacket {
            return ClientboundCookieRequestPacket(key = buffer.readIdentifier())
        }
    }
}