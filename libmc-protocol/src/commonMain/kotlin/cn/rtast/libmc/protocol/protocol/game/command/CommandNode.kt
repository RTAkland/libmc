/*
 * Copyright © 2026 RTAkland
 * Author: RTAkland
 * Date: 2026/9/7
 */

package cn.rtast.libmc.protocol.protocol.game.command

import cn.rtast.libmc.packet.PacketCodec
import cn.rtast.libmc.primitives.readMcString
import cn.rtast.libmc.primitives.readVarInt
import cn.rtast.libmc.primitives.writeMcString
import cn.rtast.libmc.primitives.writeVarInt
import cn.rtast.libmc.protocol.protocol.game.readIdentifier
import cn.rtast.libmc.protocol.protocol.game.writeIdentifier
import cn.rtast.libmc.stream.BytesBuffer

public data class CommandNode(
    val type: NodeType,
    val isExecutable: Boolean,
    val isRestricted: Boolean,
    val childrenIndexes: List<Int>,
    val redirectNodeIndex: Int?,
    val name: String?,
    val parserId: Int?,
    val properties: CommandArgumentProperties?,
    val suggestionsType: CommandSuggestionsType?,
) {
    public enum class NodeType(public val id: Int) {
        Root(0), Literal(1), Argument(2);

        public companion object {
            public const val NODE_TYPE_MASK: Int = 0x03
            public const val IS_EXECUTABLE_MASK: Int = 0x04
            public const val HAS_REDIRECT_MASK: Int = 0x08
            public const val HAS_SUGGESTIONS_TYPE_MASK: Int = 0x10
            public const val IS_RESTRICTED_MASK: Int = 0x20

            public fun fromID(id: Int): NodeType = entries.first { it.id == id }
        }
    }

    internal companion object Codec : PacketCodec<CommandNode> {
        private const val PARSER_INTEGER = 3
        private const val PARSER_LONG = 4
        private const val PARSER_FLOAT = 1
        private const val PARSER_DOUBLE = 2
        private const val PARSER_STRING = 5
        private const val PARSER_ENTITY = 6
        private const val PARSER_SCORE_HOLDER = 31
        private const val PARSER_TIME = 43
        private const val PARSER_RESOURCE_OR_TAG = 44
        private const val PARSER_RESOURCE_OR_TAG_KEY = 45
        private const val PARSER_RESOURCE = 46
        private const val PARSER_RESOURCE_KEY = 47
        private const val PARSER_RESOURCE_SELECTOR = 48

        override suspend fun encode(buffer: BytesBuffer, value: CommandNode) {
            var flags = value.type.id and NodeType.NODE_TYPE_MASK
            if (value.isExecutable) flags = flags or NodeType.IS_EXECUTABLE_MASK
            if (value.redirectNodeIndex != null) flags = flags or NodeType.HAS_REDIRECT_MASK
            if (value.suggestionsType != null) flags = flags or NodeType.HAS_SUGGESTIONS_TYPE_MASK
            if (value.isRestricted) flags = flags or NodeType.IS_RESTRICTED_MASK
            buffer.writeByte(flags.toByte())
            buffer.writeVarInt(value.childrenIndexes.size)
            value.childrenIndexes.forEach { buffer.writeVarInt(it) }
            if (value.redirectNodeIndex != null) buffer.writeVarInt(value.redirectNodeIndex)
            if (value.type == NodeType.Literal || value.type == NodeType.Argument) buffer.writeMcString(
                value.name ?: ""
            )

            if (value.type == NodeType.Argument) {
                val parserId = value.parserId ?: 0
                buffer.writeVarInt(parserId)
                value.properties?.let { buffer.encodeProperties(it) } ?: buffer.encodeProperties(
                    CommandArgumentProperties.Empty
                )
            }
            if (value.suggestionsType != null) buffer.writeIdentifier(value.suggestionsType.id)
        }

        override suspend fun decode(buffer: BytesBuffer): CommandNode {
            val flags = buffer.readByte().toInt() and 0xFF
            val nodeType = NodeType.fromID(flags and NodeType.NODE_TYPE_MASK)
            val isExecutable = (flags and NodeType.IS_EXECUTABLE_MASK) != 0
            val hasRedirect = (flags and NodeType.HAS_REDIRECT_MASK) != 0
            val hasSuggestionsType = (flags and NodeType.HAS_SUGGESTIONS_TYPE_MASK) != 0
            val isRestricted = (flags and NodeType.IS_RESTRICTED_MASK) != 0
            val childrenCount = buffer.readVarInt()
            val childrenIndexes = ArrayList<Int>(childrenCount)
            repeat(childrenCount) { childrenIndexes.add(buffer.readVarInt()) }
            val redirectNodeIndex = if (hasRedirect) buffer.readVarInt() else null
            val name =
                if (nodeType == NodeType.Literal || nodeType == NodeType.Argument) buffer.readMcString() else null
            var parserId: Int? = null
            var properties: CommandArgumentProperties? = null
            if (nodeType == NodeType.Argument) {
                val pId = buffer.readVarInt()
                parserId = pId
                properties = buffer.decodeProperties(pId)
            }
            val suggestionsType =
                if (hasSuggestionsType) CommandSuggestionsType.fromIdentifier(buffer.readIdentifier()) else null
            return CommandNode(
                type = nodeType,
                isExecutable = isExecutable,
                isRestricted = isRestricted,
                childrenIndexes = childrenIndexes,
                redirectNodeIndex = redirectNodeIndex,
                name = name,
                parserId = parserId,
                properties = properties,
                suggestionsType = suggestionsType
            )
        }

        private suspend fun BytesBuffer.decodeProperties(parserId: Int): CommandArgumentProperties {
            return when (parserId) {
                PARSER_INTEGER -> {
                    val flags = readByte().toInt()
                    val min = if ((flags and 0x01) != 0) readInt() else Int.MIN_VALUE
                    val max = if ((flags and 0x02) != 0) readInt() else Int.MAX_VALUE
                    CommandArgumentProperties.IntProp(min, max)
                }

                PARSER_LONG -> {
                    val flags = readByte().toInt()
                    val min = if ((flags and 0x01) != 0) readLong() else Long.MIN_VALUE
                    val max = if ((flags and 0x02) != 0) readLong() else Long.MAX_VALUE
                    CommandArgumentProperties.LongProp(min, max)
                }

                PARSER_FLOAT -> {
                    val flags = readByte().toInt()
                    val min = if ((flags and 0x01) != 0) readFloat() else -Float.MAX_VALUE
                    val max = if ((flags and 0x02) != 0) readFloat() else Float.MAX_VALUE
                    CommandArgumentProperties.FloatProp(min, max)
                }

                PARSER_DOUBLE -> {
                    val flags = readByte().toInt()
                    val min = if ((flags and 0x01) != 0) readDouble() else -Double.MAX_VALUE
                    val max = if ((flags and 0x02) != 0) readDouble() else Double.MAX_VALUE
                    CommandArgumentProperties.DoubleProp(min, max)
                }

                PARSER_STRING -> {
                    val behaviorId = readVarInt()
                    CommandArgumentProperties.StringProp(
                        CommandArgumentProperties.StringProp.StringBehavior.fromId(behaviorId)
                    )
                }

                PARSER_ENTITY -> {
                    val flags = readByte().toInt()
                    CommandArgumentProperties.Entity(
                        singleOnly = (flags and 0x01) != 0,
                        playersOnly = (flags and 0x02) != 0
                    )
                }

                PARSER_SCORE_HOLDER -> {
                    val flags = readByte().toInt()
                    CommandArgumentProperties.ScoreHolder(allowMultiple = (flags and 0x01) != 0)
                }

                PARSER_TIME -> CommandArgumentProperties.Time(min = readInt())
                PARSER_RESOURCE_OR_TAG, PARSER_RESOURCE_OR_TAG_KEY, PARSER_RESOURCE, PARSER_RESOURCE_KEY, PARSER_RESOURCE_SELECTOR -> {
                    CommandArgumentProperties.Registry(readIdentifier())
                }

                else -> CommandArgumentProperties.Empty
            }
        }

        private suspend fun BytesBuffer.encodeProperties(properties: CommandArgumentProperties) {
            when (properties) {
                is CommandArgumentProperties.IntProp -> {
                    var flags = 0
                    if (properties.min != Int.MIN_VALUE) flags = flags or 0x01
                    if (properties.max != Int.MAX_VALUE) flags = flags or 0x02
                    writeByte(flags.toByte())
                    if ((flags and 0x01) != 0) writeInt(properties.min)
                    if ((flags and 0x02) != 0) writeInt(properties.max)
                }

                is CommandArgumentProperties.LongProp -> {
                    var flags = 0
                    if (properties.min != Long.MIN_VALUE) flags = flags or 0x01
                    if (properties.max != Long.MAX_VALUE) flags = flags or 0x02
                    writeByte(flags.toByte())
                    if ((flags and 0x01) != 0) writeLong(properties.min)
                    if ((flags and 0x02) != 0) writeLong(properties.max)
                }

                is CommandArgumentProperties.FloatProp -> {
                    var flags = 0
                    if (properties.min != -Float.MAX_VALUE) flags = flags or 0x01
                    if (properties.max != Float.MAX_VALUE) flags = flags or 0x02
                    writeByte(flags.toByte())
                    if ((flags and 0x01) != 0) writeFloat(properties.min)
                    if ((flags and 0x02) != 0) writeFloat(properties.max)
                }

                is CommandArgumentProperties.DoubleProp -> {
                    var flags = 0
                    if (properties.min != -Double.MAX_VALUE) flags = flags or 0x01
                    if (properties.max != Double.MAX_VALUE) flags = flags or 0x02
                    writeByte(flags.toByte())
                    if ((flags and 0x01) != 0) writeDouble(properties.min)
                    if ((flags and 0x02) != 0) writeDouble(properties.max)
                }

                is CommandArgumentProperties.StringProp -> writeVarInt(properties.behavior.id)
                is CommandArgumentProperties.Entity -> {
                    var flags = 0
                    if (properties.singleOnly) flags = flags or 0x01
                    if (properties.playersOnly) flags = flags or 0x02
                    writeByte(flags.toByte())
                }

                is CommandArgumentProperties.ScoreHolder -> {
                    val flags = if (properties.allowMultiple) 0x01 else 0x00
                    writeByte(flags.toByte())
                }

                is CommandArgumentProperties.Time -> writeInt(properties.min)
                is CommandArgumentProperties.Registry -> writeIdentifier(properties.registry)
                CommandArgumentProperties.Empty -> {}
            }
        }
    }
}