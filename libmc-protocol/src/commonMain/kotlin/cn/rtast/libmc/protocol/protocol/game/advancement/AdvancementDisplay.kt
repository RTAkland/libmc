/*
 * Copyright © 2026 RTAkland
 * Author: RTAkland
 * Date: 2026/9/11
 */


package cn.rtast.libmc.protocol.protocol.game.advancement

import cn.rtast.libmc.protocol.protocol.game.Identifier
import cn.rtast.libmc.protocol.protocol.game.chat.TextComponent
import cn.rtast.libmc.protocol.protocol.game.item.slot.Slot

public data class AdvancementDisplay(
    val title: TextComponent,
    val description: TextComponent,
    val icon: Slot,
    val frameType: AdvancementFrameType,
    val flags: Int,
    val backgroundTexture: Identifier?,
    val x: Float,
    val y: Float
) {
    public val hasBackgroundTexture: Boolean get() = (flags and 0x01) != 0
    public val showToast: Boolean get() = (flags and 0x02) != 0
    public val isHidden: Boolean get() = (flags and 0x04) != 0
}