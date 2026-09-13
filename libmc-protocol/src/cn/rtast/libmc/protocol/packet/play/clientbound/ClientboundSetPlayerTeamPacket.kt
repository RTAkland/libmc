/*
 * Copyright © 2026 RTAkland
 * Author: RTAkland
 * Date: 2026/9/11
 */


package cn.rtast.libmc.protocol.packet.play.clientbound

import cn.rtast.libmc.network.BytesBuffer
import cn.rtast.libmc.packet.MinecraftPacket
import cn.rtast.libmc.packet.PacketCodec
import cn.rtast.libmc.primitives.readMcString
import cn.rtast.libmc.primitives.readPrefixed
import cn.rtast.libmc.primitives.readVarInt
import cn.rtast.libmc.protocol.protocol.game.chat.readTextComponent
import cn.rtast.libmc.protocol.protocol.game.team.CollisionRule
import cn.rtast.libmc.protocol.protocol.game.team.NameTagVisibility
import cn.rtast.libmc.protocol.protocol.game.team.TeamAction

public data class ClientboundSetPlayerTeamPacket(val teamName: String, val action: TeamAction) : MinecraftPacket {
    internal companion object Codec : PacketCodec<ClientboundSetPlayerTeamPacket> {
        override fun encode(buffer: BytesBuffer, value: ClientboundSetPlayerTeamPacket) {}
        override fun decode(buffer: BytesBuffer): ClientboundSetPlayerTeamPacket {
            val teamName = buffer.readMcString()
            val action = when (val teamAction = buffer.readByte().toInt()) {
                0 -> {
                    val info = buffer.decodeTeamInfo()
                    val entities = buffer.readPrefixed { readMcString() }
                    TeamAction.CreateTeam(info, entities)
                }

                1 -> TeamAction.RemoveTeam
                2 -> TeamAction.UpdateTeamInfo(buffer.decodeTeamInfo())
                3 -> TeamAction.AddEntities(buffer.readPrefixed { readMcString() })
                4 -> TeamAction.RemoveEntities(buffer.readPrefixed { readMcString() })
                else -> error("Unknown team action method id $teamAction")
            }
            return ClientboundSetPlayerTeamPacket(teamName, action)
        }

        private fun BytesBuffer.decodeTeamInfo(): TeamAction.TeamInfo {
            val displayName = readTextComponent()
            val prefix = readTextComponent()
            val suffix = readTextComponent()
            val nameTagVisibility = NameTagVisibility.fromID(readVarInt())
            val collisionRule = CollisionRule.fromID(readVarInt())
            val teamColor = readVarInt()
            val friendlyFlags = readByte()
            return TeamAction.TeamInfo(
                displayName, prefix, suffix,
                nameTagVisibility, collisionRule,
                teamColor, friendlyFlags
            )
        }
    }
}