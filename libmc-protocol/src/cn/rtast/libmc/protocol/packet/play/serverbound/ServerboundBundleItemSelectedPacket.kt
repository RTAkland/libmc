/*
 * Copyright © 2026 RTAkland
 * Author: RTAkland
 * Date: 2026/9/5
 */


package cn.rtast.libmc.protocol.packet.play.serverbound

import cn.rtast.libmc.network.BytesBuffer
import cn.rtast.libmc.packet.PacketCodec
import cn.rtast.libmc.packet.MinecraftPacket
import cn.rtast.libmc.primitives.writeVarInt

public data class ServerboundBundleItemSelectedPacket(val slotOfBundle: Int, val slotInBundle: Int) : MinecraftPacket {
    internal companion object Codec : PacketCodec<ServerboundBundleItemSelectedPacket> {
        override fun encode(buffer: BytesBuffer, value: ServerboundBundleItemSelectedPacket) {
            buffer.writeVarInt(value.slotOfBundle)
            buffer.writeVarInt(value.slotInBundle)
        }

        override fun decode(buffer: BytesBuffer): ServerboundBundleItemSelectedPacket =
            throw UnsupportedOperationException()
    }
}