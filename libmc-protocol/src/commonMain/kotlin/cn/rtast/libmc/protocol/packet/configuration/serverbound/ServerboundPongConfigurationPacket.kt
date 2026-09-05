/*
 * Copyright © 2026 RTAkland
 * Author: RTAkland
 * Date: 2026/9/4
 */


package cn.rtast.libmc.protocol.packet.configuration.serverbound

import cn.rtast.libmc.common.BytesBuffer
import cn.rtast.libmc.common.PacketCodec
import cn.rtast.libmc.common.packet.MinecraftPacket

public data class ServerboundPongConfigurationPacket(val id: Int) : MinecraftPacket {
    public companion object Codec : PacketCodec<ServerboundPongConfigurationPacket> {
        override fun encode(buffer: BytesBuffer, value: ServerboundPongConfigurationPacket) {
            buffer.writeInt(value.id)
        }

        override fun decode(buffer: BytesBuffer): ServerboundPongConfigurationPacket = ServerboundPongConfigurationPacket(buffer.readInt())
    }
}