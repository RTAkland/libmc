/*
 * Copyright © 2026 RTAkland
 * Author: RTAkland
 * Date: 2026/9/5
 */


package cn.rtast.libmc.protocol.packet.play.serverbound

import cn.rtast.libmc.common.BytesBuffer
import cn.rtast.libmc.common.PacketCodec
import cn.rtast.libmc.common.packet.MinecraftPacket
import cn.rtast.libmc.common.writeVarInt

public data class ServerboundBundleItemSelectedPacket(val slotOfBundle: Int, val slotInBundle: Int) : MinecraftPacket {
    public companion object Codec : PacketCodec<ServerboundBundleItemSelectedPacket> {
        override fun encode(buffer: BytesBuffer, value: ServerboundBundleItemSelectedPacket) {
            buffer.writeVarInt(value.slotOfBundle)
            buffer.writeVarInt(value.slotInBundle)
        }

        override fun decode(buffer: BytesBuffer): ServerboundBundleItemSelectedPacket =
            throw UnsupportedOperationException()
    }
}