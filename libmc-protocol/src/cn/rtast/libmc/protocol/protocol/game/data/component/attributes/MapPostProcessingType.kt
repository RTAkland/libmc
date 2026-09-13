/*
 * Copyright © 2026 RTAkland
 * Author: RTAkland
 * Date: 2026/9/10
 */


package cn.rtast.libmc.protocol.protocol.game.data.component.attributes

public enum class MapPostProcessingType(public val id: Int) {
    LOCK(0), SCALE(1);

    public companion object {
        public fun fromID(id: Int): MapPostProcessingType = entries.first { it.id == id }
    }
}