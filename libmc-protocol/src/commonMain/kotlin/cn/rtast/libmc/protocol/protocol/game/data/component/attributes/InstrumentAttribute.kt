/*
 * Copyright © 2026 RTAkland
 * Author: RTAkland
 * Date: 2026/9/10
 */


package cn.rtast.libmc.protocol.protocol.game.data.component.attributes

import cn.rtast.libmc.primitives.IdOrX
import cn.rtast.libmc.protocol.protocol.game.chat.TextComponent
import cn.rtast.libmc.protocol.protocol.game.sound.SoundEvent

public data class InstrumentAttribute(
    val sound: IdOrX<SoundEvent>,
    val useDuration: Float,
    val range: Float,
    val description: TextComponent,
)
