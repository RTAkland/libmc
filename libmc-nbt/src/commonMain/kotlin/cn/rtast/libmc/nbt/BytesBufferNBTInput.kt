/*
 * Copyright © 2026 RTAkland
 * Author: RTAkland
 * Date: 2026/9/5
 */


package cn.rtast.libmc.nbt

import cn.rtast.libmc.common.ByteOrder
import cn.rtast.libmc.common.BytesBuffer

public class BytesBufferNBTInput(override val order: ByteOrder, private val buffer: BytesBuffer) : NBTInput {
    override fun readByte(): Byte = buffer.readByte()
    override fun readBytes(count: Int): ByteArray = buffer.readBytes(count)
}

public fun BytesBuffer.toNBTInput(order: ByteOrder = ByteOrder.BIG_ENDIAN): BytesBufferNBTInput =
    BytesBufferNBTInput(order, this)