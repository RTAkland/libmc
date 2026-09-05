/*
 * Copyright © 2026 RTAkland
 * Author: RTAkland
 * Date: 2026/9/5
 */

package cn.rtast.libmc.protocol.network

import cn.rtast.libmc.common.*
import cn.rtast.libmc.common.packet.MinecraftPacket
import cn.rtast.libmc.protocol.client.ClientStateMachine
import cn.rtast.libmc.protocol.protocol.GameProtocols
import kotlin.concurrent.Volatile

internal class NetworkChannel(
    private val host: String,
    private val port: Int,
    private val context: LibMCContext,
    private val stateMachine: ClientStateMachine,
) {
    private var socket: Socket? = null
    private var readChannel: ReadChannel? = null
    private var writeChannel: WriteChannel? = null

    @Volatile
    private var threshold = -1

    fun connect() {
        val sk = Socket(host, port, context)
        this.socket = sk
        this.readChannel = sk.openReadChannel()
        this.writeChannel = sk.openWriteChannel()
    }

    fun setCompression(threshold: Int) {
        this.threshold = threshold
    }

    fun readNextPacket(): MinecraftPacket {
        val channel = requireNotNull(readChannel) { "ReadChannel not connected" }
        val packetLength = channel.readVarInt()
        val rawFrameBytes = channel.readBytes(packetLength)
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
        return packet
    }

    fun sendPacket(packet: MinecraftPacket) {
        val channel = requireNotNull(writeChannel) { "WriteChannel not connected" }
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
        channel.writeFully(frameBuffer.toByteArray())
        channel.flush()
    }

    fun close() {
        socket?.close()
    }
}