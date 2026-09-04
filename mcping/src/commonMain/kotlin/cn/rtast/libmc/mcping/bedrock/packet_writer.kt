/*
 * Copyright © 2026 RTAkland
 * Author: RTAkland
 * Date: 2026/9/3
 */


package cn.rtast.libmc.mcping.bedrock

import cn.rtast.libmc.common._Buffer
import cn.rtast.libmc.common._UdpSocket


internal fun _UdpSocket.sendPacket(packet: MinecraftBedrockPacket): ByteArray  {
    val buf = _Buffer()
    packet.writePayload(buf)
    return sendAndReceive(buf.toByteArray())
}