/*
 * Copyright © 2026 RTAkland
 * Author: RTAkland
 * Date: 2026/9/4
 */


package cn.rtast.libmc.protocol.packet.configuration.clientbound

import cn.rtast.libmc.common.stream.BytesBuffer
import cn.rtast.libmc.common.packet.MinecraftPacket
import cn.rtast.libmc.common.packet.PacketCodec
import cn.rtast.libmc.common.primitives.readVarInt
import cn.rtast.libmc.protocol.packet.configuration.KnownPacks

public data class ClientboundSelectKnownPacksPacket(val knownPacks: List<KnownPacks>) : MinecraftPacket {
    internal companion object Codec : PacketCodec<ClientboundSelectKnownPacksPacket> {
        override suspend fun encode(buffer: BytesBuffer, value: ClientboundSelectKnownPacksPacket) {}
        override suspend fun decode(buffer: BytesBuffer): ClientboundSelectKnownPacksPacket {
            val packsCount = buffer.readVarInt()
            val packs = List(packsCount) { KnownPacks.decode(buffer) }
            return ClientboundSelectKnownPacksPacket(packs)
        }
    }
}