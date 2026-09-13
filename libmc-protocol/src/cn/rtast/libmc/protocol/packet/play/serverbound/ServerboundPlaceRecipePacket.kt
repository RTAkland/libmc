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

public data class ServerboundPlaceRecipePacket(
    val windowId: Int,
    val recipeId: Int,
    val makeAll: Boolean,
) : MinecraftPacket {
    internal companion object Codec : PacketCodec<ServerboundPlaceRecipePacket> {
        override fun encode(buffer: BytesBuffer, value: ServerboundPlaceRecipePacket) {
            buffer.writeVarInt(value.windowId)
            buffer.writeVarInt(value.recipeId)
            buffer.writeBoolean(value.makeAll)
        }

        override fun decode(buffer: BytesBuffer): ServerboundPlaceRecipePacket =
            throw UnsupportedOperationException()
    }
}