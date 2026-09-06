/*
 * Copyright © 2026 RTAkland
 * Author: RTAkland
 * Date: 2026/9/6
 */


package cn.rtast.libmc.protocol.packet.configuration.clientbound

import cn.rtast.libmc.common.BytesBuffer
import cn.rtast.libmc.common.PacketCodec
import cn.rtast.libmc.common.readVarInt
import cn.rtast.libmc.common.writeVarInt
import cn.rtast.libmc.protocol.protocol.game.Identifier
import cn.rtast.libmc.protocol.protocol.game.readIdentifier
import cn.rtast.libmc.protocol.protocol.game.writeIdentifier

public data class ClientboundUpdateTagsPacket(val registries: List<TaggedRegistry>) : ClientboundConfigurationPacket {
    public data class TaggedRegistry(val registryId: Identifier, val tags: List<Tag>) {
        internal companion object Codec : PacketCodec<TaggedRegistry> {
            override fun encode(buffer: BytesBuffer, value: TaggedRegistry) {
                buffer.writeIdentifier(value.registryId)
                buffer.writeVarInt(value.tags.size)
                value.tags.forEach { Tag.encode(buffer, it) }
            }

            override fun decode(buffer: BytesBuffer): TaggedRegistry {
                val id = buffer.readIdentifier()
                val tagCount = buffer.readVarInt()
                val tags = ArrayList<Tag>(tagCount)
                repeat(tagCount) { tags.add(Tag.decode(buffer)) }
                return TaggedRegistry(id, tags)
            }
        }
    }

    public data class Tag(val name: Identifier, val entries: IntArray) {
        internal companion object Codec : PacketCodec<Tag> {
            override fun encode(buffer: BytesBuffer, value: Tag) {
                buffer.writeIdentifier(value.name)
                buffer.writeVarInt(value.entries.size)
                value.entries.forEach { buffer.writeVarInt(it) }
            }

            override fun decode(buffer: BytesBuffer): Tag {
                val name = buffer.readIdentifier()
                val entrySize = buffer.readVarInt()
                val entries = ArrayList<Int>(entrySize)
                repeat(entrySize) { entries.add(buffer.readVarInt()) }
                return Tag(name, entries.toIntArray())
            }
        }

        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (other == null || this::class != other::class) return false
            other as Tag
            if (name != other.name) return false
            if (!entries.contentEquals(other.entries)) return false
            return true
        }

        override fun hashCode(): Int {
            var result = name.hashCode()
            result = 31 * result + entries.contentHashCode()
            return result
        }
    }

    internal companion object Codec : PacketCodec<ClientboundUpdateTagsPacket> {
        override fun encode(buffer: BytesBuffer, value: ClientboundUpdateTagsPacket) {}
        override fun decode(buffer: BytesBuffer): ClientboundUpdateTagsPacket {
            val registryCount = buffer.readVarInt()
            val registries = ArrayList<TaggedRegistry>(registryCount)
            repeat(registryCount) { registries.add(TaggedRegistry.decode(buffer)) }
            return ClientboundUpdateTagsPacket(registries)
        }
    }
}