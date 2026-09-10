/*
 * Copyright © 2026 RTAkland
 * Author: RTAkland
 * Date: 2026/9/10
 */


package cn.rtast.libmc.protocol.protocol.game.potion

import cn.rtast.libmc.network.BytesBuffer
import cn.rtast.libmc.primitives.readPrefixOptional
import cn.rtast.libmc.primitives.readVarInt
import cn.rtast.libmc.primitives.writePrefixedOptional
import cn.rtast.libmc.primitives.writeVarInt

public data class PotionEffect(
    val effectId: Int,
    val amplifier: Int,
    val duration: Int,
    val ambient: Boolean,
    val showParticles: Boolean,
    val showIcon: Boolean,
    val hideEffect: PotionEffect?,
)

internal fun BytesBuffer.readPotionEffect(): PotionEffect {
    val effectId = readVarInt()
    val amplifier = readVarInt()
    val duration = readVarInt()
    val ambient = readBoolean()
    val showParticles = readBoolean()
    val showIcon = readBoolean()
    val hideEffect = readPrefixOptional { readPotionEffect() }
    return PotionEffect(effectId, amplifier, duration, ambient, showParticles, showIcon, hideEffect)
}

internal fun BytesBuffer.writePotionEffect(effect: PotionEffect) {
    writeVarInt(effect.effectId)
    writeVarInt(effect.amplifier)
    writeVarInt(effect.duration)
    writeBoolean(effect.ambient)
    writeBoolean(effect.showParticles)
    writeBoolean(effect.showIcon)
    writePrefixedOptional(effect.hideEffect) { writePotionEffect(it) }
}