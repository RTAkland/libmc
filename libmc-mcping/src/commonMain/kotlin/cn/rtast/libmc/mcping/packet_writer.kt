/*
 * Copyright © 2026 RTAkland
 * Author: RTAkland
 * Date: 2026/9/5
 */


package cn.rtast.libmc.mcping

import cn.rtast.libmc.common.stream.BytesBuffer
import cn.rtast.libmc.common.primitives.VarIntCodec
import cn.rtast.libmc.common.stream.WriteChannel
import cn.rtast.libmc.common.packet.MinecraftPacket
import cn.rtast.libmc.common.packet.PacketCodec
import cn.rtast.libmc.common.packet.writeBuffer

public suspend fun <T : MinecraftPacket> WriteChannel.sendPacket(packet: T, packetId: Int, codec: PacketCodec<T>) {
    val bodyBuffer = BytesBuffer()
    VarIntCodec.encode(bodyBuffer, packetId)
    codec.encode(bodyBuffer, packet)
    val frameBuffer = BytesBuffer()
    VarIntCodec.encode(bodyBuffer, bodyBuffer.size)
    frameBuffer.writeBuffer(bodyBuffer)
    val bytes = frameBuffer.toByteArray()
    this.writeFully(bytes, 0, bytes.size)
    this.flush()
}