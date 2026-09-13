/*
 * Copyright © 2026 RTAkland
 * Author: RTAkland
 * Date: 2026/9/11
 */


package cn.rtast.libmc.protocol.protocol.game.recipe

import cn.rtast.libmc.primitives.IdSet

public data class RecipeEntry(
    val recipeId: Int,
    val display: RecipeDisplay,
    val groupId: Int,
    val categoryId: Int,
    val ingredients: List<IdSet>?,
    val flags: Byte
) {
    val showNotification: Boolean get() = (flags.toInt() and 0x01) != 0
    val highlightAsNew: Boolean get() = (flags.toInt() and 0x02) != 0
}