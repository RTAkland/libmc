/*
 * Copyright © 2026 RTAkland
 * Author: RTAkland
 * Date: 2026/9/5
 */


package cn.rtast.libmc.protocol.protocol.game.debug

public enum class DebugSubscriptionType(public val id: Int) {
    BEES(0),
    BRAIN(1),
    GAME_TEST(2),
    GAME_TEST_MARKER(3),
    GOAL_SELECTOR(4),
    NEIGHBOR_UPDATES(5),
    PATH_FINDING(6),
    RAIDS(7),
    STRUCTURES(8),
    WORLD_GEN(9);

    public companion object {
        public fun fromID(id: Int): DebugSubscriptionType =
            requireNotNull(entries.firstOrNull { it.id == id }) { "Unknown debug subscription id $id" }
    }
}