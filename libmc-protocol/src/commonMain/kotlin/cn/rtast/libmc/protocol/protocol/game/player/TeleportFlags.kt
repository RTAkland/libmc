/*
 * Copyright © 2026 RTAkland
 * Author: RTAkland
 * Date: 2026/9/6
 */


package cn.rtast.libmc.protocol.protocol.game.player

public data class TeleportFlags(
    val relativeX: Boolean = false,
    val relativeY: Boolean = false,
    val relativeZ: Boolean = false,
    val relativeYaw: Boolean = false,
    val relativePitch: Boolean = false,
    val relativeVelocityX: Boolean = false,
    val relativeVelocityY: Boolean = false,
    val relativeVelocityZ: Boolean = false,
    val rotateVelocity: Boolean = false,
) {
    public fun toInt(): Int {
        var mask = 0
        if (relativeX) mask = mask or MASK_X
        if (relativeY) mask = mask or MASK_Y
        if (relativeZ) mask = mask or MASK_Z
        if (relativeYaw) mask = mask or MASK_YAW
        if (relativePitch) mask = mask or MASK_PITCH
        if (relativeVelocityX) mask = mask or MASK_VEL_X
        if (relativeVelocityY) mask = mask or MASK_VEL_Y
        if (relativeVelocityZ) mask = mask or MASK_VEL_Z
        if (rotateVelocity) mask = mask or MASK_ROTATE_VEL
        return mask
    }

    public companion object {
        public const val MASK_X: Int = 0x0001
        public const val MASK_Y: Int = 0x0002
        public const val MASK_Z: Int = 0x0004
        public const val MASK_YAW: Int = 0x0008
        public const val MASK_PITCH: Int = 0x0010
        public const val MASK_VEL_X: Int = 0x0020
        public const val MASK_VEL_Y: Int = 0x0040
        public const val MASK_VEL_Z: Int = 0x0080
        public const val MASK_ROTATE_VEL: Int = 0x0100

        public fun fromInt(mask: Int): TeleportFlags {
            return TeleportFlags(
                relativeX = (mask and MASK_X) != 0,
                relativeY = (mask and MASK_Y) != 0,
                relativeZ = (mask and MASK_Z) != 0,
                relativeYaw = (mask and MASK_YAW) != 0,
                relativePitch = (mask and MASK_PITCH) != 0,
                relativeVelocityX = (mask and MASK_VEL_X) != 0,
                relativeVelocityY = (mask and MASK_VEL_Y) != 0,
                relativeVelocityZ = (mask and MASK_VEL_Z) != 0,
                rotateVelocity = (mask and MASK_ROTATE_VEL) != 0
            )
        }
    }
}