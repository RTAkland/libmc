/*
 * Copyright © 2026 RTAkland
 * Author: RTAkland
 * Date: 2026/9/5
 */


package cn.rtast.libmc.protocol.protocol.game.item

public enum class RecipeBookType(public val id: Int) {
    CRAFTING(0),
    FURNACE(1),
    BLAST_FURNACE(2),
    SMOKER(3);

    public companion object {
        public fun fromID(id: Int): RecipeBookType = entries.first { it.id == id }
    }
}