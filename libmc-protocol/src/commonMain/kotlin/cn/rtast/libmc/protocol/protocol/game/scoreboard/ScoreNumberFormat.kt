/*
 * Copyright © 2026 RTAkland
 * Author: RTAkland
 * Date: 2026/9/6
 */


package cn.rtast.libmc.protocol.protocol.game.scoreboard

import cn.rtast.libmc.nbt.NBTCompound
import cn.rtast.libmc.nbt.NBTTag

public sealed interface ScoreNumberFormat {
    public object Blank : ScoreNumberFormat
    public data class Styled(val styling: NBTTag.CompoundTag) : ScoreNumberFormat
    public data class Fixed(val content: NBTCompound) : ScoreNumberFormat
}