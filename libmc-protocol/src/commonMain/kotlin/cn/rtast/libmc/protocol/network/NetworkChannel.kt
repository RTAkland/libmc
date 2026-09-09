/*
 * Copyright © 2026 RTAkland
 * Author: RTAkland
 * Date: 2026/9/5
 */

package cn.rtast.libmc.protocol.network

import cn.rtast.libmc.crypto.ProtocolContext
import cn.rtast.libmc.network.BytesBuffer
import cn.rtast.libmc.network.wrap
import cn.rtast.libmc.packet.MinecraftPacket
import cn.rtast.libmc.packet.writeBuffer
import cn.rtast.libmc.primitives.readVarInt
import cn.rtast.libmc.primitives.writeVarInt
import cn.rtast.libmc.protocol.client.ClientStateMachine
import cn.rtast.libmc.protocol.client.PacketEventDispatcher
import cn.rtast.libmc.protocol.protocol.GamePacketsProtocolCodec.clientboundGameProtocols
import cn.rtast.libmc.protocol.protocol.GamePacketsProtocolCodec.serverboundGameProtocols
import cn.rtast.libmc.zlibCompress
import cn.rtast.libmc.zlibDecompress
import kotlin.concurrent.Volatile

public class NetworkChannel internal constructor(
    host: String,
    port: Int,
    private val stateMachine: ClientStateMachine,
    private val dispatcher: PacketEventDispatcher,
    protocolContext: ProtocolContext,
) {
    internal val session: NetworkSession = NetworkSession(host, port, protocolContext)

    @Volatile
    private var threshold = -1

    public suspend fun connect(): Unit = session.connect()
    public fun setCompression(threshold: Int): Unit = run { this.threshold = threshold }

    /**
     * This function receive all inbound packets, and dispatch as a packet event
     * See [PacketEventDispatcher.dispatchReceive],
     * Use [PacketEventDispatcher.onPacket] to get packet event
     */
    public suspend fun readNextPacket(): MinecraftPacket {
        val packetLength = session.readVarInt()
        val frameBuf = session.readBytes(packetLength).wrap()
        val payloadBuf = if (threshold < 0) frameBuf else {
            val dataLength = frameBuf.readVarInt()
            if (dataLength == 0) frameBuf else {
                val compressedBytes = frameBuf.toByteArray()
                compressedBytes.zlibDecompress(dataLength).wrap()
            }
        }
        val currentState = stateMachine.currentState
        val packetId = payloadBuf.readVarInt()
        val packet = clientboundGameProtocols.getRegistry(currentState).decodePacket(packetId, payloadBuf)
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
        serverboundGameProtocols.getRegistry(stateMachine.currentState).encodePacket(uncompressedBodyBuf, packet)
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