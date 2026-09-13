/*
 * Copyright © 2026 RTAkland
 * Author: RTAkland
 * Date: 2026/9/12
 */

package cn.rtast.libmc.socket

import kotlin.math.min

public class ReadChannel(private val socket: NativeSocket, bufferSize: Int = 8192) {
    private val buffer = ByteArray(bufferSize)
    private var head = 0
    private var tail = 0
    public var transformer: DataTransformer? = null

    public fun readByte(): Byte {
        if (tail - head < 1) fillBuffer()
        return buffer[head++]
    }

    public fun readBytes(length: Int): ByteArray {
        val array = ByteArray(length)
        readFully(array, 0, length)
        return array
    }

    public fun readFully(out: ByteArray, start: Int = 0, end: Int = out.size) {
        var current = start
        while (current < end) {
            val bufferedAvailable = tail - head
            if (bufferedAvailable > 0) {
                val toCopy = min(bufferedAvailable, end - current)
                buffer.copyInto(out, current, head, head + toCopy)
                head += toCopy
                current += toCopy
            } else {
                val remainingNeeded = end - current
                if (remainingNeeded >= buffer.size) {
                    // 修复点 1：使用带有 offset 和 length 的 receive 重载，确保数据写入 out[current...] 处
                    val bytesRead = socket.receive(out, current, remainingNeeded)
                    if (bytesRead <= 0) error("Socket closed or EOF reached")
                    transformer?.transform(out, current, bytesRead)
                    current += bytesRead
                } else {
                    fillBuffer()
                }
            }
        }
    }

    private fun fillBuffer() {
        head = 0
        val bytesRead = socket.receive(buffer, 0, buffer.size)
        if (bytesRead <= 0) {
            tail = 0
            throw IllegalStateException("Socket closed or EOF reached")
        }
        tail = bytesRead
        transformer?.transform(buffer, 0, tail)
    }
}