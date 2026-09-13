/*
 * Copyright © 2026 RTAkland
 * Author: RTAkland
 * Date: 2026/9/6
 */


package cn.rtast.libmc.protocol.packet.play.clientbound

import cn.rtast.libmc.network.BytesBuffer
import cn.rtast.libmc.packet.PacketCodec
import cn.rtast.libmc.packet.MinecraftPacket
import cn.rtast.libmc.primitives.readVarInt

public data class ClientboundSetCameraPacket(val cameraId: Int) : MinecraftPacket {
    internal companion object Codec : PacketCodec<ClientboundSetCameraPacket> {
        override fun encode(buffer: BytesBuffer, value: ClientboundSetCameraPacket) {}
        override fun decode(buffer: BytesBuffer): ClientboundSetCameraPacket {
            return ClientboundSetCameraPacket(buffer.readVarInt())
        }
    }
}