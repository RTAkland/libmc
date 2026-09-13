/*
 * Copyright © 2026 RTAkland
 * Author: RTAkland
 * Date: 2026/9/5
 */


package cn.rtast.libmc.protocol.protocol.game.player

public data class PlayerAbilities(
    public val isInvulnerable: Boolean = false,
    public val isFlying: Boolean = false,
    public val allowFlying: Boolean = false,
    public val isCreativeInstantBreak: Boolean = false,
) {
    public fun mask(): Byte {
        var mask = 0
        if (isInvulnerable) mask = mask or MASK_INVULNERABLE
        if (isFlying) mask = mask or MASK_FLYING
        if (allowFlying) mask = mask or MASK_ALLOW_FLYING
        if (isCreativeInstantBreak) mask = mask or MASK_CREATIVE_INSTANT_BREAK
        return mask.toByte()
    }

    public companion object {
        public const val MASK_INVULNERABLE: Int = 0x01
        public const val MASK_FLYING: Int = 0x02
        public const val MASK_ALLOW_FLYING: Int = 0x04
        public const val MASK_CREATIVE_INSTANT_BREAK: Int = 0x08

        public fun fromByte(flags: Byte): PlayerAbilities {
            val mask = flags.toInt()
            return PlayerAbilities(
                isInvulnerable = (mask and MASK_INVULNERABLE) != 0,
                isFlying = (mask and MASK_FLYING) != 0,
                allowFlying = (mask and MASK_ALLOW_FLYING) != 0,
                isCreativeInstantBreak = (mask and MASK_CREATIVE_INSTANT_BREAK) != 0
            )
        }
    }
}