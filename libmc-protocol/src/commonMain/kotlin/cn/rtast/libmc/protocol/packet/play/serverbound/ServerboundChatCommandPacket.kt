/*
 * Copyright © 2026 RTAkland
 * Author: RTAkland
 * Date: 2026/9/5
 */


package cn.rtast.libmc.protocol.packet.play.serverbound

import cn.rtast.libmc.network.BytesBuffer
import cn.rtast.libmc.packet.PacketCodec
import cn.rtast.libmc.packet.MinecraftPacket
import cn.rtast.libmc.primitives.writeMcString

public data class ServerboundChatCommandPacket(val command: String) : MinecraftPacket {
    internal companion object Codec : PacketCodec<ServerboundChatCommandPacket> {
        override fun encode(buffer: BytesBuffer, value: ServerboundChatCommandPacket) {
            buffer.writeMcString(value.command.removePrefix("/"))
        }

        override fun decode(buffer: BytesBuffer): ServerboundChatCommandPacket =
            throw UnsupportedOperationException()
    }
}