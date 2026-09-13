/*
 * Copyright © 2026 RTAkland
 * Author: RTAkland
 * Date: 2026/9/11
 */


package cn.rtast.libmc.protocol.registry

import cn.rtast.libmc.network.BytesBuffer
import cn.rtast.libmc.protocol.protocol.game.recipe.RecipeDisplayData

internal object RecipeDisplayDataRegistry : ArrayIndexedRegistry<RecipeDisplayData>("RecipeDisplayDataRegistry") {
    init {
        register(RecipeDisplayData.CraftingShapeless)
        register(RecipeDisplayData.CraftingShaped)
        register(RecipeDisplayData.Furnace)
        register(RecipeDisplayData.Stonecutter)
        register(RecipeDisplayData.Smithing)

        freeze()
    }
}

internal fun BytesBuffer.readRecipeDisplayData(typeId: Int) = RecipeDisplayDataRegistry.read(typeId, this)
internal fun BytesBuffer.writeRecipeDisplayData(data: RecipeDisplayData) = RecipeDisplayDataRegistry.write(this, data)