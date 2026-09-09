/*
 * Copyright © 2026 RTAkland
 * Author: RTAkland
 * Date: 2026/9/9
 */


package cn.rtast.libmc.protocol.protocol.game.item

public enum class DyeColor(public val id: Int) {
    WHITE(0), ORANGE(1), MAGENTA(2), LIGHT_BLUE(3),
    YELLOW(4), LIME(5), PINK(6), GRAY(7), LIGHT_GRAY(8),
    CYAN(9), PURPLE(10), BLUE(11), BROWN(12), GREEN(13),
    RED(14), BLACK(15);

    public companion object {
        private val CACHED_ID = ArrayList<DyeColor>(entries.size).apply {
            for (color in entries) this[color.id] = color
        }

        public fun fromID(id: Int): DyeColor = CACHED_ID.first { it.id == id }
    }
}