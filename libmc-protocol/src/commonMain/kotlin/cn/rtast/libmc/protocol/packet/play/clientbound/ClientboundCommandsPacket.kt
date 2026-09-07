/*
 * Copyright © 2026 RTAkland
 * Author: RTAkland
 * Date: 2026/9/7
 */


package cn.rtast.libmc.protocol.packet.play.clientbound

import cn.rtast.libmc.packet.MinecraftPacket
import cn.rtast.libmc.packet.PacketCodec
import cn.rtast.libmc.primitives.readPrefixed
import cn.rtast.libmc.primitives.readVarInt
import cn.rtast.libmc.protocol.protocol.game.command.CommandNode
import cn.rtast.libmc.stream.BytesBuffer

public data class ClientboundCommandsPacket(val nodes: List<CommandNode>, val rootIndex: Int) : MinecraftPacket {
    internal companion object Codec : PacketCodec<ClientboundCommandsPacket> {
        override suspend fun encode(buffer: BytesBuffer, value: ClientboundCommandsPacket) {}
        override suspend fun decode(buffer: BytesBuffer): ClientboundCommandsPacket {
            val nodes = buffer.readPrefixed { CommandNode.decode(buffer) }
            val rootIndex = buffer.readVarInt()
            return ClientboundCommandsPacket(nodes, rootIndex)
        }
    }
}