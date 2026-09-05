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

    fun connect() {
        val sk = Socket(host, port, context)
        this.socket = sk
        this.readChannel = sk.openReadChannel()
        this.writeChannel = sk.openWriteChannel()
    }

    fun readNextPacket(): MinecraftPacket {
        val channel = requireNotNull(readChannel) { "ReadChannel not connected" }
        val length = channel.readVarInt()
        val buf = channel.readBytes(length).wrap()
        val currentState = stateMachine.currentState
        val packetId = buf.readVarInt()
        return GameProtocols.clientboundGameProtocols.getRegistry(currentState).decodePacket(packetId, buf)
    }

    fun sendPacket(packet: MinecraftPacket) {
        val channel = requireNotNull(writeChannel) { "WriteChannel not connected" }
        val bodyBuffer = BytesBuffer()
        GameProtocols.serverboundGameProtocols.getRegistry(stateMachine.currentState).encodePacket(bodyBuffer, packet)
        val frameBuffer = BytesBuffer().apply {
            writeVarInt(bodyBuffer.size)
            writeBuffer(bodyBuffer)
        }
        channel.writeFully(frameBuffer.toByteArray())
        channel.flush()
    }

    fun close() {
        socket?.close()
    }
}