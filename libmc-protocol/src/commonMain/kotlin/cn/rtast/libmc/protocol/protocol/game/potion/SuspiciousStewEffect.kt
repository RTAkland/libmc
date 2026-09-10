/*
 * Copyright © 2026 RTAkland
 * Author: RTAkland
 * Date: 2026/9/10
 */


package cn.rtast.libmc.protocol.protocol.game.potion

import cn.rtast.libmc.network.BytesBuffer
import cn.rtast.libmc.primitives.readVarInt
import cn.rtast.libmc.primitives.writeVarInt

public data class SuspiciousStewEffect(val effectId: Int, val duration: Int)

internal fun BytesBuffer.readSuspiciousStewEffect(): SuspiciousStewEffect {
    val effectId = readVarInt()
    val duration = readVarInt()
    return SuspiciousStewEffect(
        effectId = effectId,
        duration = duration
    )
}

internal fun BytesBuffer.writeSuspiciousStewEffect(effect: SuspiciousStewEffect) {
    writeVarInt(effect.effectId)
    writeVarInt(effect.duration)
}