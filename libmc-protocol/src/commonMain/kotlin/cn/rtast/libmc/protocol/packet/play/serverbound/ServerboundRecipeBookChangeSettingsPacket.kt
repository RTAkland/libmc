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
import cn.rtast.libmc.protocol.protocol.game.item.RecipeBookType

public data class ServerboundRecipeBookChangeSettingsPacket(
    val bookId: RecipeBookType,
    val bookOpen: Boolean,
    val filterActive: Boolean,
) : MinecraftPacket {
    public companion object Codec : PacketCodec<ServerboundRecipeBookChangeSettingsPacket> {
        override fun encode(buffer: BytesBuffer, value: ServerboundRecipeBookChangeSettingsPacket) {
            buffer.writeVarInt(value.bookId.id)
            buffer.writeBoolean(value.bookOpen)
            buffer.writeBoolean(value.filterActive)
        }

        override fun decode(buffer: BytesBuffer): ServerboundRecipeBookChangeSettingsPacket =
            throw UnsupportedOperationException()
    }
}