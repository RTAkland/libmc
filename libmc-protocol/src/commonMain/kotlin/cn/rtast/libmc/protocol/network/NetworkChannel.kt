/*
 * Copyright © 2026 RTAkland
 * Author: RTAkland
 * Date: 2026/9/5
 */

package cn.rtast.libmc.protocol.network

import cn.rtast.libmc.network.BytesBuffer
import cn.rtast.libmc.network.wrap
import cn.rtast.libmc.packet.MinecraftPacket
import cn.rtast.libmc.packet.writeBuffer
import cn.rtast.libmc.primitives.readVarInt
import cn.rtast.libmc.primitives.writeVarInt
import cn.rtast.libmc.protocol.client.MinecraftClient
import cn.rtast.libmc.protocol.registry.GamePacketsProtocolRegistry.clientboundGameProtocols
import cn.rtast.libmc.protocol.registry.GamePacketsProtocolRegistry.serverboundGameProtocols
import cn.rtast.libmc.zlibCompress
import cn.rtast.libmc.zlibDecompress
import kotlin.concurrent.Volatile

public class NetworkChannel internal constructor(private val client: MinecraftClient) {
    internal val networkSession: NetworkSession = NetworkSession(client)

    @Volatile
    private var threshold = -1

    public fun connect(): Unit = networkSession.connect()
    public fun setCompression(threshold: Int): Unit = run { this.threshold = threshold }

    public fun readNextPacket(): MinecraftPacket {
        val packetLength = networkSession.readVarInt()
        return networkSession.readBytes(packetLength).wrap().use { frameBuf ->
            val isCompressed = threshold >= 0
            if (!isCompressed) decodeAndDispatch(frameBuf) else {
                val dataLength = frameBuf.readVarInt()
                if (dataLength == 0) decodeAndDispatch(frameBuf) else {
                    val compressedBytes = frameBuf.readBytes()
                    compressedBytes.zlibDecompress(dataLength).wrap().use { payloadBuf ->
                        decodeAndDispatch(payloadBuf)
                    }
                }
            }
        }
    }

    private fun decodeAndDispatch(buf: BytesBuffer): MinecraftPacket {
        val currentState = client.stateMachine.currentState
        val packetId = buf.readVarInt()
        val packet = clientboundGameProtocols.getRegistry(currentState).decodePacket(packetId, buf)
        client.dispatchReceive(packet, client.session)
        return packet
    }

    public fun sendPacket(packet: MinecraftPacket) {
        BytesBuffer().use { uncompressedBodyBuf ->
            serverboundGameProtocols.getRegistry(client.stateMachine.currentState)
                .encodePacket(uncompressedBodyBuf, packet)
            BytesBuffer().use { frameBuffer ->
                if (threshold < 0) {
                    frameBuffer.writeVarInt(uncompressedBodyBuf.size)
                    frameBuffer.writeBuffer(uncompressedBodyBuf)
                } else {
                    BytesBuffer().use { contentBuf ->
                        if (uncompressedBodyBuf.size < threshold) {
                            contentBuf.writeVarInt(0)
                            contentBuf.writeBuffer(uncompressedBodyBuf)
                        } else {
                            val compressedData = uncompressedBodyBuf.peek().zlibCompress()
                            contentBuf.writeVarInt(uncompressedBodyBuf.size)
                            contentBuf.writeBytes(compressedData)
                        }
                        frameBuffer.writeVarInt(contentBuf.size)
                        frameBuffer.writeBuffer(contentBuf)
                    }
                }
                networkSession.writeFully(frameBuffer.readBytes())
            }
        }
        client.dispatchSent(packet, client.session)
    }

    public fun close(): Unit = networkSession.close()
}