/*
 * Copyright © 2026 RTAkland
 * Author: RTAkland
 * Date: 2026/9/4
 */


package cn.rtast.libmc.protocol.packet.play.clientbound

import cn.rtast.libmc.stream.BytesBuffer
import cn.rtast.libmc.packet.MinecraftPacket
import cn.rtast.libmc.packet.PacketCodec
import cn.rtast.libmc.primitives.readMcString
import cn.rtast.libmc.primitives.readUuid
import cn.rtast.libmc.primitives.readVarInt
import cn.rtast.libmc.nbt.NBTCompound
import cn.rtast.libmc.protocol.protocol.util.readNetworkNBTCompound
import kotlin.uuid.Uuid

/**
 * ref: https://minecraft.wiki/w/Java_Edition_protocol/Packets#Player_Chat_Message
 */
public data class ClientboundPlayerChatMessagePacket(
    val globalIndex: Int,
    val sender: Uuid,
    val index: Int,
    val messageSignature: ByteArray?,
    val message: String,
    val timestamp: Long,
    val salt: Long,
    val previousMessages: List<PreviousMessageEntry>,
    val unsignedContent: NBTCompound?,
    val filterType: ChatFilterType,
    val filterMaskBits: LongArray?,
    val chatType: Int,
    val senderName: NBTCompound,
    val targetName: NBTCompound?,
) : MinecraftPacket {
    public enum class ChatFilterType(public val id: Int) {
        PASS_THROUGH(0),
        FULLY_FILTERED(1),
        PARTIALLY_FILTERED(2);

        internal companion object {
            fun fromID(id: Int): ChatFilterType =
                entries.firstOrNull { it.id == id } ?: PASS_THROUGH
        }
    }

    public data class PreviousMessageEntry(val messageId: Int, val signature: ByteArray?) {
        internal companion object Codec : PacketCodec<PreviousMessageEntry> {
            override suspend fun encode(buffer: BytesBuffer, value: PreviousMessageEntry) {}
            override suspend fun decode(buffer: BytesBuffer): PreviousMessageEntry {
                val messageId = buffer.readVarInt()
                val signature = if (messageId == 0) buffer.readBytes(256) else null
                return PreviousMessageEntry(messageId, signature)
            }
        }

        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (other == null || this::class != other::class) return false
            other as PreviousMessageEntry
            if (messageId != other.messageId) return false
            if (!signature.contentEquals(other.signature)) return false
            return true
        }

        override fun hashCode(): Int {
            var result = messageId
            result = 31 * result + (signature?.contentHashCode() ?: 0)
            return result
        }
    }

    internal companion object Codec : PacketCodec<ClientboundPlayerChatMessagePacket> {
        override suspend fun encode(buffer: BytesBuffer, value: ClientboundPlayerChatMessagePacket) {}
        override suspend fun decode(buffer: BytesBuffer): ClientboundPlayerChatMessagePacket {
            val globalIndex = buffer.readVarInt()
            val sender = buffer.readUuid()
            val index = buffer.readVarInt()
            val hasSignature = buffer.readBoolean()
            val messageSignature = if (hasSignature) buffer.readBytes(256) else null

            val message = buffer.readMcString()
            val timestamp = buffer.readLong()
            val salt = buffer.readLong()

            val prevMessageCount = buffer.readVarInt()
            val prevMessages = List(prevMessageCount) { PreviousMessageEntry.decode(buffer) }

            val hasUnsignedContent = buffer.readBoolean()
            val unsignedContent = if (hasUnsignedContent) buffer.readNetworkNBTCompound() else null  // ?

            val filterTypeId = buffer.readVarInt()
            val filterType = ChatFilterType.fromID(filterTypeId)

            val filterMaskBits = if (filterType == ChatFilterType.PARTIALLY_FILTERED) {
                val bitSetLen = buffer.readVarInt()
                LongArray(bitSetLen) { buffer.readLong() }
            } else null

            val chatType = buffer.readVarInt()
            val senderName = buffer.readNetworkNBTCompound() // ?

            val hasTargetName = buffer.readBoolean()
            val targetName = if (hasTargetName) buffer.readNetworkNBTCompound() else null // ?
            return ClientboundPlayerChatMessagePacket(
                globalIndex, sender, index,
                messageSignature, message,
                timestamp, salt, prevMessages,
                unsignedContent, filterType,
                filterMaskBits, chatType,
                senderName, targetName
            )
        }
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || this::class != other::class) return false

        other as ClientboundPlayerChatMessagePacket

        if (globalIndex != other.globalIndex) return false
        if (index != other.index) return false
        if (timestamp != other.timestamp) return false
        if (salt != other.salt) return false
        if (chatType != other.chatType) return false
        if (sender != other.sender) return false
        if (!messageSignature.contentEquals(other.messageSignature)) return false
        if (message != other.message) return false
        if (previousMessages != other.previousMessages) return false
        if (unsignedContent != other.unsignedContent) return false
        if (filterType != other.filterType) return false
        if (!filterMaskBits.contentEquals(other.filterMaskBits)) return false
        if (senderName != other.senderName) return false
        if (targetName != other.targetName) return false

        return true
    }

    override fun hashCode(): Int {
        var result = globalIndex
        result = 31 * result + index
        result = 31 * result + timestamp.hashCode()
        result = 31 * result + salt.hashCode()
        result = 31 * result + chatType
        result = 31 * result + sender.hashCode()
        result = 31 * result + (messageSignature?.contentHashCode() ?: 0)
        result = 31 * result + message.hashCode()
        result = 31 * result + previousMessages.hashCode()
        result = 31 * result + unsignedContent.hashCode()
        result = 31 * result + filterType.hashCode()
        result = 31 * result + (filterMaskBits?.contentHashCode() ?: 0)
        result = 31 * result + senderName.hashCode()
        result = 31 * result + targetName.hashCode()
        return result
    }
}