# 前言

如果底层TCP Socket库是非挂起的, 那么**不要**将IO读写操作使用`withContext`包裹,
这会导致在网络IO中频繁的切换线程导致程序性能**下降**.

# 基于Netty的TCP Socket实现

```kotlin
dependencies {
    implementation("io.netty:netty-transport:4.2.17.Final")
    implementation("io.netty:netty-buffer:4.2.17.Final")
}
```

<details>
<summary>点击展开代码</summary>

```kotlin
import cn.rtast.libmc.network.RawSocket
import cn.rtast.libmc.network.ReadChannel
import cn.rtast.libmc.network.SocketEngine
import cn.rtast.libmc.network.WriteChannel
import io.netty.bootstrap.Bootstrap
import io.netty.buffer.ByteBuf
import io.netty.buffer.Unpooled
import io.netty.channel.*
import io.netty.channel.nio.NioIoHandler
import io.netty.channel.socket.SocketChannel
import io.netty.channel.socket.nio.NioSocketChannel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.withContext
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import io.netty.channel.Channel as NettyChannel
import kotlinx.coroutines.channels.Channel as KotlinChannel

public class NettyNetworkSocketEngine : SocketEngine {
    override fun create(host: String, port: Int): RawSocket = NettyNetworkSocket(host, port)

    private class NettyNetworkSocket(private val host: String, private val port: Int) : RawSocket {
        private val workerGroup: EventLoopGroup = MultiThreadIoEventLoopGroup(0, NioIoHandler.newFactory())
        private lateinit var channel: NettyChannel
        private val inboundQueue = KotlinChannel<ByteArray>(KotlinChannel.UNLIMITED)
        private lateinit var readChannel: NettyReadChannel
        private lateinit var writeChannel: NettyWriteChannel

        override suspend fun connect() {
            val bootstrap = Bootstrap()
                .group(workerGroup)
                .channel(NioSocketChannel::class.java)
                .option(ChannelOption.TCP_NODELAY, true)
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 10000)
                .handler(object : ChannelInitializer<SocketChannel>() {
                    override fun initChannel(ch: SocketChannel) {
                        ch.pipeline().addLast(object : ChannelInboundHandlerAdapter() {
                            override fun channelRead(ctx: ChannelHandlerContext, msg: Any) {
                                if (msg is ByteBuf) {
                                    try {
                                        val bytes = ByteArray(msg.readableBytes())
                                        msg.readBytes(bytes)
                                        inboundQueue.trySend(bytes)
                                    } finally {
                                        msg.release()
                                    }
                                }
                            }

                            override fun exceptionCaught(ctx: ChannelHandlerContext, cause: Throwable) {
                                inboundQueue.close(cause)
                                ctx.close()
                            }

                            override fun channelInactive(ctx: ChannelHandlerContext) {
                                inboundQueue.close()
                                super.channelInactive(ctx)
                            }
                        })
                    }
                })

            channel = withContext(Dispatchers.IO) {
                bootstrap.connect(host, port).suspendAwait()
            }

            readChannel = NettyReadChannel(inboundQueue)
            writeChannel = NettyWriteChannel(channel)
        }

        override fun openReadChannel(): ReadChannel = readChannel
        override fun openWriteChannel(): WriteChannel = writeChannel

        override fun close() {
            if (::channel.isInitialized && channel.isOpen) {
                channel.close()
            }
            inboundQueue.close()
            workerGroup.shutdownGracefully()
        }
    }

    private class NettyReadChannel(private val inboundQueue: KotlinChannel<ByteArray>) : ReadChannel {
        private var currentChunk: ByteArray? = null
        private var chunkOffset = 0

        private suspend fun fetchNextChunk() {
            val next = inboundQueue.receiveCatching().getOrNull()
                ?: throw IllegalStateException("Socket/Channel closed while reading")
            currentChunk = next
            chunkOffset = 0
        }

        override suspend fun readByte(): Byte {
            while (currentChunk == null || chunkOffset >= currentChunk!!.size) {
                fetchNextChunk()
            }
            val chunk = currentChunk!!
            return chunk[chunkOffset++]
        }

        override suspend fun readBytes(length: Int): ByteArray {
            val result = ByteArray(length)
            readFully(result, 0, length)
            return result
        }

        override suspend fun readFully(out: ByteArray, start: Int, end: Int) {
            var written = start

            while (written < end) {
                while (currentChunk == null || chunkOffset >= currentChunk!!.size) {
                    fetchNextChunk()
                }
                val chunk = currentChunk!!
                val available = chunk.size - chunkOffset
                val toCopy = minOf(available, end - written)

                chunk.copyInto(
                    out,
                    destinationOffset = written,
                    startIndex = chunkOffset,
                    endIndex = chunkOffset + toCopy
                )
                chunkOffset += toCopy
                written += toCopy
            }
        }
    }

    private class NettyWriteChannel(private val channel: NettyChannel) : WriteChannel {
        private val writeMutex = Mutex()

        override suspend fun writeFully(value: ByteArray, startIndex: Int, endIndex: Int) {
            val length = endIndex - startIndex
            if (length <= 0) return

            val nettyBuf = Unpooled.copiedBuffer(value, startIndex, length)

            writeMutex.withLock {
                withContext(Dispatchers.IO) {
                    channel.writeAndFlush(nettyBuf).suspendAwait()
                }
            }
        }

        override suspend fun flush() {
            writeMutex.withLock {
                withContext(Dispatchers.IO) {
                    channel.flush()
                }
            }
        }
    }

}

private suspend inline fun ChannelFuture.suspendAwait(): NettyChannel = suspendCancellableCoroutine { cont ->
    if (isDone) {
        if (isSuccess) cont.resume(channel())
        else cont.resumeWithException(cause() ?: RuntimeException("Netty operation failed"))
        return@suspendCancellableCoroutine
    }

    addListener { future ->
        if (future.isSuccess) {
            cont.resume(channel())
        } else {
            cont.resumeWithException(future.cause() ?: RuntimeException("Netty operation failed"))
        }
    }
    cont.invokeOnCancellation { cancel(false) }

}
```

> 请一并复制import部分的代码

</details>

# 基于ktor-network的TCP Socket实现

```kotlin
dependencies {
    implementation("io.ktor:ktor-network:3.5.2")
}
```

<details>
<summary>点击展开代码</summary>

```kotlin
public class KtorNetworkEngine : SocketEngine {
    override fun create(host: String, port: Int): RawSocket = KtorNetworkSocket(host, port)
}

public class KtorNetworkSocket(private val host: String, private val port: Int) : RawSocket {
    private val sm = SelectorManager(Dispatchers.IO)
    private lateinit var socket: Socket

    override suspend fun connect(): Unit = run { socket = aSocket(sm).tcp().connect(host, port) }
    override fun openReadChannel(): ReadChannel = KtorReadChannel(socket.openReadChannel())
    override fun openWriteChannel(): WriteChannel = KtorWriteChannel(socket.openWriteChannel())
    override fun close() {
        socket.close()
        sm.close()
    }
}

public class KtorReadChannel(private val readChannel: ByteReadChannel) : ReadChannel {
    override suspend fun readByte(): Byte = readChannel.readByte()
    override suspend fun readBytes(length: Int): ByteArray = readChannel.readByteArray(length)
    override suspend fun readFully(out: ByteArray, start: Int, end: Int): Unit =
        readChannel.readFully(out, start, end)
}

public class KtorWriteChannel(private val writeChannel: ByteWriteChannel) : WriteChannel {
    override suspend fun writeFully(value: ByteArray, startIndex: Int, endIndex: Int) {
        writeChannel.writeFully(value, startIndex, endIndex)
    }

    override suspend fun flush(): Unit = writeChannel.flush()
}
```

</details>

# 基于Java内置Socket的TCP Socket实现

<details>
<summary>点击展开代码</summary>

```kotlin
class JavaSocketEngine : SocketEngine {
    override fun create(host: String, port: Int): RawSocket = JavaNetworkSocket(host, port)
}

class JavaNetworkSocket(
    private val host: String,
    private val port: Int,
    private val connectTimeoutMs: Int = 10000,
) : RawSocket {
    private lateinit var socket: Socket
    private lateinit var readChannel: JavaReadChannel
    private lateinit var writeChannel: JavaWriteChannel

    override suspend fun connect() {
        val s = Socket()
        s.tcpNoDelay = true
        s.connect(InetSocketAddress(host, port), connectTimeoutMs)

        socket = s
        readChannel = JavaReadChannel(s.getInputStream())
        writeChannel = JavaWriteChannel(s.getOutputStream())
    }

    override fun openReadChannel(): ReadChannel = readChannel
    override fun openWriteChannel(): WriteChannel = writeChannel

    override fun close() {
        if (::socket.isInitialized && !socket.isClosed) {
            runCatching { socket.close() }
        }
    }
}

class JavaReadChannel(private val inputStream: InputStream) : ReadChannel {
    override suspend fun readByte(): Byte {
        val b = inputStream.read()
        if (b == -1) throw IllegalStateException("Socket stream reached EOF while reading byte")
        return b.toByte()
    }

    override suspend fun readBytes(length: Int): ByteArray {
        val buffer = ByteArray(length)
        readFullyInternal(buffer, 0, length)
        return buffer
    }

    override suspend fun readFully(out: ByteArray, start: Int, end: Int) {
        readFullyInternal(out, start, end - start)
    }

    private fun readFullyInternal(out: ByteArray, offset: Int, length: Int) {
        var bytesRead = 0
        while (bytesRead < length) {
            val count = inputStream.read(out, offset + bytesRead, length - bytesRead)
            if (count == -1) {
                throw IllegalStateException("Socket stream closed unexpectedly (read $bytesRead of $length bytes)")
            }
            bytesRead += count
        }
    }
}

class JavaWriteChannel(private val outputStream: OutputStream) : WriteChannel {
    override suspend fun writeFully(value: ByteArray, startIndex: Int, endIndex: Int) {
        outputStream.write(value, startIndex, endIndex - startIndex)
    }

    override suspend fun flush() {
        outputStream.flush()
    }
}
```
</details>