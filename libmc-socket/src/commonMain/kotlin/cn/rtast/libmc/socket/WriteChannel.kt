/*
 * Copyright © 2026 RTAkland
 * Author: RTAkland
 * Date: 2026/9/12
 */

package cn.rtast.libmc.socket

public class WriteChannel(private val socket: NativeSocket, bufferSize: Int = 8192) {
    private val buffer = ByteArray(bufferSize)
    private var position = 0

    public var transformer: DataTransformer? = null

    public fun writeFully(value: ByteArray, startIndex: Int = 0, endIndex: Int = value.size) {
        var current = startIndex
        while (current < endIndex) {
            val availableCapacity = buffer.size - position
            val toCopy = minOf(endIndex - current, availableCapacity)
            value.copyInto(buffer, position, current, current + toCopy)
            position += toCopy
            current += toCopy
            if (position == buffer.size) flushInternal()
        }
    }

    public fun flush() {
        if (position > 0) flushInternal()
    }

    private fun flushInternal() {
        transformer?.transform(buffer, 0, position)
        var current = 0
        while (current < position) {
            val chunk = buffer.copyOfRange(current, position)
            val written = socket.send(chunk)
            if (written <= 0) error("Failed to write to socket, connection closed")
            current += written
        }
        position = 0
    }
}