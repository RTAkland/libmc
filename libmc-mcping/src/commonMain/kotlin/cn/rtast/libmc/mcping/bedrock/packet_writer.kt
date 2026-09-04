/*
 * Copyright © 2026 RTAkland
 * Author: RTAkland
 * Date: 2026/9/3
 */


package cn.rtast.libmc.mcping.bedrock

import cn.rtast.libmc.common.PacketCodec
import cn.rtast.libmc.common._Buffer
import cn.rtast.libmc.common._UdpSocket


internal fun <T : MinecraftBedrockPacket> _UdpSocket.sendPacket(packet: T, codec: PacketCodec<T>): ByteArray {
    val buf = _Buffer()
    codec.encode(buf, packet)
    return sendAndReceive(buf.toByteArray())
}