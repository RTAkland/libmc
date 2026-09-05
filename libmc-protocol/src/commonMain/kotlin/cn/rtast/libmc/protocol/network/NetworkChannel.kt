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

internal class NetworkChannel(
    private val host: String,
    private val port: Int,
    private val context: LibMCContext,
    private val stateMachine: ClientStateMachine,
) {
    private var socket: Socket? = null
    private var readChannel: ReadChannel? = null
    private var writeChannel: WriteChannel? = null
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
        val payloadBuf = if (threshold < 0) rawFrameBytes.wrap() else {
            val frameBuf = rawFrameBytes.wrap()
            val dataLength = frameBuf.readVarInt()
            if (dataLength == 0) {
                frameBuf.readBytes(frameBuf.remaining.toInt()).wrap()
            } else frameBuf.readBytes(frameBuf.remaining.toInt()).zlibDecompress(dataLength).wrap()
        }

        val currentState = stateMachine.currentState
        val packetId = payloadBuf.readVarInt()
        return GameProtocols.clientboundGameProtocols.getRegistry(currentState).decodePacket(packetId, payloadBuf)
    }

    fun sendPacket(packet: MinecraftPacket) {
        val channel = requireNotNull(writeChannel) { "WriteChannel not connected" }
        val bodyBuffer = BytesBuffer()
        GameProtocols.serverboundGameProtocols.getRegistry(stateMachine.currentState).encodePacket(bodyBuffer, packet)
        val frameBuffer = BytesBuffer().apply {
            if (threshold < 0) {
                writeVarInt(bodyBuffer.size)
                writeBuffer(bodyBuffer)
            } else {
                val uncompressedData = bodyBuffer.toByteArray()
                if (uncompressedData.size < threshold) {
                    val contentBuf = BytesBuffer().apply {
                        writeVarInt(0)
                        writeBytes(uncompressedData)
                    }
                    writeVarInt(contentBuf.size)
                    writeBuffer(contentBuf)
                } else {
                    val compressedData = uncompressedData.zlibCompress()
                    val contentBuf = BytesBuffer().apply {
                        writeVarInt(uncompressedData.size)
                        writeBytes(compressedData)
                    }
                    writeVarInt(contentBuf.size)
                    writeBuffer(contentBuf)
                }
            }
        }
        channel.writeFully(frameBuffer.toByteArray())
        channel.flush()
    }

    fun close() {
        socket?.close()
    }
}