/*
 * Copyright © 2026 RTAkland
 * Author: RTAkland
 * Date: 2026/9/6
 */


package cn.rtast.libmc.protocol.packet.play.clientbound

import cn.rtast.libmc.network.BytesBuffer
import cn.rtast.libmc.packet.MinecraftPacket
import cn.rtast.libmc.packet.PacketCodec

public data class ClientboundPlayerRotationPacket(
    val yaw: Float,
    val relativeYaw: Boolean,
    val pitch: Float,
    val relativePitch: Boolean,
) : MinecraftPacket {
    internal companion object Codec : PacketCodec<ClientboundPlayerRotationPacket> {
        override fun encode(buffer: BytesBuffer, value: ClientboundPlayerRotationPacket) {}
        override fun decode(buffer: BytesBuffer): ClientboundPlayerRotationPacket {
            val yaw = buffer.readFloat()
            val relativeYaw = buffer.readBoolean()
            val pitch = buffer.readFloat()
            val relativePitch = buffer.readBoolean()
            return ClientboundPlayerRotationPacket(yaw, relativeYaw, pitch, relativePitch)
        }
    }
}