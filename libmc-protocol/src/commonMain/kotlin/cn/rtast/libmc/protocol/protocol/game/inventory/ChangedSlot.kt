/*
 * Copyright © 2026 RTAkland
 * Author: RTAkland
 * Date: 2026/9/5
 */


package cn.rtast.libmc.protocol.protocol.game.inventory

import cn.rtast.libmc.common.BytesBuffer
import cn.rtast.libmc.common.PacketCodec
import cn.rtast.libmc.common.readVarInt
import cn.rtast.libmc.common.writeVarInt
import kotlinx.serialization.Serializable

@Serializable
public data class ChangedSlot(val slotNumber: Int, val item: HashedSlot) {
    public companion object Codec : PacketCodec<ChangedSlot> {
        override fun encode(buffer: BytesBuffer, value: ChangedSlot) {
            buffer.writeVarInt(value.slotNumber)
            HashedSlot.encode(buffer, value.item)
        }

        override fun decode(buffer: BytesBuffer): ChangedSlot {
            val slotNumber = buffer.readVarInt()
            val item = HashedSlot.decode(buffer)
            return ChangedSlot(slotNumber, item)
        }
    }
}