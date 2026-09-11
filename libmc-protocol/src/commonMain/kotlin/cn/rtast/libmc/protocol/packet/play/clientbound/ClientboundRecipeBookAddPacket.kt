/*
 * Copyright © 2026 RTAkland
 * Author: RTAkland
 * Date: 2026/9/11
 */


package cn.rtast.libmc.protocol.packet.play.clientbound

import cn.rtast.libmc.network.BytesBuffer
import cn.rtast.libmc.packet.MinecraftPacket
import cn.rtast.libmc.packet.PacketCodec
import cn.rtast.libmc.primitives.readIdSet
import cn.rtast.libmc.primitives.readPrefixOptional
import cn.rtast.libmc.primitives.readPrefixed
import cn.rtast.libmc.primitives.readVarInt
import cn.rtast.libmc.protocol.protocol.game.recipe.RecipeEntry
import cn.rtast.libmc.protocol.protocol.game.recipe.readRecipeDisplay

public data class ClientboundRecipeBookAddPacket(val recipes: List<RecipeEntry>, val replace: Boolean) :
    MinecraftPacket {
    internal companion object Codec : PacketCodec<ClientboundRecipeBookAddPacket> {
        override fun encode(buffer: BytesBuffer, value: ClientboundRecipeBookAddPacket) {}
        override fun decode(buffer: BytesBuffer): ClientboundRecipeBookAddPacket {
            val recipes = buffer.readPrefixed {
                val recipeId = readVarInt()
                val display = readRecipeDisplay()
                val groupId = readVarInt()
                val categoryId = readVarInt()
                val ingredients = readPrefixOptional { readPrefixed { readIdSet() } }
                val flags = readByte()
                RecipeEntry(recipeId, display, groupId, categoryId, ingredients, flags)
            }
            val replace = buffer.readBoolean()
            return ClientboundRecipeBookAddPacket(recipes, replace)
        }
    }
}