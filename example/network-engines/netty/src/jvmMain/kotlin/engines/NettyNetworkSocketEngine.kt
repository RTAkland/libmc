/*
 * Copyright © 2026 RTAkland
 * Author: RTAkland
 * Date: 2026/9/8
 */

package engines

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