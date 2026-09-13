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
import cn.rtast.libmc.protocol.protocol.event.PacketEventDispatcher
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

    /**
     * This function receive all inbound packets, and dispatch as a packet event
     * See [PacketEventDispatcher.dispatchReceive],
     * Use [PacketEventDispatcher.onPacket] to get packet event
     */
    public fun readNextPacket(): MinecraftPacket {
        val packetLength = networkSession.readVarInt()
        val frameBuf = networkSession.readBytes(packetLength).wrap()
        val payloadBuf = if (threshold < 0) frameBuf else {
            val dataLength = frameBuf.readVarInt()
            if (dataLength == 0) frameBuf else {
                val compressedBytes = frameBuf.toByteArray()
                compressedBytes.zlibDecompress(dataLength).wrap()
            }
        }
        val currentState = client.stateMachine.currentState
        val packetId = payloadBuf.readVarInt()
        val packet = clientboundGameProtocols.getRegistry(currentState).decodePacket(packetId, payloadBuf)
        client.dispatchReceive(packet, client.session)
        return packet
    }

    /**
     * This function will dispatch a packet event when packet was sent.
     * See [PacketEventDispatcher.dispatchSent],
     * Use [PacketEventDispatcher.onSent] to get packet event
     */
    public fun sendPacket(packet: MinecraftPacket) {
        val uncompressedBodyBuf = BytesBuffer()
        serverboundGameProtocols.getRegistry(client.stateMachine.currentState).encodePacket(uncompressedBodyBuf, packet)
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
        networkSession.writeFully(frameBuffer.toByteArray())
        client.dispatchSent(packet, client.session)
    }

    public fun close(): Unit = networkSession.close()
}