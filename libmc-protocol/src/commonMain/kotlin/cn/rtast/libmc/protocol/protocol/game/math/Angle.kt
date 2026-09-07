/*
 * Copyright © 2026 RTAkland
 * Author: RTAkland
 * Date: 2026/9/5
 */


package cn.rtast.libmc.protocol.protocol.game.math

import cn.rtast.libmc.stream.BytesBuffer
import kotlin.jvm.JvmInline

/**
 * ref: https://minecraft.wiki/w/Java_Edition_protocol/Packets#Type:Angle
 */
@JvmInline
public value class Angle(public val raw: Byte) {
    /**
     * convert the Angle(byte) to 360-degree float angles
     */
    public fun toDegrees(): Float = (raw.toInt() and 0xFF) * 360.0f / 256.0f

    /**
     * encode 360-degress float angles into Angle(byte)
     */
    public fun toAngleByte(): Byte = ((raw % 360.0f) * 256.0f / 360.0f).toInt().toByte()
}


internal suspend fun BytesBuffer.readAngle(): Angle = Angle(this.readByte())
internal suspend fun BytesBuffer.writeAngle(value: Angle) = this.writeByte(value.raw)