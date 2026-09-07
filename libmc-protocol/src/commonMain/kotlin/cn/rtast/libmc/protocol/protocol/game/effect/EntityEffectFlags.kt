/*
 * Copyright © 2026 RTAkland
 * Author: RTAkland
 * Date: 2026/9/6
 */


package cn.rtast.libmc.protocol.protocol.game.effect

import cn.rtast.libmc.network.BytesBuffer

public data class EntityEffectFlags(
    val isAmbient: Boolean = false,
    val showParticles: Boolean = true,
    val showIcon: Boolean = true,
    val blend: Boolean = false,
) {
    public fun toByte(): Byte {
        var mask = 0
        if (isAmbient) mask = mask or MASK_AMBIENT
        if (showParticles) mask = mask or MASK_SHOW_PARTICLES
        if (showIcon) mask = mask or MASK_SHOW_ICON
        if (blend) mask = mask or MASK_BLEND
        return mask.toByte()
    }

    public companion object {
        public const val MASK_AMBIENT: Int = 0x01
        public const val MASK_SHOW_PARTICLES: Int = 0x02
        public const val MASK_SHOW_ICON: Int = 0x04
        public const val MASK_BLEND: Int = 0x08

        public fun fromByte(flags: Byte): EntityEffectFlags = fromInt(flags.toInt())

        public fun fromInt(mask: Int): EntityEffectFlags {
            return EntityEffectFlags(
                isAmbient = (mask and MASK_AMBIENT) != 0,
                showParticles = (mask and MASK_SHOW_PARTICLES) != 0,
                showIcon = (mask and MASK_SHOW_ICON) != 0,
                blend = (mask and MASK_BLEND) != 0
            )
        }
    }
}

@Suppress("NOTHING_TO_INLINE")
internal inline fun BytesBuffer.readEntityEffectFlags(): EntityEffectFlags {
    return EntityEffectFlags.fromByte(this.readByte())
}