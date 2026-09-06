/*
 * Copyright © 2026 RTAkland
 * Author: RTAkland
 * Date: 2026/9/6
 */


package cn.rtast.libmc.protocol.packet.play.serverbound

import cn.rtast.libmc.common.BytesBuffer
import cn.rtast.libmc.common.PacketCodec
import cn.rtast.libmc.common.packet.MinecraftPacket
import cn.rtast.libmc.common.writeMcString
import cn.rtast.libmc.protocol.protocol.game.block.BlockPos
import cn.rtast.libmc.protocol.protocol.game.block.writeBlockPos

public data class ServerboundSignUpdatePacket(
    val location: BlockPos,
    /**
     * Whether the updated text is in front or on the back of the sign
     */
    val isFrontText: Boolean,
    val line1: String,
    val line2: String,
    val line3: String,
    val line4: String,
) : MinecraftPacket {
    internal companion object Codec : PacketCodec<ServerboundSignUpdatePacket> {
        private const val MAX_LINE_LENGTH = 384

        override fun encode(buffer: BytesBuffer, value: ServerboundSignUpdatePacket) {
            require(value.line1.length <= MAX_LINE_LENGTH) { "Line 1 exceeds maximum length of $MAX_LINE_LENGTH" }
            require(value.line2.length <= MAX_LINE_LENGTH) { "Line 2 exceeds maximum length of $MAX_LINE_LENGTH" }
            require(value.line3.length <= MAX_LINE_LENGTH) { "Line 3 exceeds maximum length of $MAX_LINE_LENGTH" }
            require(value.line4.length <= MAX_LINE_LENGTH) { "Line 4 exceeds maximum length of $MAX_LINE_LENGTH" }
            buffer.writeBlockPos(value.location)
            buffer.writeBoolean(value.isFrontText)
            buffer.writeMcString(value.line1)
            buffer.writeMcString(value.line2)
            buffer.writeMcString(value.line3)
            buffer.writeMcString(value.line4)
        }

        override fun decode(buffer: BytesBuffer): ServerboundSignUpdatePacket =
            throw UnsupportedOperationException()
    }
}