/*
 * Copyright © 2026 RTAkland
 * Author: RTAkland
 * Date: 2026/9/4
 */


package cn.rtast.libmc.protocol.packet.play

import cn.rtast.libmc.common.*
import cn.rtast.libmc.common.packet.MinecraftPacket
import cn.rtast.libmc.protocol.util.writeMinimalTextNbt
import kotlin.uuid.Uuid

public data class ClientboundPlayerChatMessagePacket(
    val globalIndex: Int,
    val sender: Uuid,
    val index: Int,
    val messageSignature: ByteArray?,
    val message: String,
    val timestamp: Long,
    val salt: Long,
    val previousMessages: List<PreviousMessageEntry>,
    val unsignedContent: String?,
    val filterType: ChatFilterType,
    val filterMaskBits: LongArray?,
    val chatType: Int,
    val senderName: String,
    val targetName: String?,
) : MinecraftPacket {
    public enum class ChatFilterType(public val id: Int) {
        PASS_THROUGH(0),
        FULLY_FILTERED(1),
        PARTIALLY_FILTERED(2);

        public companion object {
            public fun fromId(id: Int): ChatFilterType =
                entries.firstOrNull { it.id == id } ?: PASS_THROUGH
        }
    }

    public data class PreviousMessageEntry(
        val messageId: Int,
        val signature: ByteArray?,
    ) {
        public companion object Codec : PacketCodec<PreviousMessageEntry> {
            override fun encode(buffer: BytesBuffer, value: PreviousMessageEntry) {
                buffer.writeVarInt(value.messageId)
                if (value.messageId == 0) {
                    val sig = requireNotNull(value.signature) { "signature must be present when messageId is 0" }
                    require(sig.size == 256)
                    buffer.writeBytes(sig)
                }
            }

            override fun decode(buffer: BytesBuffer): PreviousMessageEntry = throw UnsupportedOperationException()  // TODO
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

    public companion object Codec : PacketCodec<ClientboundPlayerChatMessagePacket> {
        override fun encode(buffer: BytesBuffer, value: ClientboundPlayerChatMessagePacket) {
            buffer.writeVarInt(value.globalIndex)
            buffer.writeUuid(value.sender)
            buffer.writeVarInt(value.index)
            val hasSignature = value.messageSignature != null
            buffer.writeBoolean(hasSignature)
            if (hasSignature) buffer.writeBytes(requireNotNull(value.messageSignature))

            buffer.writeMcString(value.message)
            buffer.writeLong(value.timestamp)
            buffer.writeLong(value.salt)

            require(value.previousMessages.size == 20)
            buffer.writeVarInt(value.previousMessages.size)
            value.previousMessages.forEach { entry -> PreviousMessageEntry.encode(buffer, entry) }

            val hasUnsignedContent = value.unsignedContent != null
            buffer.writeBoolean(hasUnsignedContent)
            value.unsignedContent?.let { buffer.writeMinimalTextNbt(it) }

            buffer.writeVarInt(value.filterType.id)
            if (value.filterType == ChatFilterType.PARTIALLY_FILTERED) {
                val mask = requireNotNull(value.filterMaskBits)
                buffer.writeVarInt(mask.size)
                mask.forEach { buffer.writeLong(it) }
            }

            buffer.writeVarInt(value.chatType)
            buffer.writeMinimalTextNbt(value.senderName)

            val hasTargetName = value.targetName != null
            buffer.writeBoolean(hasTargetName)
            value.targetName?.let { buffer.writeMinimalTextNbt(it) }
        }

        override fun decode(buffer: BytesBuffer): ClientboundPlayerChatMessagePacket =
            throw UnsupportedOperationException()  // TODO
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