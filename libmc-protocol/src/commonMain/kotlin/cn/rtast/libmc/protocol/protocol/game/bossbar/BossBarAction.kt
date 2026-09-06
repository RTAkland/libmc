/*
 * Copyright © 2026 RTAkland
 * Author: RTAkland
 * Date: 2026/9/6
 */


package cn.rtast.libmc.protocol.protocol.game.bossbar

import cn.rtast.libmc.nbt.NBTCompound

public sealed interface BossBarAction {
    public data class Add(
        val title: NBTCompound,
        val health: Float,
        val color: BossBarColor,
        val division: BossBarDivision,
        val flags: BossBarFlags,
    ) : BossBarAction

    public object Remove : BossBarAction
    public data class UpdateHealth(val health: Float) : BossBarAction
    public data class UpdateTitle(val title: NBTCompound) : BossBarAction
    public data class UpdateStyle(val color: BossBarColor, val division: BossBarDivision) : BossBarAction
    public data class UpdateFlags(val flags: BossBarFlags) : BossBarAction

    public companion object {
        internal const val ADD_ID = 0
        internal const val REMOVE_ID = 1
        internal const val UPDATE_HEALTH_ID = 2
        internal const val UPDATE_TITLE_ID = 3
        internal const val UPDATE_STYLE_ID = 4
        internal const val UPDATE_FLAGS_ID = 5
    }
}