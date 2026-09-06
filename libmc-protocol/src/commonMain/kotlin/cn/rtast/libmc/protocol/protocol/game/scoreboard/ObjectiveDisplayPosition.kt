/*
 * Copyright © 2026 RTAkland
 * Author: RTAkland
 * Date: 2026/9/6
 */


package cn.rtast.libmc.protocol.protocol.game.scoreboard

public sealed interface ObjectiveDisplayPosition {
    public object List : ObjectiveDisplayPosition
    public object Sidebar : ObjectiveDisplayPosition
    public object BelowName : ObjectiveDisplayPosition
    public data class TeamSidebar(val teamColorId: Int) : ObjectiveDisplayPosition

    public companion object {
        public fun fromID(id: Int): ObjectiveDisplayPosition = when (id) {
            0 -> List
            1 -> Sidebar
            2 -> BelowName
            in 3..18 -> TeamSidebar(teamColorId = id - 3)
            else -> Sidebar
        }
    }
}