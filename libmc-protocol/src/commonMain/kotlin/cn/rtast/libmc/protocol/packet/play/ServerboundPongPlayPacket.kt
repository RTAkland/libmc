/*
 * Copyright © 2026 RTAkland
 * Author: RTAkland
 * Date: 2026/9/5
 */

package cn.rtast.libmc.protocol.packet.play

import cn.rtast.libmc.common.BytesBuffer
import cn.rtast.libmc.common.PacketCodec
import cn.rtast.libmc.common.packet.MinecraftPacket

public data class ServerboundPongPlayPacket(val id: Int) : MinecraftPacket {
    public companion object Codec : PacketCodec<ServerboundPongPlayPacket> {
        override fun encode(buffer: BytesBuffer, value: ServerboundPongPlayPacket) {
            buffer.writeInt(value.id)
        }

        override fun decode(buffer: BytesBuffer): ServerboundPongPlayPacket {
            return ServerboundPongPlayPacket(id = buffer.readInt())
        }
    }
}