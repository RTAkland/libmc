/*
 * Copyright © 2026 RTAkland
 * Author: RTAkland
 * Date: 2026/9/6
 */


package cn.rtast.libmc.protocol.protocol.game.block

public enum class StructureBlockAction(public val id: Int) {
    UPDATE_DATA(0),
    SAVE(1),
    LOAD(2),
    DETECT_SIZE(3);

    public companion object {
        public fun fromID(id: Int): StructureBlockAction = entries.first { it.id == id }
    }
}

public enum class StructureBlockMode(public val id: Int) {
    SAVE(0),
    LOAD(1),
    CORNER(2),
    DATA(3);

    public companion object {
        public fun fromID(id: Int): StructureBlockMode = entries.first { it.id == id }
    }
}

public enum class StructureMirror(public val id: Int) {
    NONE(0),
    LEFT_RIGHT(1),
    FRONT_BACK(2);

    public companion object {
        public fun fromID(id: Int): StructureMirror = entries.first { it.id == id }
    }
}

public enum class StructureRotation(public val id: Int) {
    NONE(0),
    CLOCKWISE_90(1),
    CLOCKWISE_180(2),
    COUNTERCLOCKWISE_90(3);

    public companion object {
        public fun fromID(id: Int): StructureRotation = entries.first { it.id == id }
    }
}