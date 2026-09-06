/*
 * Copyright © 2026 RTAkland
 * Author: RTAkland
 * Date: 2026/9/6
 */


package cn.rtast.libmc.protocol.packet.play.clientbound

import cn.rtast.libmc.common.stream.BytesBuffer
import cn.rtast.libmc.common.packet.PacketCodec
import cn.rtast.libmc.common.packet.MinecraftPacket
import cn.rtast.libmc.common.primitives.readVarInt

public data class ClientboundSetCameraPacket(val cameraId: Int) : MinecraftPacket {
    internal companion object Codec : PacketCodec<ClientboundSetCameraPacket> {
        override suspend fun encode(buffer: BytesBuffer, value: ClientboundSetCameraPacket) {}
        override suspend fun decode(buffer: BytesBuffer): ClientboundSetCameraPacket {
            return ClientboundSetCameraPacket(buffer.readVarInt())
        }
    }
}