/*
 * Copyright © 2026 RTAkland
 * Author: RTAkland
 * Date: 2026/9/6
 */


package cn.rtast.libmc.protocol.packet.configuration.clientbound

import cn.rtast.libmc.stream.BytesBuffer
import cn.rtast.libmc.packet.MinecraftPacket
import cn.rtast.libmc.packet.PacketCodec
import cn.rtast.libmc.primitives.readVarInt
import cn.rtast.libmc.protocol.protocol.game.Identifier
import cn.rtast.libmc.protocol.protocol.game.readIdentifier

public data class ClientboundUpdateEnabledFeaturesPacket(val features: List<Identifier>) : MinecraftPacket {
    internal companion object Codec : PacketCodec<ClientboundUpdateEnabledFeaturesPacket> {
        override suspend fun encode(buffer: BytesBuffer, value: ClientboundUpdateEnabledFeaturesPacket) {}
        override suspend fun decode(buffer: BytesBuffer): ClientboundUpdateEnabledFeaturesPacket {
            val featureCount = buffer.readVarInt()
            val features = ArrayList<Identifier>(featureCount)
            repeat(featureCount) { features.add(buffer.readIdentifier()) }
            return ClientboundUpdateEnabledFeaturesPacket(features)
        }
    }
}