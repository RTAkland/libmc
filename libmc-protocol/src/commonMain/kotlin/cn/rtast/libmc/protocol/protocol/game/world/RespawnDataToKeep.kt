/*
 * Copyright © 2026 RTAkland
 * Author: RTAkland
 * Date: 2026/9/6
 */

package cn.rtast.libmc.protocol.protocol.game.world

public data class RespawnDataToKeep(
    val keepAttributes: Boolean = false,
    val keepMetadata: Boolean = false
) {
    public fun toByte(): Byte {
        var mask = 0
        if (keepAttributes) mask = mask or MASK_KEEP_ATTRIBUTES
        if (keepMetadata) mask = mask or MASK_KEEP_METADATA
        return mask.toByte()
    }

    public companion object {
        public const val MASK_KEEP_ATTRIBUTES: Int = 0x01
        public const val MASK_KEEP_METADATA: Int = 0x02

        public fun fromByte(flags: Byte): RespawnDataToKeep {
            val mask = flags.toInt()
            return RespawnDataToKeep(
                keepAttributes = (mask and MASK_KEEP_ATTRIBUTES) != 0,
                keepMetadata = (mask and MASK_KEEP_METADATA) != 0
            )
        }
    }
}