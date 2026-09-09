/*
 * Copyright © 2026 RTAkland
 * Author: RTAkland
 * Date: 2026/9/9
 */


package cn.rtast.libmc.protocol.protocol.game.color

import cn.rtast.libmc.network.BytesBuffer
import kotlin.jvm.JvmInline

@JvmInline
public value class Color24(public val rawValue: Int) {
    public val rgb: Int get() = rawValue and 0xFFFFFF
    public val red: Int get() = (rgb shr 16) and 0xFF
    public val green: Int get() = (rgb shr 8) and 0xFF
    public val blue: Int get() = rgb and 0xFF

    public val redFloat: Float get() = red / 255.0f
    public val greenFloat: Float get() = green / 255.0f
    public val blueFloat: Float get() = blue / 255.0f

    override fun toString(): String = "Color24(r=$red, g=$green, b=$blue, hex=0x${rgb.toString(16).padStart(6, '0')})"

    public companion object {
        public val BLACK: Color24 = Color24(0x000000)
        public val WHITE: Color24 = Color24(0xFFFFFF)
        public val RED: Color24 = Color24(0xFF0000)
        public val GREEN: Color24 = Color24(0x00FF00)
        public val BLUE: Color24 = Color24(0x0000FF)

        public fun fromRGB(red: Int, green: Int, blue: Int): Color24 {
            val r = red.coerceIn(0, 255)
            val g = green.coerceIn(0, 255)
            val b = blue.coerceIn(0, 255)
            return Color24((r shl 16) or (g shl 8) or b)
        }

        public fun fromRGBFloat(red: Float, green: Float, blue: Float): Color24 {
            return fromRGB((red * 255.0f).toInt(), (green * 255.0f).toInt(), (blue * 255.0f).toInt())
        }
    }
}

internal fun BytesBuffer.readColor24(): Color24 = Color24(readInt())
internal fun BytesBuffer.writeColor24(color: Color24) = writeInt(color.rawValue)