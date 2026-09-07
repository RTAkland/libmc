/*
 * Copyright © 2026 RTAkland
 * Author: RTAkland
 * Date: 2026/9/3
 */


package cn.rtast.libmc.mcping.bedrock

import cn.rtast.libmc.LibMCContext
import cn.rtast.libmc.stream.UdpSocket
import cn.rtast.libmc.stream.wrap
import cn.rtast.libmc.mcping.PingResponse
import kotlin.time.Clock

internal suspend fun pingBedrockServer(host: String, port: Int, context: LibMCContext): PingResponse {
    val socket = UdpSocket(host, port, context)
    return try {
        val sendTime = Clock.System.now().toEpochMilliseconds()
        val requestPacket = BedrockRequestPacket(sendTime)
        val responseBytes = socket.sendPacket(requestPacket, BedrockRequestPacket)
        val receiveTime = Clock.System.now().toEpochMilliseconds()
        val responsePacket = BedrockResponsePacket.decode(responseBytes.wrap())
        val latency = (receiveTime - sendTime).toInt()
        PingResponse(responsePacket.payload, latency)
    } finally {
        socket.close()
    }
}