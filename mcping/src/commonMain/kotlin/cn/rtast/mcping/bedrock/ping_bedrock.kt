/*
 * Copyright © 2026 RTAkland
 * Author: RTAkland
 * Date: 2026/9/3
 */


package cn.rtast.mcping.bedrock

import cn.rtast.mcping.PingResponse
import cn.rtast.mcping.platform.PingContext
import cn.rtast.mcping.platform._UdpSocket
import cn.rtast.mcping.platform.wrap
import kotlin.time.Clock

internal fun pingBedrockServer(host: String, port: Int, context: PingContext): PingResponse {
    val socket = _UdpSocket(host, port, context)
    return try {
        val sendTime = Clock.System.now().toEpochMilliseconds()
        val packet = BedrockRequestPacket(sendTime)
        val responseBytes = socket.sendPacket(packet)
        val receiveTime = Clock.System.now().toEpochMilliseconds()
        val buf = responseBytes.wrap()
        buf.readByte()  // packet id
        buf.readLong() // time
        buf.readLong()  // server guid
        buf.readBytes(16)  // magic refer to `rakNetMagic`
        val payloadLength = buf.readShort()
        val payload = buf.readBytes(payloadLength.toInt())
        PingResponse(payload.decodeToString(), (receiveTime - sendTime).toInt())
    } finally {
        socket.close()
    }
}