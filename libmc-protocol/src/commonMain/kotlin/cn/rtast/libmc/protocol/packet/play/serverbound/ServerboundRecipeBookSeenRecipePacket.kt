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

public data class ServerboundRecipeBookSeenRecipePacket(val recipeId: Int) : MinecraftPacket {
    public companion object Codec : PacketCodec<ServerboundRecipeBookSeenRecipePacket> {
        override fun encode(buffer: BytesBuffer, value: ServerboundRecipeBookSeenRecipePacket) {
            buffer.writeVarInt(value.recipeId)
        }

        override fun decode(buffer: BytesBuffer): ServerboundRecipeBookSeenRecipePacket =
            throw UnsupportedOperationException()
    }
}