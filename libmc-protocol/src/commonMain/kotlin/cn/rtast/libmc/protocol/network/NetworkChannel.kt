/*
 * Copyright © 2026 RTAkland
 * Author: RTAkland
 * Date: 2026/9/5
 */

package cn.rtast.libmc.protocol.network

import cn.rtast.libmc.common.*
import cn.rtast.libmc.common.crypto.NetworkCipher
import cn.rtast.libmc.common.packet.MinecraftPacket
import cn.rtast.libmc.common.packet.writeBuffer
import cn.rtast.libmc.common.primitives.readVarInt
import cn.rtast.libmc.common.primitives.writeVarInt
import cn.rtast.libmc.common.stream.BytesBuffer
import cn.rtast.libmc.common.stream.wrap
import cn.rtast.libmc.protocol.client.ClientStateMachine
import cn.rtast.libmc.protocol.protocol.GameProtocols
import kotlin.concurrent.Volatile

internal class NetworkChannel(
    host: String,
    port: Int,
    context: LibMCContext,
    private val stateMachine: ClientStateMachine,
    cipherProvider: (ByteArray) -> NetworkCipher,
) {
    val session: NetworkSession = NetworkSession(host, port, context, cipherProvider)

    @Volatile
    private var threshold = -1

    fun connect() {
        session.connect()
    }

    fun setCompression(threshold: Int) {
        this.threshold = threshold
    }

    suspend fun readNextPacket(): MinecraftPacket {
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
        return GameProtocols.clientboundGameProtocols
            .getRegistry(currentState)
            .decodePacket(packetId, payloadBuf)
    }

    suspend fun sendPacket(packet: MinecraftPacket) {
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
    }

    fun close() {
        session.close()
    }
}