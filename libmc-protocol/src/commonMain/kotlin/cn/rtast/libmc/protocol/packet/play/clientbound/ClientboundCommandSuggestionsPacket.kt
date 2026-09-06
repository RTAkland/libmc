/*
 * Copyright © 2026 RTAkland
 * Author: RTAkland
 * Date: 2026/9/6
 */


package cn.rtast.libmc.protocol.packet.play.clientbound

import cn.rtast.libmc.common.packet.MinecraftPacket
import cn.rtast.libmc.common.packet.PacketCodec
import cn.rtast.libmc.common.primitives.readMcString
import cn.rtast.libmc.common.primitives.readVarInt
import cn.rtast.libmc.common.primitives.writeMcString
import cn.rtast.libmc.common.stream.BytesBuffer
import cn.rtast.libmc.nbt.NBTCompound
import cn.rtast.libmc.protocol.protocol.util.readNetworkNBTCompound
import cn.rtast.libmc.protocol.protocol.util.writeNetworkNBTCompound

public data class ClientboundCommandSuggestionsPacket(
    val id: Int,
    val start: Int,
    val length: Int,
    val matches: List<CommandSuggestionMatch>,
) : MinecraftPacket {
    public data class CommandSuggestionMatch(val match: String, val tooltip: NBTCompound?) {
        internal companion object Codec : PacketCodec<CommandSuggestionMatch> {
            override suspend fun encode(buffer: BytesBuffer, value: CommandSuggestionMatch) {
                buffer.writeMcString(value.match)
                buffer.writeBoolean(value.tooltip != null)
                if (value.tooltip != null) buffer.writeNetworkNBTCompound(value.tooltip)
            }

            override suspend fun decode(buffer: BytesBuffer): CommandSuggestionMatch {
                val match = buffer.readMcString()
                val hasTooltip = buffer.readBoolean()
                val tooltip = if (hasTooltip) buffer.readNetworkNBTCompound() else null
                return CommandSuggestionMatch(match, tooltip)
            }
        }
    }

    internal companion object Codec : PacketCodec<ClientboundCommandSuggestionsPacket> {
        override suspend fun encode(buffer: BytesBuffer, value: ClientboundCommandSuggestionsPacket) {}
        override suspend fun decode(buffer: BytesBuffer): ClientboundCommandSuggestionsPacket {
            val id = buffer.readVarInt()
            val start = buffer.readVarInt()
            val length = buffer.readVarInt()
            val matchCount = buffer.readVarInt()
            val matches = ArrayList<CommandSuggestionMatch>(matchCount)
            repeat(matchCount) { matches.add(CommandSuggestionMatch.decode(buffer)) }
            return ClientboundCommandSuggestionsPacket(id, start, length, matches)
        }
    }
}