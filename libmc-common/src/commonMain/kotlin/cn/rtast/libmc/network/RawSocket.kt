/*
 * Copyright © 2026 RTAkland
 * Author: RTAkland
 * Date: 2026/9/3
 */


package cn.rtast.libmc.network

public interface RawSocket : AutoCloseable {
    /**
     * Open socket connection
     */
    public suspend fun connect()

    /**
     * An abstract function, used to open tcp socket read channel
     */
    public fun openReadChannel(): ReadChannel

    /**
     * An abstract function, used to open tcp socket write/send channel
     */
    public fun openWriteChannel(): WriteChannel

    /**
     * Close socket
     */
    override fun close()
}