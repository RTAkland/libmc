/*
 * Copyright © 2026 RTAkland
 * Author: RTAkland
 * Date: 2026/9/7
 */


package cn.rtast.libmc.protocol.packet.play.clientbound

import cn.rtast.libmc.packet.MinecraftPacket
import cn.rtast.libmc.packet.PacketCodec
import cn.rtast.libmc.primitives.readOptional
import cn.rtast.libmc.primitives.readPrefixed
import cn.rtast.libmc.primitives.readPrefixedByteArray
import cn.rtast.libmc.primitives.readVarInt
import cn.rtast.libmc.protocol.protocol.game.chat.readTextComponent
import cn.rtast.libmc.protocol.protocol.game.item.MapItemColorPatch
import cn.rtast.libmc.protocol.protocol.game.item.MapItemIcon
import cn.rtast.libmc.protocol.protocol.game.item.MapItemIconType
import cn.rtast.libmc.stream.BytesBuffer

public data class ClientboundMapItemDataPacket(
    val mapId: Int,
    val scale: Byte,
    val locked: Boolean,
    val icons: List<MapItemIcon>?,
    val colorPatch: MapItemColorPatch?,
) : MinecraftPacket {
    internal companion object Codec : PacketCodec<ClientboundMapItemDataPacket> {
        override suspend fun encode(buffer: BytesBuffer, value: ClientboundMapItemDataPacket) {}
        override suspend fun decode(buffer: BytesBuffer): ClientboundMapItemDataPacket {
            val mapId = buffer.readVarInt()
            val scale = buffer.readByte()
            val locked = buffer.readBoolean()
            val icons = buffer.readOptional {
                readPrefixed {
                    val type = MapItemIconType.fromID(readVarInt())
                    val x = readByte()
                    val z = readByte()
                    val direction = readByte()
                    val displayName = readOptional { readTextComponent() }
                    MapItemIcon(type, x, z, direction, displayName)
                }
            }
            val columns = buffer.readUByte()
            val colorPatch = if (columns > 0u) {
                val rows = buffer.readUByte()
                val xOffset = buffer.readUByte()
                val zOffset = buffer.readUByte()
                val data = buffer.readPrefixedByteArray()
                MapItemColorPatch(columns, rows, xOffset, zOffset, data)
            } else null
            return ClientboundMapItemDataPacket(mapId, scale, locked, icons, colorPatch)
        }
    }
}