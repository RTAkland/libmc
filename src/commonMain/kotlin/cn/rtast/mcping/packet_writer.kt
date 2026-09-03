/*
 * Copyright © 2026 RTAkland
 * Author: RTAkland
 * Date: 2026/9/3
 */


package cn.rtast.mcping

import cn.rtast.mcping.platform.PlatformBuffer
import cn.rtast.mcping.platform.PlatformWriteChannel

internal fun PlatformWriteChannel.sendPacket(packet: MinecraftPacket) {
    val bodyBuffer = PlatformBuffer()
    bodyBuffer.writeVarInt(packet.packetId)
    packet.writePayload(bodyBuffer)
    val frameBuffer = PlatformBuffer()
    frameBuffer.writeVarInt(bodyBuffer.size)
    frameBuffer.writeBytes(bodyBuffer.toByteArray())
    val bytes = frameBuffer.toByteArray()
    this.writeFully(bytes, 0, bytes.size)
    this.flush()
}