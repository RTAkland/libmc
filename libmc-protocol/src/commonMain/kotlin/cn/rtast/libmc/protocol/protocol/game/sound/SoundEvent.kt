/*
 * Copyright © 2026 RTAkland
 * Author: RTAkland
 * Date: 2026/9/6
 */


package cn.rtast.libmc.protocol.protocol.game.sound

import cn.rtast.libmc.common.stream.BytesBuffer
import cn.rtast.libmc.common.primitives.readOptional
import cn.rtast.libmc.common.primitives.writeOptional
import cn.rtast.libmc.protocol.protocol.game.Identifier
import cn.rtast.libmc.protocol.protocol.game.readIdentifier
import cn.rtast.libmc.protocol.protocol.game.writeIdentifier

public data class SoundEvent(val name: Identifier, val hasFixedRange: Boolean, val fixedRange: Float?)

internal suspend fun BytesBuffer.readSoundEvent(): SoundEvent {
    val name = readIdentifier()
    val hasFixedRange = readBoolean()
    val fixedRange = readOptional { readFloat() }
    return SoundEvent(name, hasFixedRange, fixedRange)
}

internal suspend fun BytesBuffer.writeSoundEvent(event: SoundEvent) {
    writeIdentifier(event.name)
    writeBoolean(event.hasFixedRange)
    writeOptional(event.fixedRange) { writeFloat(it) }
}