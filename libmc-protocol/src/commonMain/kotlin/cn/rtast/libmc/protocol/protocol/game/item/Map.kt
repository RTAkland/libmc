/*
 * Copyright © 2026 RTAkland
 * Author: RTAkland
 * Date: 2026/9/7
 */


package cn.rtast.libmc.protocol.protocol.game.item

import cn.rtast.libmc.protocol.protocol.game.chat.TextComponent

public enum class MapItemIconType(public val id: Int) {
    WHITE_ARROW(0), GREEN_ARROW(1), RED_ARROW(2), BLUE_ARROW(3),
    WHITE_CROSS(4), RED_POINTER(5), WHITE_CIRCLE(6), SMALL_WHITE_CIRCLE(7),
    MANSION(8), MONUMENT(9), WHITE_BANNER(10), ORANGE_BANNER(11),
    MAGENTA_BANNER(12), LIGHT_BLUE_BANNER(13), YELLOW_BANNER(14),
    LIME_BANNER(15), PINK_BANNER(16), GRAY_BANNER(17), LIGHT_GRAY_BANNER(18),
    CYAN_BANNER(19), PURPLE_BANNER(20), BLUE_BANNER(21), BROWN_BANNER(22),
    GREEN_BANNER(23), RED_BANNER(24), BLACK_BANNER(25), TREASURE_MARKER(26),
    DESERT_VILLAGE(27), PLAINS_VILLAGE(28), SAVANNA_VILLAGE(29), SNOWY_VILLAGE(30),
    TAIGA_VILLAGE(31), JUNGLE_TEMPLE(32), SWAMP_HUT(33), TRIAL_CHAMBERS(34);

    public companion object {
        public fun fromID(id: Int): MapItemIconType = entries.find { it.id == id } ?: WHITE_ARROW
    }
}

public data class MapItemIcon(
    val type: MapItemIconType,
    val x: Byte,
    val z: Byte,
    val direction: Byte,
    val displayName: TextComponent?
)

public data class MapItemColorPatch(
    val columns: UByte,
    val rows: UByte,
    val xOffset: UByte,
    val zOffset: UByte,
    val data: ByteArray
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || this::class != other::class) return false
        other as MapItemColorPatch
        if (columns != other.columns) return false
        if (rows != other.rows) return false
        if (xOffset != other.xOffset) return false
        if (zOffset != other.zOffset) return false
        if (!data.contentEquals(other.data)) return false
        return true
    }

    override fun hashCode(): Int {
        var result = columns.hashCode()
        result = 31 * result + rows.hashCode()
        result = 31 * result + xOffset.hashCode()
        result = 31 * result + zOffset.hashCode()
        result = 31 * result + data.contentHashCode()
        return result
    }
}