/*
 * Copyright © 2026 RTAkland
 * Author: RTAkland
 * Date: 2026/9/6
 */


package cn.rtast.libmc.protocol.packet.play.clientbound

import cn.rtast.libmc.packet.MinecraftPacket
import cn.rtast.libmc.packet.PacketCodec
import cn.rtast.libmc.primitives.IdOrX
import cn.rtast.libmc.primitives.readIdOrX
import cn.rtast.libmc.primitives.readOptional
import cn.rtast.libmc.stream.BytesBuffer
import cn.rtast.libmc.nbt.NBTCompound
import cn.rtast.libmc.protocol.protocol.game.chat.InlineChatType
import cn.rtast.libmc.protocol.protocol.game.chat.readInlineChatType
import cn.rtast.libmc.protocol.protocol.util.readNetworkNBTCompound

public data class ClientboundDisguisedChatMessagePacket(
    val message: NBTCompound,
    val chatType: IdOrX<InlineChatType>,
    val senderName: NBTCompound,
    val targetName: NBTCompound?,
) : MinecraftPacket {
    internal companion object Codec : PacketCodec<ClientboundDisguisedChatMessagePacket> {
        override suspend fun encode(buffer: BytesBuffer, value: ClientboundDisguisedChatMessagePacket) {}
        override suspend fun decode(buffer: BytesBuffer): ClientboundDisguisedChatMessagePacket {
            val message = buffer.readNetworkNBTCompound()
            val chatType = buffer.readIdOrX { readInlineChatType() }
            val senderName = buffer.readNetworkNBTCompound()
            val targetName = buffer.readOptional { readNetworkNBTCompound() }
            return ClientboundDisguisedChatMessagePacket(message, chatType, senderName, targetName)
        }
    }
}