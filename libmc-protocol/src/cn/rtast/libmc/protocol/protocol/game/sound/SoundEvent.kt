/*
 * Copyright © 2026 RTAkland
 * Author: RTAkland
 * Date: 2026/9/6
 */


package cn.rtast.libmc.protocol.protocol.game.sound

import cn.rtast.libmc.network.BytesBuffer
import cn.rtast.libmc.primitives.readOptional
import cn.rtast.libmc.primitives.writeOptional
import cn.rtast.libmc.protocol.protocol.game.Identifier
import cn.rtast.libmc.protocol.protocol.game.readIdentifier
import cn.rtast.libmc.protocol.protocol.game.writeIdentifier

public data class SoundEvent(val name: Identifier, val hasFixedRange: Boolean, val fixedRange: Float?)

internal fun BytesBuffer.readSoundEvent(): SoundEvent {
    val name = readIdentifier()
    val hasFixedRange = readBoolean()
    val fixedRange = readOptional(hasFixedRange) { readFloat() }
    return SoundEvent(name, hasFixedRange, fixedRange)
}

internal fun BytesBuffer.writeSoundEvent(event: SoundEvent) {
    writeIdentifier(event.name)
    writeBoolean(event.hasFixedRange)
    writeOptional(event.fixedRange) { writeFloat(it) }
}