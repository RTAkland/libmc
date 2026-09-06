/*
 * Copyright © 2026 RTAkland
 * Author: RTAkland
 * Date: 2026/9/6
 */


package cn.rtast.libmc.protocol.packet.configuration.clientbound

import cn.rtast.libmc.common.BytesBuffer
import cn.rtast.libmc.common.PacketCodec
import cn.rtast.libmc.common.readVarInt
import cn.rtast.libmc.protocol.protocol.game.Identifier
import cn.rtast.libmc.protocol.protocol.game.readIdentifier
import cn.rtast.libmc.protocol.protocol.game.registry.RegistryEntry

public data class ClientboundRegistryDataPacket(
    /**
     * Name of the registry, such as minecraft:dimension_type.
     */
    val registryId: Identifier,
    val entries: List<RegistryEntry>,
) : ClientboundConfigurationPacket {
    internal companion object Codec : PacketCodec<ClientboundRegistryDataPacket> {
        override fun encode(buffer: BytesBuffer, value: ClientboundRegistryDataPacket) {}
        override fun decode(buffer: BytesBuffer): ClientboundRegistryDataPacket {
            val id = buffer.readIdentifier()
            val entryCount = buffer.readVarInt()
            val entries = ArrayList<RegistryEntry>(entryCount)
            (0 until entryCount).forEach { _ -> entries.add(RegistryEntry.decode(buffer)) }
            return ClientboundRegistryDataPacket(id, entries)
        }
    }
}