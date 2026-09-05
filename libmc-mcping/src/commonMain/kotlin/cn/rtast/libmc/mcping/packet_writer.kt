/*
 * Copyright © 2026 RTAkland
 * Author: RTAkland
 * Date: 2026/9/5
 */


package cn.rtast.libmc.mcping

import cn.rtast.libmc.common.*
import cn.rtast.libmc.common.packet.MinecraftPacket

public fun <T : MinecraftPacket> WriteChannel.sendPacket(packet: T, packetId: Int, codec: PacketCodec<T>) {
    val bodyBuffer = BytesBuffer()
    bodyBuffer.write(packetId, VarIntCodec)
    codec.encode(bodyBuffer, packet)
    val frameBuffer = BytesBuffer()
    frameBuffer.write(bodyBuffer.size, VarIntCodec)
    frameBuffer.writeBuffer(bodyBuffer)
    val bytes = frameBuffer.toByteArray()
    this.writeFully(bytes, 0, bytes.size)
    this.flush()
}