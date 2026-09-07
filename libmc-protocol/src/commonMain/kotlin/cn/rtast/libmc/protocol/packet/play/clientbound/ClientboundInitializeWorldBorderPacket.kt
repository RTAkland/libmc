/*
 * Copyright © 2026 RTAkland
 * Author: RTAkland
 * Date: 2026/9/6
 */


package cn.rtast.libmc.protocol.packet.play.clientbound

import cn.rtast.libmc.stream.BytesBuffer
import cn.rtast.libmc.packet.PacketCodec
import cn.rtast.libmc.packet.MinecraftPacket
import cn.rtast.libmc.primitives.readVarInt
import cn.rtast.libmc.primitives.readVarLong

public data class ClientboundInitializeWorldBorderPacket(
    val centerX: Double,
    val centerZ: Double,
    val oldDiameter: Double,
    val newDiameter: Double,
    val speed: Long,
    val portalTeleportBoundary: Int,
    val warningBlocks: Int,
    val warningTime: Int,
) : MinecraftPacket {
    internal companion object Codec : PacketCodec<ClientboundInitializeWorldBorderPacket> {
        override suspend fun encode(buffer: BytesBuffer, value: ClientboundInitializeWorldBorderPacket) {}
        override suspend fun decode(buffer: BytesBuffer): ClientboundInitializeWorldBorderPacket {
            val centerX = buffer.readDouble()
            val centerZ = buffer.readDouble()
            val oldDiameter = buffer.readDouble()
            val newDiameter = buffer.readDouble()
            val speed = buffer.readVarLong()
            val portalTeleportBoundary = buffer.readVarInt()
            val warningBlocks = buffer.readVarInt()
            val warningTime = buffer.readVarInt()
            return ClientboundInitializeWorldBorderPacket(
                centerX, centerZ, oldDiameter,
                newDiameter, speed,
                portalTeleportBoundary,
                warningBlocks, warningTime
            )
        }
    }
}