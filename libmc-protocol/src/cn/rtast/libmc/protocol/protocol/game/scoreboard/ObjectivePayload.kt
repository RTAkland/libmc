/*
 * Copyright © 2026 RTAkland
 * Author: RTAkland
 * Date: 2026/9/6
 */


package cn.rtast.libmc.protocol.protocol.game.scoreboard

import cn.rtast.libmc.protocol.protocol.game.chat.TextComponent

public sealed interface ObjectivePayload {
    public object Remove : ObjectivePayload
    public data class Upsert(
        val displayName: TextComponent,
        val renderType: ObjectiveRenderType,
        val defaultNumberFormat: ScoreNumberFormat?,
    ) : ObjectivePayload
}