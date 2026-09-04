/*
 * Copyright © 2026 RTAkland
 * Author: RTAkland
 * Date: 2026/9/3
 */


package cn.rtast.libmc.mcping.java

import cn.rtast.libmc.common.*
import cn.rtast.libmc.mcping.PingResponse
import kotlin.time.Clock

internal fun pingJavaServer(host: String, port: Int, context: LibMCContext): PingResponse {
    val socket = _Socket(host, port, context)
    val receiveChannel = socket.openReadChannel()
    val sendChannel = socket.openWriteChannel()

    return try {
        val handshakePacket = HandshakePacket(
            protocolVersion = -1,
            serverAddress = host,
            serverPort = port.toUShort(),
            nextState = 1
        )
        sendChannel.sendPacket(handshakePacket, HandshakePacket)
        sendChannel.sendPacket(StatusRequestPacket, StatusRequestPacket)

        val statusFrameBuffer = receiveChannel.readPacketFrame()
        val statusPacketId = VarIntCodec.decode(statusFrameBuffer)
        if (statusPacketId != 0x00) {
            throw IllegalStateException("Expected StatusResponse packet ID 0x00, got $statusPacketId")
        }
        val jsonResponse = McStringCodec.decode(statusFrameBuffer)

        val sendTime = Clock.System.now().toEpochMilliseconds()
        val pingPacket = PingPacket(sendTime)
        sendChannel.sendPacket(pingPacket, PingPacket)

        val pongFrameBuffer = receiveChannel.readPacketFrame()
        val pongPacketId = VarIntCodec.decode(pongFrameBuffer)
        if (pongPacketId != 0x01) {
            throw IllegalStateException("Expected Pong packet ID 0x01, got $pongPacketId")
        }
        val latency = (Clock.System.now().toEpochMilliseconds() - sendTime).toInt()
        PingResponse(jsonResponse, latency)
    } finally {
        socket.close()
    }
}