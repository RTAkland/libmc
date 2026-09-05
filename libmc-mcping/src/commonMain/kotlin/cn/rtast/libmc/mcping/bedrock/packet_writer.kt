/*
 * Copyright © 2026 RTAkland
 * Author: RTAkland
 * Date: 2026/9/3
 */


package cn.rtast.libmc.mcping.bedrock

import cn.rtast.libmc.common.PacketCodec
import cn.rtast.libmc.common.BytesBuffer
import cn.rtast.libmc.common.UdpSocket


internal fun <T : MinecraftBedrockPacket> UdpSocket.sendPacket(packet: T, codec: PacketCodec<T>): ByteArray {
    val buf = BytesBuffer()
    codec.encode(buf, packet)
    return sendAndReceive(buf.toByteArray())
}