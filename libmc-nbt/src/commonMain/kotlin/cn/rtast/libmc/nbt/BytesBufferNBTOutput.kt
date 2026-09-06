/*
 * Copyright © 2026 RTAkland
 * Author: RTAkland
 * Date: 2026/9/5
 */


package cn.rtast.libmc.nbt

import cn.rtast.libmc.common.stream.ByteOrder
import cn.rtast.libmc.common.stream.BytesBuffer

public class BytesBufferNBTOutput(
    override val order: ByteOrder,
    override val root: NBTTag.CompoundTag,
    private val buffer: BytesBuffer,
) : NBTOutput {
    override suspend fun writeByte(value: Byte): Unit = buffer.writeByte(value)
    override suspend fun writeBytes(value: ByteArray): Unit = buffer.writeBytes(value)
    override suspend fun toByteArray(): ByteArray = buffer.toByteArray()
}

public fun BytesBuffer.toNBTOutput(
    root: NBTTag.CompoundTag,
    order: ByteOrder = ByteOrder.BIG_ENDIAN,
): BytesBufferNBTOutput =
    BytesBufferNBTOutput(order, root, this)