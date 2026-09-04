/*
 * Copyright © 2026 RTAkland
 * Author: RTAkland
 * Date: 2026/9/3
 */


package cn.rtast.libmc.mcping.java

import cn.rtast.libmc.common.PacketCodec
import cn.rtast.libmc.common._Buffer
import cn.rtast.libmc.common._WriteChannel
import cn.rtast.libmc.common.write
import cn.rtast.libmc.common.writeBuffer


internal fun <T : MinecraftPacket> _WriteChannel.sendPacket(packet: T, codec: PacketCodec<T>) {
    val bodyBuffer = _Buffer()
    bodyBuffer.write(packet.packetId, VarIntCodec)
    codec.encode(bodyBuffer, packet)
    val frameBuffer = _Buffer()
    frameBuffer.write(bodyBuffer.size, VarIntCodec)
    frameBuffer.writeBuffer(bodyBuffer)
    val bytes = frameBuffer.toByteArray()
    this.writeFully(bytes, 0, bytes.size)
    this.flush()
}