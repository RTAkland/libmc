/*
 * Copyright © 2026 RTAkland
 * Author: RTAkland
 * Date: 2026/9/5
 */


package cn.rtast.libmc.protocol.packet.play.serverbound

import cn.rtast.libmc.common.*
import cn.rtast.libmc.common.packet.MinecraftPacket

public data class ServerboundEditBookPacket(val slot: Int, val entries: List<String>, val title: String?) :
    MinecraftPacket {
    public companion object Codec : PacketCodec<ServerboundEditBookPacket> {
        override fun encode(buffer: BytesBuffer, value: ServerboundEditBookPacket) {
            buffer.writeVarInt(value.slot)
            buffer.writePrefixedStringArray(value.entries)
            val hasTitle = value.title != null
            buffer.writeBoolean(hasTitle)
            if (hasTitle) buffer.writeMcString(value.title)
        }

        override fun decode(buffer: BytesBuffer): ServerboundEditBookPacket =
            throw UnsupportedOperationException()
    }
}