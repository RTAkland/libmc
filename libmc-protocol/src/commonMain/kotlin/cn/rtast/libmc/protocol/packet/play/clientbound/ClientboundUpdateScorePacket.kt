/*
 * Copyright © 2026 RTAkland
 * Author: RTAkland
 * Date: 2026/9/6
 */


package cn.rtast.libmc.protocol.packet.play.clientbound

import cn.rtast.libmc.nbt.NBTTag
import cn.rtast.libmc.packet.MinecraftPacket
import cn.rtast.libmc.packet.PacketCodec
import cn.rtast.libmc.primitives.readMcString
import cn.rtast.libmc.primitives.readOptional
import cn.rtast.libmc.primitives.readVarInt
import cn.rtast.libmc.protocol.protocol.game.chat.TextComponent
import cn.rtast.libmc.protocol.protocol.game.chat.readTextComponent
import cn.rtast.libmc.protocol.protocol.game.scoreboard.ScoreNumberFormat
import cn.rtast.libmc.protocol.protocol.util.readNetworkNBTCompound
import cn.rtast.libmc.stream.BytesBuffer

public data class ClientboundUpdateScorePacket(
    val entityName: String,
    val objectiveName: String,
    val value: Int,
    val displayName: TextComponent?,
    val numberFormat: ScoreNumberFormat?,
) : MinecraftPacket {
    internal companion object Codec : PacketCodec<ClientboundUpdateScorePacket> {
        override suspend fun encode(buffer: BytesBuffer, value: ClientboundUpdateScorePacket) {}
        override suspend fun decode(buffer: BytesBuffer): ClientboundUpdateScorePacket {
            val entityName = buffer.readMcString()
            val objectiveName = buffer.readMcString()
            val value = buffer.readVarInt()
            val displayName = buffer.readOptional { readTextComponent() }
            val numberFormat = buffer.readOptional {
                when (val type = readVarInt()) {
                    0 -> ScoreNumberFormat.Blank
                    1 -> ScoreNumberFormat.Styled(styling = readNetworkNBTCompound().element as NBTTag.CompoundTag)  // fix me
                    2 -> ScoreNumberFormat.Fixed(content = readTextComponent())
                    else -> error("Unknown ScoreNumberFormat type: $type")
                }
            }
            return ClientboundUpdateScorePacket(entityName, objectiveName, value, displayName, numberFormat)
        }
    }
}