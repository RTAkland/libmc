/*
 * Copyright © 2026 RTAkland
 * Author: RTAkland
 * Date: 2026/9/5
 */


package cn.rtast.libmc.protocol.protocol.game

import cn.rtast.libmc.common.BytesBuffer
import cn.rtast.libmc.common.PacketCodec
import cn.rtast.libmc.common.readVarInt
import cn.rtast.libmc.common.writeVarInt
import kotlinx.serialization.Serializable

/**
 * ref: https://minecraft.wiki/w/Java_Edition_protocol/Packets#Award_Statistics
 */
@Serializable
public data class StatisticsEntry(val categoryId: Int, val statisticId: Int, val value: Int) {
    public companion object Codec : PacketCodec<StatisticsEntry> {
        override fun encode(buffer: BytesBuffer, value: StatisticsEntry) {
            buffer.writeVarInt(value.categoryId)
            buffer.writeVarInt(value.statisticId)
            buffer.writeVarInt(value.value)
        }

        override fun decode(buffer: BytesBuffer): StatisticsEntry {
            val categoryId = buffer.readVarInt()
            val statisticId = buffer.readVarInt()
            val value = buffer.readVarInt()
            return StatisticsEntry(categoryId, statisticId, value)
        }
    }
}