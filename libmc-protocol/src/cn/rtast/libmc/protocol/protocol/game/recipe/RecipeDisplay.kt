/*
 * Copyright © 2026 RTAkland
 * Author: RTAkland
 * Date: 2026/9/11
 */


package cn.rtast.libmc.protocol.protocol.game.recipe

import cn.rtast.libmc.network.BytesBuffer
import cn.rtast.libmc.primitives.readVarInt
import cn.rtast.libmc.primitives.writeVarInt
import cn.rtast.libmc.protocol.registry.readRecipeDisplayData
import cn.rtast.libmc.protocol.registry.writeRecipeDisplayData

public data class RecipeDisplay(val displayType: Int, val data: RecipeDisplayData)

internal fun BytesBuffer.readRecipeDisplay(): RecipeDisplay {
    val displayType = readVarInt()
    val data = readRecipeDisplayData(displayType)
    return RecipeDisplay(displayType, data)
}

internal fun BytesBuffer.writeRecipeDisplay(display: RecipeDisplay) {
    writeVarInt(display.displayType)
    writeRecipeDisplayData(display.data)
}