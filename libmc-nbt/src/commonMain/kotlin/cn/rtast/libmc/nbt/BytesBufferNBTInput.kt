/*
 * Copyright © 2026 RTAkland
 * Author: RTAkland
 * Date: 2026/9/5
 */


package cn.rtast.libmc.nbt

import cn.rtast.libmc.stream.ByteOrder
import cn.rtast.libmc.stream.BytesBuffer

public class BytesBufferNBTInput(override val order: ByteOrder, private val buffer: BytesBuffer) : NBTInput {
    override suspend fun readByte(): Byte = buffer.readByte()
    override suspend fun readBytes(count: Int): ByteArray = buffer.readBytes(count)
}

public fun BytesBuffer.toNBTInput(order: ByteOrder = ByteOrder.BIG_ENDIAN): BytesBufferNBTInput =
    BytesBufferNBTInput(order, this)