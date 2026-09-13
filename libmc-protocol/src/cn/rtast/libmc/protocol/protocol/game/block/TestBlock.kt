/*
 * Copyright © 2026 RTAkland
 * Author: RTAkland
 * Date: 2026/9/6
 */


package cn.rtast.libmc.protocol.protocol.game.block

public enum class TestInstanceBlockAction(public val id: Int) {
    INIT(0),
    QUERY(1),
    SET(2),
    RESET(3),
    SAVE(4),
    EXPORT(5),
    RUN(6);

    public companion object {
        public fun fromID(id: Int): TestInstanceBlockAction = entries.first { it.id == id }
    }
}

public enum class TestBlockMode(public val id: Int) {
    START(0),
    LOG(1),
    FAIL(2),
    ACCEPT(3);

    public companion object {
        public fun fromID(id: Int): TestBlockMode = entries.first { it.id == id }
    }
}

public enum class TestInstanceRotation(public val id: Int) {
    NONE(0),
    CLOCKWISE_90(1),
    CLOCKWISE_180(2),
    COUNTERCLOCKWISE_90(3);

    public companion object {
        public fun fromID(id: Int): TestInstanceRotation = entries.first { it.id == id }
    }
}

public enum class TestInstanceStatus(public val id: Int) {
    CLEARED(0),
    RUNNING(1),
    FINISHED(2);

    public companion object {
        public fun fromID(id: Int): TestInstanceStatus = entries.first { it.id == id }
    }
}