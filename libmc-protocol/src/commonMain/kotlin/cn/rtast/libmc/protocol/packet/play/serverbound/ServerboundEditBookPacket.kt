/*
 * Copyright © 2026 RTAkland
 * Author: RTAkland
 * Date: 2026/9/5
 */


package cn.rtast.libmc.protocol.packet.play.serverbound

import cn.rtast.libmc.packet.MinecraftPacket
import cn.rtast.libmc.packet.PacketCodec
import cn.rtast.libmc.primitives.writeMcString
import cn.rtast.libmc.primitives.writePrefixedStringArray
import cn.rtast.libmc.primitives.writeVarInt
import cn.rtast.libmc.network.BytesBuffer

public data class ServerboundEditBookPacket(val slot: Int, val entries: List<String>, val title: String?) :
    MinecraftPacket {
    internal companion object Codec : PacketCodec<ServerboundEditBookPacket> {
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