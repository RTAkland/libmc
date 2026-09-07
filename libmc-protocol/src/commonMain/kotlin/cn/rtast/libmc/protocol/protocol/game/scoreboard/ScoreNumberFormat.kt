/*
 * Copyright © 2026 RTAkland
 * Author: RTAkland
 * Date: 2026/9/6
 */


package cn.rtast.libmc.protocol.protocol.game.scoreboard

import cn.rtast.libmc.nbt.NBTTag
import cn.rtast.libmc.protocol.protocol.game.chat.TextComponent

public sealed interface ScoreNumberFormat {
    public object Blank : ScoreNumberFormat
    public data class Styled(val styling: NBTTag.CompoundTag) : ScoreNumberFormat
    public data class Fixed(val content: TextComponent) : ScoreNumberFormat
}