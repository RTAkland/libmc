/*
 * Copyright © 2026 RTAkland
 * Author: RTAkland
 * Date: 2026/9/6
 */


package cn.rtast.libmc.protocol.protocol.game.registry

import cn.rtast.libmc.stream.BytesBuffer
import cn.rtast.libmc.packet.PacketCodec
import cn.rtast.libmc.nbt.NBTCompound
import cn.rtast.libmc.protocol.protocol.game.Identifier
import cn.rtast.libmc.protocol.protocol.game.readIdentifier
import cn.rtast.libmc.protocol.protocol.game.writeIdentifier
import cn.rtast.libmc.protocol.protocol.util.readNetworkNBTCompound
import cn.rtast.libmc.protocol.protocol.util.writeNetworkNBTCompound

public data class RegistryEntry(
    /**
     * Name of the entry, such as minecraft:overworld.
     */
    val id: Identifier,
    /**
     * Entry data. If omitted, sourced from the selected known packs.
     */
    val data: NBTCompound?,
) {
    internal companion object Codec : PacketCodec<RegistryEntry> {
        override suspend fun encode(buffer: BytesBuffer, value: RegistryEntry) {
            buffer.writeIdentifier(value.id)
            buffer.writeBoolean(value.data != null)
            if (value.data != null) buffer.writeNetworkNBTCompound(value.data)
        }

        override suspend fun decode(buffer: BytesBuffer): RegistryEntry {
            val id = buffer.readIdentifier()
            val hasData = buffer.readBoolean()
            val data = if (hasData) buffer.readNetworkNBTCompound() else null
            return RegistryEntry(id, data)
        }
    }
}