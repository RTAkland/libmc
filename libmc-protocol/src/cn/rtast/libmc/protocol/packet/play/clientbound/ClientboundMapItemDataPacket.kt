/*
 * Copyright © 2026 RTAkland
 * Author: RTAkland
 * Date: 2026/9/7
 */


package cn.rtast.libmc.protocol.packet.play.clientbound

import cn.rtast.libmc.network.BytesBuffer
import cn.rtast.libmc.packet.MinecraftPacket
import cn.rtast.libmc.packet.PacketCodec
import cn.rtast.libmc.primitives.readOptional
import cn.rtast.libmc.primitives.readPrefixOptional
import cn.rtast.libmc.primitives.readPrefixed
import cn.rtast.libmc.primitives.readPrefixedByteArray
import cn.rtast.libmc.primitives.readVarInt
import cn.rtast.libmc.protocol.protocol.game.chat.readTextComponent
import cn.rtast.libmc.protocol.protocol.game.item.MapItemColorPatch
import cn.rtast.libmc.protocol.protocol.game.item.MapItemIcon
import cn.rtast.libmc.protocol.protocol.game.item.MapItemIconType

public data class ClientboundMapItemDataPacket(
    val mapId: Int,
    val scale: Byte,
    val locked: Boolean,
    val icons: List<MapItemIcon>?,
    val colorPatch: MapItemColorPatch?,
) : MinecraftPacket {
    internal companion object Codec : PacketCodec<ClientboundMapItemDataPacket> {
        override fun encode(buffer: BytesBuffer, value: ClientboundMapItemDataPacket) {}
        override fun decode(buffer: BytesBuffer): ClientboundMapItemDataPacket {
            val mapId = buffer.readVarInt()
            val scale = buffer.readByte()
            val locked = buffer.readBoolean()
            val icons = buffer.readPrefixOptional {
                readPrefixed {
                    val type = MapItemIconType.fromID(readVarInt())
                    val x = readByte()
                    val z = readByte()
                    val direction = readByte()
                    val displayName = readPrefixOptional { readTextComponent() }
                    MapItemIcon(type, x, z, direction, displayName)
                }
            }
            val columns = buffer.readUByte()
            val colorPatch = buffer.readOptional(columns > 0u) {
                val rows = buffer.readUByte()
                val xOffset = buffer.readUByte()
                val zOffset = buffer.readUByte()
                val data = buffer.readPrefixedByteArray()
                MapItemColorPatch(columns, rows, xOffset, zOffset, data)
            }
            return ClientboundMapItemDataPacket(mapId, scale, locked, icons, colorPatch)
        }
    }
}