/*
 * Copyright © 2026 RTAkland
 * Author: RTAkland
 * Date: 2026/9/4
 */


package cn.rtast.libmc.common

public fun <T : MinecraftPacket> _WriteChannel.sendPacket(packet: T, codec: PacketCodec<T>) {
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