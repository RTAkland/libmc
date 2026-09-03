/*
 * Copyright © 2026 RTAkland
 * Author: RTAkland
 * Date: 2026/9/3
 */


package cn.rtast.mcping.bedrock

import cn.rtast.mcping.platform.PlatformBuffer
import cn.rtast.mcping.platform.UdpSocket

internal fun UdpSocket.sendPacket(packet: MinecraftBedrockPacket): ByteArray  {
    val buf = PlatformBuffer()
    packet.writePayload(buf)
    return sendAndReceive(buf.toByteArray())
}