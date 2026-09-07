/*
 * Copyright © 2026 RTAkland
 * Author: RTAkland
 * Date: 2026/9/5
 */

package cn.rtast.libmc.protocol.network

import cn.rtast.libmc.LibMCContext
import cn.rtast.libmc.crypto.NetworkCipher
import cn.rtast.libmc.packet.MinecraftPacket
import cn.rtast.libmc.packet.writeBuffer
import cn.rtast.libmc.primitives.readVarInt
import cn.rtast.libmc.primitives.writeVarInt
import cn.rtast.libmc.protocol.client.ClientStateMachine
import cn.rtast.libmc.protocol.event.PacketEventDispatcher
import cn.rtast.libmc.protocol.protocol.GameProtocols
import cn.rtast.libmc.stream.BytesBuffer
import cn.rtast.libmc.stream.wrap
import cn.rtast.libmc.zlibCompress
import cn.rtast.libmc.zlibDecompress
import kotlin.concurrent.Volatile

public class NetworkChannel internal constructor(
    host: String,
    port: Int,
    context: LibMCContext,
    private val stateMachine: ClientStateMachine,
    cipherProvider: (ByteArray) -> NetworkCipher,
    private val dispatcher: PacketEventDispatcher,
) {
    internal val session: NetworkSession = NetworkSession(host, port, context, cipherProvider)

    @Volatile
    private var threshold = -1

    public fun connect(): Unit = session.connect()
    public fun setCompression(threshold: Int): Unit = run { this.threshold = threshold }

    /**
     * This function receive all inbound packets, and dispatch as a packet event
     * See [PacketEventDispatcher.dispatchReceive],
     * Use [PacketEventDispatcher.onPacket] to get packet event
     */
    public suspend fun readNextPacket(): MinecraftPacket {
        val packetLength = session.readVarInt()
        val rawFrameBytes = session.readBytes(packetLength)
        val frameBuf = rawFrameBytes.wrap()
        val payloadBuf = if (threshold < 0) frameBuf else {
            val dataLength = frameBuf.readVarInt()
            val remainingBytes = frameBuf.readBytes(frameBuf.remaining.toInt())
            if (dataLength == 0) remainingBytes.wrap() else remainingBytes.zlibDecompress(dataLength).wrap()
        }
        val currentState = stateMachine.currentState
        val packetId = payloadBuf.readVarInt()
        val packet = GameProtocols.clientboundGameProtocols
            .getRegistry(currentState)
            .decodePacket(packetId, payloadBuf)
        dispatcher.dispatchReceive(packet)
        return packet
    }

    /**
     * This function will dispatch a packet event when packet was sent.
     * See [PacketEventDispatcher.dispatchSent],
     * Use [PacketEventDispatcher.onSent] to get packet event
     */
    public suspend fun sendPacket(packet: MinecraftPacket) {
        val uncompressedBodyBuf = BytesBuffer()
        GameProtocols.serverboundGameProtocols
            .getRegistry(stateMachine.currentState)
            .encodePacket(uncompressedBodyBuf, packet)
        val uncompressedData = uncompressedBodyBuf.toByteArray()
        val frameBuffer = BytesBuffer()
        if (threshold < 0) {
            frameBuffer.writeVarInt(uncompressedData.size)
            frameBuffer.writeBytes(uncompressedData)
        } else {
            val contentBuf = BytesBuffer()
            if (uncompressedData.size < threshold) {
                contentBuf.writeVarInt(0)
                contentBuf.writeBytes(uncompressedData)
            } else {
                val compressedData = uncompressedData.zlibCompress()
                contentBuf.writeVarInt(uncompressedData.size)
                contentBuf.writeBytes(compressedData)
            }
            frameBuffer.writeVarInt(contentBuf.size)
            frameBuffer.writeBuffer(contentBuf)
        }
        session.writeFully(frameBuffer.toByteArray())
        dispatcher.dispatchSent(packet)
    }

    public fun close(): Unit = session.close()
}