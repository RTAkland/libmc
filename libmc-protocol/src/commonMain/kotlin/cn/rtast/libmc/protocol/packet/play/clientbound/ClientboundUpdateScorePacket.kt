/*
 * Copyright © 2026 RTAkland
 * Author: RTAkland
 * Date: 2026/9/6
 */


package cn.rtast.libmc.protocol.packet.play.clientbound

import cn.rtast.libmc.common.packet.MinecraftPacket
import cn.rtast.libmc.common.packet.PacketCodec
import cn.rtast.libmc.common.primitives.readMcString
import cn.rtast.libmc.common.primitives.readOptional
import cn.rtast.libmc.common.primitives.readVarInt
import cn.rtast.libmc.common.stream.BytesBuffer
import cn.rtast.libmc.nbt.NBTCompound
import cn.rtast.libmc.nbt.NBTTag
import cn.rtast.libmc.protocol.protocol.game.scoreboard.ScoreNumberFormat
import cn.rtast.libmc.protocol.protocol.util.readNetworkNBTCompound

public data class ClientboundUpdateScorePacket(
    val entityName: String,
    val objectiveName: String,
    val value: Int,
    val displayName: NBTCompound?,
    val numberFormat: ScoreNumberFormat?,
) : MinecraftPacket {
    internal companion object Codec : PacketCodec<ClientboundUpdateScorePacket> {
        override suspend fun encode(buffer: BytesBuffer, value: ClientboundUpdateScorePacket) {}
        override suspend fun decode(buffer: BytesBuffer): ClientboundUpdateScorePacket {
            val entityName = buffer.readMcString()
            val objectiveName = buffer.readMcString()
            val value = buffer.readVarInt()
            val displayName = buffer.readOptional { readNetworkNBTCompound() }
            val numberFormat = buffer.readOptional {
                when (val type = readVarInt()) {
                    0 -> ScoreNumberFormat.Blank
                    1 -> ScoreNumberFormat.Styled(styling = readNetworkNBTCompound().element as NBTTag.CompoundTag)  // fix me
                    2 -> ScoreNumberFormat.Fixed(content = readNetworkNBTCompound())
                    else -> error("Unknown ScoreNumberFormat type: $type")
                }
            }
            return ClientboundUpdateScorePacket(entityName, objectiveName, value, displayName, numberFormat)
        }
    }
}