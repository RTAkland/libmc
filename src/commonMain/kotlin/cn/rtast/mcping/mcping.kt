/*
 * Copyright © 2026 RTAkland
 * Author: RTAkland
 * Date: 2026/9/3
 */


package cn.rtast.mcping

import cn.rtast.mcping.platform.PlatformSocket


public fun mcping(host: String, port: Int): String {
    val socket = PlatformSocket(host, port)
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
        if (packetId == StatusRequestPacket.packetId) receiveChannel.readMcString()
        else throw IllegalStateException("Server does not respond correct packet id, expected ${StatusRequestPacket.packetId} but got $packetId")
    } finally {
        socket.close()
    }
}