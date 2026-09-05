/*
 * Copyright © 2026 RTAkland
 * Author: RTAkland
 * Date: 2026/9/4
 */


package cn.rtast.libmc.protocol.packet.configuration

import cn.rtast.libmc.common.BytesBuffer
import cn.rtast.libmc.common.PacketCodec
import cn.rtast.libmc.common.packet.MinecraftPacket

public data class ServerboundPongPacket(val id: Int) : MinecraftPacket {
    public companion object Codec : PacketCodec<ServerboundPongPacket> {
        override fun encode(buffer: BytesBuffer, value: ServerboundPongPacket) {
            buffer.writeInt(value.id)
        }

        override fun decode(buffer: BytesBuffer): ServerboundPongPacket = ServerboundPongPacket(buffer.readInt())
    }
}