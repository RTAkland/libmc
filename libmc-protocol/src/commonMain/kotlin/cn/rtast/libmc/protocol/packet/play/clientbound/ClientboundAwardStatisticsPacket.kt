/*
 * Copyright © 2026 RTAkland
 * Author: RTAkland
 * Date: 2026/9/5
 */


package cn.rtast.libmc.protocol.packet.play.clientbound

import cn.rtast.libmc.common.stream.BytesBuffer
import cn.rtast.libmc.common.packet.MinecraftPacket
import cn.rtast.libmc.common.packet.PacketCodec
import cn.rtast.libmc.common.primitives.readVarInt
import cn.rtast.libmc.protocol.protocol.game.StatisticsEntry

/**
 * ref: https://minecraft.wiki/w/Java_Edition_protocol/Packets#Award_Statistics
 */
public data class ClientboundAwardStatisticsPacket(val stats: List<StatisticsEntry>) : MinecraftPacket {
    internal companion object Codec : PacketCodec<ClientboundAwardStatisticsPacket> {
        override suspend fun encode(buffer: BytesBuffer, value: ClientboundAwardStatisticsPacket) {}
        override suspend fun decode(buffer: BytesBuffer): ClientboundAwardStatisticsPacket {
            val count = buffer.readVarInt()
            val stats = List(count) { StatisticsEntry.decode(buffer) }
            return ClientboundAwardStatisticsPacket(stats)
        }
    }
}