/*
 * Copyright © 2026 RTAkland
 * Author: RTAkland
 * Date: 2026/9/3
 */


package cn.rtast.mcping.java

import cn.rtast.mcping.platform._Buffer
import cn.rtast.mcping.platform._WriteChannel

internal fun _WriteChannel.sendPacket(packet: MinecraftPacket) {
    val bodyBuffer = _Buffer()
    bodyBuffer.writeVarInt(packet.packetId)
    packet.writePayload(bodyBuffer)
    val frameBuffer = _Buffer()
    frameBuffer.writeVarInt(bodyBuffer.size)
    frameBuffer.writeBytes(bodyBuffer.toByteArray())
    val bytes = frameBuffer.toByteArray()
    this.writeFully(bytes, 0, bytes.size)
    this.flush()
}