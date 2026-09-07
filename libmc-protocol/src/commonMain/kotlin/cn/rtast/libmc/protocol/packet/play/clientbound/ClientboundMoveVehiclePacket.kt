/*
 * Copyright © 2026 RTAkland
 * Author: RTAkland
 * Date: 2026/9/6
 */


package cn.rtast.libmc.protocol.packet.play.clientbound

import cn.rtast.libmc.network.BytesBuffer
import cn.rtast.libmc.packet.PacketCodec
import cn.rtast.libmc.packet.MinecraftPacket
import cn.rtast.libmc.protocol.protocol.game.math.Vec3d
import cn.rtast.libmc.protocol.protocol.game.math.readVec3d

public data class ClientboundMoveVehiclePacket(val position: Vec3d, val yaw: Float, val pitch: Float) :
    MinecraftPacket {
    internal companion object Codec : PacketCodec<ClientboundMoveVehiclePacket> {
        override fun encode(buffer: BytesBuffer, value: ClientboundMoveVehiclePacket) {}
        override fun decode(buffer: BytesBuffer): ClientboundMoveVehiclePacket {
            val position = buffer.readVec3d()!!
            val yaw = buffer.readFloat()
            val pitch = buffer.readFloat()
            return ClientboundMoveVehiclePacket(position, yaw, pitch)
        }
    }
}