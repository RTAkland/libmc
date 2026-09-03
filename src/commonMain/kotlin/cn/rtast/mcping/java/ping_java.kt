/*
 * Copyright © 2026 RTAkland
 * Author: RTAkland
 * Date: 2026/9/3
 */


package cn.rtast.mcping.java

import cn.rtast.mcping.PingResponse
import cn.rtast.mcping.platform.PingContext
import cn.rtast.mcping.platform.Socket
import kotlin.time.Clock

internal fun pingJavaServer(host: String, port: Int, context: PingContext): PingResponse {
    val socket = Socket(host, port, context)
    val receiveChannel = socket.openReadChannel()
    val sendChannel = socket.openWriteChannel()

    return try {
        val handshakePacket = HandshakePacket(
            protocolVersion = -1,
            serverAddress = host,
            serverPort = port.toUShort(),
            nextState = 1
        )
        sendChannel.sendPacket(handshakePacket)
        sendChannel.sendPacket(StatusRequestPacket)

        receiveChannel.readVarInt()  // consume a varint
        val packetId = receiveChannel.readVarInt()
        val jsonResponse = if (packetId == StatusRequestPacket.packetId) receiveChannel.readMcString()
        else throw IllegalStateException("Server does not respond correct packet id, expected ${StatusRequestPacket.packetId} but got $packetId")

        val sendTime = Clock.System.now().toEpochMilliseconds()
        val pingPacket = PingPacket(sendTime)
        sendChannel.sendPacket(pingPacket)
        receiveChannel.readVarInt()  // consume a varint
        receiveChannel.readVarInt()  // packet id
        receiveChannel.readLong() // pong packet payload
        PingResponse(jsonResponse, (Clock.System.now().toEpochMilliseconds() - sendTime).toInt())
    } finally {
        socket.close()
    }
}