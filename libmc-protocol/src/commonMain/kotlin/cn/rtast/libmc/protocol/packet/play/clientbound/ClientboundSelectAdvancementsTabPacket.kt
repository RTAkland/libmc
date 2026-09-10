/*
 * Copyright © 2026 RTAkland
 * Author: RTAkland
 * Date: 2026/9/6
 */


package cn.rtast.libmc.protocol.packet.play.clientbound

import cn.rtast.libmc.network.BytesBuffer
import cn.rtast.libmc.packet.MinecraftPacket
import cn.rtast.libmc.packet.PacketCodec
import cn.rtast.libmc.primitives.readPrefixOptional
import cn.rtast.libmc.protocol.protocol.game.Identifier
import cn.rtast.libmc.protocol.protocol.game.readIdentifier

public data class ClientboundSelectAdvancementsTabPacket(val tabId: Identifier?) : MinecraftPacket {
    internal companion object Codec : PacketCodec<ClientboundSelectAdvancementsTabPacket> {
        override fun encode(buffer: BytesBuffer, value: ClientboundSelectAdvancementsTabPacket) {}
        override fun decode(buffer: BytesBuffer): ClientboundSelectAdvancementsTabPacket {
            return ClientboundSelectAdvancementsTabPacket(buffer.readPrefixOptional { readIdentifier() })
        }
    }
}