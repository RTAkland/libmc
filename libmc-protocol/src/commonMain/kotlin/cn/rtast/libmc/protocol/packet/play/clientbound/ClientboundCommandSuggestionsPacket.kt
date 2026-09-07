/*
 * Copyright © 2026 RTAkland
 * Author: RTAkland
 * Date: 2026/9/6
 */


package cn.rtast.libmc.protocol.packet.play.clientbound

import cn.rtast.libmc.packet.MinecraftPacket
import cn.rtast.libmc.packet.PacketCodec
import cn.rtast.libmc.primitives.readMcString
import cn.rtast.libmc.primitives.readVarInt
import cn.rtast.libmc.protocol.protocol.game.chat.TextComponent
import cn.rtast.libmc.protocol.protocol.game.chat.readTextComponent
import cn.rtast.libmc.network.BytesBuffer

public data class ClientboundCommandSuggestionsPacket(
    val id: Int,
    val start: Int,
    val length: Int,
    val matches: List<CommandSuggestionMatch>,
) : MinecraftPacket {
    public data class CommandSuggestionMatch(val match: String, val tooltip: TextComponent?) {
        internal companion object Codec : PacketCodec<CommandSuggestionMatch> {
            override fun encode(buffer: BytesBuffer, value: CommandSuggestionMatch) {}
            override fun decode(buffer: BytesBuffer): CommandSuggestionMatch {
                val match = buffer.readMcString()
                val hasTooltip = buffer.readBoolean()
                val tooltip = if (hasTooltip) buffer.readTextComponent() else null
                return CommandSuggestionMatch(match, tooltip)
            }
        }
    }

    internal companion object Codec : PacketCodec<ClientboundCommandSuggestionsPacket> {
        override fun encode(buffer: BytesBuffer, value: ClientboundCommandSuggestionsPacket) {}
        override fun decode(buffer: BytesBuffer): ClientboundCommandSuggestionsPacket {
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