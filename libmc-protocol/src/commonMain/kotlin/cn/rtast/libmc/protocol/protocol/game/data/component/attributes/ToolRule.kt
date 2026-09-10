/*
 * Copyright © 2026 RTAkland
 * Author: RTAkland
 * Date: 2026/9/10
 */


package cn.rtast.libmc.protocol.protocol.game.data.component.attributes

import cn.rtast.libmc.primitives.IdSet

public data class ToolRule(
    val blocks: IdSet,
    val speed: Float?,
    val correctForDrops: Boolean?,
)