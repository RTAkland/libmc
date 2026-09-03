/*
 * Copyright © 2026 RTAkland
 * Author: RTAkland
 * Date: 2026/9/3
 */


package cn.rtast.mcping.bedrock

import cn.rtast.mcping.platform._Buffer
import cn.rtast.mcping.platform._UdpSocket

internal fun _UdpSocket.sendPacket(packet: MinecraftBedrockPacket): ByteArray  {
    val buf = _Buffer()
    packet.writePayload(buf)
    return sendAndReceive(buf.toByteArray())
}