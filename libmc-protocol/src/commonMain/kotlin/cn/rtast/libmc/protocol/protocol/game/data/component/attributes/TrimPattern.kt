/*
 * Copyright © 2026 RTAkland
 * Author: RTAkland
 * Date: 2026/9/10
 */


package cn.rtast.libmc.protocol.protocol.game.data.component.attributes

import cn.rtast.libmc.protocol.protocol.game.chat.TextComponent

public data class TrimPattern(
    val assetName: String,
    val templateItem: Int,
    val description: TextComponent,
    val decal: Boolean,
)