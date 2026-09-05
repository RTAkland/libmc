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

public data class ServerboundChangeContainerSlotStatePacket(val slotId: Int, val windowId: Int, val state: Boolean) :
    MinecraftPacket {
    public companion object Codec : PacketCodec<ServerboundChangeContainerSlotStatePacket> {
        override fun encode(buffer: BytesBuffer, value: ServerboundChangeContainerSlotStatePacket) {
            buffer.writeVarInt(value.slotId)
            buffer.writeVarInt(value.windowId)
            buffer.writeBoolean(value.state)
        }

        override fun decode(buffer: BytesBuffer): ServerboundChangeContainerSlotStatePacket =
            throw UnsupportedOperationException()
    }
}