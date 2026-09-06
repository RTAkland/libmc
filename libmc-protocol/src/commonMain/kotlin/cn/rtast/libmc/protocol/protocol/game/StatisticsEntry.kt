/*
 * Copyright © 2026 RTAkland
 * Author: RTAkland
 * Date: 2026/9/5
 */


package cn.rtast.libmc.protocol.protocol.game

import cn.rtast.libmc.common.stream.BytesBuffer
import cn.rtast.libmc.common.packet.PacketCodec
import cn.rtast.libmc.common.primitives.readVarInt
import cn.rtast.libmc.common.primitives.writeVarInt

/**
 * ref: https://minecraft.wiki/w/Java_Edition_protocol/Packets#Award_Statistics
 */
public data class StatisticsEntry(val categoryId: Int, val statisticId: Int, val value: Int) {
    internal companion object Codec : PacketCodec<StatisticsEntry> {
        override suspend fun encode(buffer: BytesBuffer, value: StatisticsEntry) {
            buffer.writeVarInt(value.categoryId)
            buffer.writeVarInt(value.statisticId)
            buffer.writeVarInt(value.value)
        }

        override suspend fun decode(buffer: BytesBuffer): StatisticsEntry {
            val categoryId = buffer.readVarInt()
            val statisticId = buffer.readVarInt()
            val value = buffer.readVarInt()
            return StatisticsEntry(categoryId, statisticId, value)
        }
    }
}