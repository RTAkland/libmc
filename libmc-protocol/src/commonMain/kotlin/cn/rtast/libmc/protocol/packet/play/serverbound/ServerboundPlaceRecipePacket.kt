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

public data class ServerboundPlaceRecipePacket(
    val windowId: Int,
    val recipeId: Int,
    val makeAll: Boolean,
) : MinecraftPacket {
    public companion object Codec : PacketCodec<ServerboundPlaceRecipePacket> {
        override fun encode(buffer: BytesBuffer, value: ServerboundPlaceRecipePacket) {
            buffer.writeVarInt(value.windowId)
            buffer.writeVarInt(value.recipeId)
            buffer.writeBoolean(value.makeAll)
        }

        override fun decode(buffer: BytesBuffer): ServerboundPlaceRecipePacket =
            throw UnsupportedOperationException()
    }
}