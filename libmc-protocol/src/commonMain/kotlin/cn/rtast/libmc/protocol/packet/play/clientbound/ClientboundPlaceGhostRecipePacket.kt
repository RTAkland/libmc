/*
 * Copyright © 2026 RTAkland
 * Author: RTAkland
 * Date: 2026/9/11
 */


package cn.rtast.libmc.protocol.packet.play.clientbound

import cn.rtast.libmc.network.BytesBuffer
import cn.rtast.libmc.packet.MinecraftPacket
import cn.rtast.libmc.packet.PacketCodec
import cn.rtast.libmc.primitives.readVarInt
import cn.rtast.libmc.protocol.protocol.game.recipe.RecipeDisplay
import cn.rtast.libmc.protocol.protocol.game.recipe.readRecipeDisplay

public data class ClientboundPlaceGhostRecipePacket(val windowId: Int, val recipeDisplay: RecipeDisplay) :
    MinecraftPacket {
    internal companion object Codec : PacketCodec<ClientboundPlaceGhostRecipePacket> {
        override fun encode(buffer: BytesBuffer, value: ClientboundPlaceGhostRecipePacket) {}
        override fun decode(buffer: BytesBuffer): ClientboundPlaceGhostRecipePacket {
            val windowId = buffer.readVarInt()
            val recipeDisplay = buffer.readRecipeDisplay()
            return ClientboundPlaceGhostRecipePacket(windowId, recipeDisplay)
        }
    }
}