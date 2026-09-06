/*
 * Copyright © 2026 RTAkland
 * Author: RTAkland
 * Date: 2026/9/4
 */


package cn.rtast.libmc.protocol.protocol.game.block

import cn.rtast.libmc.common.BytesBuffer
import cn.rtast.libmc.common.PacketCodec

/**
 * An integer/block position: x (-33 554 432 to 33 554 431), z (-33 554 432 to 33 554 431), y (-2048 to 2047)
 * ref: https://minecraft.wiki/w/Java_Edition_protocol/Packets#Type:Position
 */
public data class BlockPos(val x: Int, val y: Int, val z: Int) {
    public companion object : PacketCodec<BlockPos> {
        private const val PACKED_X_MASK = 0x3FFFFFFL // 26 bits
        private const val PACKED_Y_MASK = 0xFFFL      // 12 bits
        private const val PACKED_Z_MASK = 0x3FFFFFFL // 26 bits

        override fun decode(buffer: BytesBuffer): BlockPos {
            val packed = buffer.readLong()
            val x = (packed shr 38).toInt()
            val y = (packed shl 52 shr 52).toInt()
            val z = (packed shl 26 shr 38).toInt()
            return BlockPos(x, y, z)
        }

        override fun encode(buffer: BytesBuffer, value: BlockPos) {
            val xLong = (value.x.toLong() and PACKED_X_MASK)
            val yLong = (value.y.toLong() and PACKED_Y_MASK)
            val zLong = (value.z.toLong() and PACKED_Z_MASK)
            buffer.writeLong(xLong shl 38 or (zLong shl 12) or yLong)
        }
    }
}

internal fun BytesBuffer.readBlockPos(): BlockPos = BlockPos.decode(this)
internal fun BytesBuffer.writeBlockPos(pos: BlockPos) = BlockPos.encode(this, pos)