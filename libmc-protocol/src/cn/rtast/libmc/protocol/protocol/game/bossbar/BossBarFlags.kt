/*
 * Copyright © 2026 RTAkland
 * Author: RTAkland
 * Date: 2026/9/6
 */


package cn.rtast.libmc.protocol.protocol.game.bossbar

public data class BossBarFlags(
    val darkenSky: Boolean,
    val isDragonBar: Boolean,
    val createFog: Boolean,
) {
    public companion object {
        public fun fromBitmask(mask: Int): BossBarFlags = BossBarFlags(
            darkenSky = (mask and 0x01) != 0,
            isDragonBar = (mask and 0x02) != 0,
            createFog = (mask and 0x04) != 0
        )
    }
}