/*
 * Copyright © 2026 RTAkland
 * Author: RTAkland
 * Date: 2026/9/11
 */


package cn.rtast.libmc.protocol.protocol.game.team

import cn.rtast.libmc.protocol.protocol.game.chat.TextComponent

public sealed interface TeamAction {
    public val methodId: Int

    public data class CreateTeam(
        val teamInfo: TeamInfo,
        val entities: List<String>,
    ) : TeamAction {
        override val methodId: Int = 0
    }

    public data object RemoveTeam : TeamAction {
        override val methodId: Int = 1
    }

    public data class UpdateTeamInfo(val teamInfo: TeamInfo) : TeamAction {
        override val methodId: Int = 2
    }

    public data class AddEntities(val entities: List<String>) : TeamAction {
        override val methodId: Int = 3
    }

    public data class RemoveEntities(val entities: List<String>) : TeamAction {
        override val methodId: Int = 4
    }

    public data class TeamInfo(
        val displayName: TextComponent,
        val prefix: TextComponent,
        val suffix: TextComponent,
        val nameTagVisibility: NameTagVisibility,
        val collisionRule: CollisionRule,
        val teamColor: Int,
        val friendlyFlags: Byte,
    ) {
        public val allowFriendlyFire: Boolean get() = (friendlyFlags.toInt() and 0x01) != 0
        public val canSeeFriendlyInvisible: Boolean get() = (friendlyFlags.toInt() and 0x02) != 0
    }
}