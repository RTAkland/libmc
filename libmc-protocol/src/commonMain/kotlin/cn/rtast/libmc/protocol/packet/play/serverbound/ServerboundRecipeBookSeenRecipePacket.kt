/*
 * Copyright © 2026 RTAkland
 * Author: RTAkland
 * Date: 2026/9/5
 */


package cn.rtast.libmc.protocol.packet.play.serverbound

import cn.rtast.libmc.stream.BytesBuffer
import cn.rtast.libmc.packet.PacketCodec
import cn.rtast.libmc.packet.MinecraftPacket
import cn.rtast.libmc.primitives.writeVarInt

public data class ServerboundRecipeBookSeenRecipePacket(val recipeId: Int) : MinecraftPacket {
    internal companion object Codec : PacketCodec<ServerboundRecipeBookSeenRecipePacket> {
        override suspend fun encode(buffer: BytesBuffer, value: ServerboundRecipeBookSeenRecipePacket) {
            buffer.writeVarInt(value.recipeId)
        }

        override suspend fun decode(buffer: BytesBuffer): ServerboundRecipeBookSeenRecipePacket =
            throw UnsupportedOperationException()
    }
}