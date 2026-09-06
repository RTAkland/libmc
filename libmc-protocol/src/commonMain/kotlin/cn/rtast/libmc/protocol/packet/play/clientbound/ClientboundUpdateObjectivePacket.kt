/*
 * Copyright © 2026 RTAkland
 * Author: RTAkland
 * Date: 2026/9/6
 */


package cn.rtast.libmc.protocol.packet.play.clientbound

import cn.rtast.libmc.common.BytesBuffer
import cn.rtast.libmc.common.PacketCodec
import cn.rtast.libmc.common.packet.MinecraftPacket
import cn.rtast.libmc.common.readMcString
import cn.rtast.libmc.common.readVarInt
import cn.rtast.libmc.nbt.NBTTag
import cn.rtast.libmc.protocol.protocol.game.scoreboard.ObjectivePayload
import cn.rtast.libmc.protocol.protocol.game.scoreboard.ObjectiveRenderType
import cn.rtast.libmc.protocol.protocol.game.scoreboard.ScoreNumberFormat
import cn.rtast.libmc.protocol.protocol.util.readNetworkNBTCompound

public data class ClientboundUpdateObjectivePacket(
    val objectiveName: String,
    val mode: Byte,
    val payload: ObjectivePayload?,
) : MinecraftPacket {
    internal companion object Codec : PacketCodec<ClientboundUpdateObjectivePacket> {
        override fun encode(buffer: BytesBuffer, value: ClientboundUpdateObjectivePacket) {}
        override fun decode(buffer: BytesBuffer): ClientboundUpdateObjectivePacket {
            val objectiveName = buffer.readMcString()
            val mode = buffer.readByte()
            val payload = when (mode.toInt()) {
                1 -> ObjectivePayload.Remove
                0, 2 -> {
                    val displayName = buffer.readNetworkNBTCompound()
                    val renderType = ObjectiveRenderType.fromID(buffer.readVarInt())
                    val hasNumberFormat = buffer.readBoolean()
                    val numberFormat = if (hasNumberFormat) {
                        when (val type = buffer.readVarInt()) {
                            0 -> ScoreNumberFormat.Blank
                            1 -> ScoreNumberFormat.Styled(styling = buffer.readNetworkNBTCompound().element as NBTTag.CompoundTag)  // fix me
                            2 -> ScoreNumberFormat.Fixed(content = buffer.readNetworkNBTCompound())  // ?
                            else -> error("Unknown ScoreNumberFormat type: $type")
                        }
                    } else null
                    ObjectivePayload.Upsert(
                        displayName = displayName,
                        renderType = renderType,
                        defaultNumberFormat = numberFormat
                    )
                }

                else -> null
            }
            return ClientboundUpdateObjectivePacket(objectiveName, mode, payload)
        }
    }
}