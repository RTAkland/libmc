/*
 * Copyright © 2026 RTAkland
 * Author: RTAkland
 * Date: 2026/9/7
 */


package cn.rtast.libmc.protocol.packet.play.clientbound

import cn.rtast.libmc.packet.MinecraftPacket
import cn.rtast.libmc.packet.PacketCodec
import cn.rtast.libmc.primitives.*
import cn.rtast.libmc.protocol.protocol.game.Identifier
import cn.rtast.libmc.protocol.protocol.game.readIdentifier
import cn.rtast.libmc.protocol.protocol.game.world.waypoint.WaypointColor
import cn.rtast.libmc.protocol.protocol.game.world.waypoint.WaypointData
import cn.rtast.libmc.protocol.protocol.game.world.waypoint.WaypointOperation
import cn.rtast.libmc.network.BytesBuffer
import kotlin.uuid.Uuid

public data class ClientboundWaypointPacket(
    val operation: WaypointOperation,
    val identifier: Either<Uuid, Identifier>,
    val iconStyle: Identifier,
    val waypointColor: WaypointColor?,
    val waypointData: WaypointData,
) : MinecraftPacket {
    internal companion object Codec : PacketCodec<ClientboundWaypointPacket> {
        override fun encode(buffer: BytesBuffer, value: ClientboundWaypointPacket) {}
        override fun decode(buffer: BytesBuffer): ClientboundWaypointPacket {
            val operation = WaypointOperation.fromID(buffer.readVarInt())
            val identifier = buffer.readEither(
                readLeft = { readUuid() },
                readRight = { readIdentifier() }
            )
            val iconStyle = buffer.readIdentifier()
            val color = buffer.readOptional {
                val red = readUByte()
                val green = readUByte()
                val blue = readUByte()
                WaypointColor(red, green, blue)
            }
            val data = when (WaypointData.Type.fromID(buffer.readVarInt())) {
                WaypointData.Type.EMPTY -> WaypointData.Empty
                WaypointData.Type.VEC3I -> {
                    val x = buffer.readVarInt()
                    val y = buffer.readVarInt()
                    val z = buffer.readVarInt()
                    WaypointData.Vec3i(x, y, z)
                }

                WaypointData.Type.CHUNK -> {
                    val x = buffer.readVarInt()
                    val z = buffer.readVarInt()
                    WaypointData.Chunk(x, z)
                }

                WaypointData.Type.AZIMUTH -> WaypointData.Azimuth(buffer.readFloat())
            }
            return ClientboundWaypointPacket(operation, identifier, iconStyle, color, data)
        }
    }
}