/*
 * Copyright © 2026 RTAkland
 * Author: RTAkland
 * Date: 2026/9/6
 */


package cn.rtast.libmc.protocol.packet.configuration.clientbound

import cn.rtast.libmc.packet.MinecraftPacket
import cn.rtast.libmc.packet.PacketCodec
import cn.rtast.libmc.primitives.readMcString
import cn.rtast.libmc.primitives.readUuid
import cn.rtast.libmc.primitives.readVarInt
import cn.rtast.libmc.protocol.protocol.game.chat.TextComponent
import cn.rtast.libmc.protocol.protocol.game.chat.readTextComponent
import cn.rtast.libmc.network.BytesBuffer
import kotlin.uuid.Uuid

public data class ClientboundAddResourcePackPacket(
    val uuid: Uuid,
    val url: String,
    val hash: String,
    val forced: Boolean,
    val prompt: TextComponent,
) : MinecraftPacket {
    internal companion object Codec : PacketCodec<ClientboundAddResourcePackPacket> {
        override fun encode(buffer: BytesBuffer, value: ClientboundAddResourcePackPacket) {}
        override fun decode(buffer: BytesBuffer): ClientboundAddResourcePackPacket {
            val uuid = buffer.readUuid()
            val url = buffer.readMcString()
            val hash = buffer.readMcString()
            val forced = buffer.readBoolean()
            buffer.readVarInt()  // ?
            val prompt = buffer.readTextComponent()  // ?
            return ClientboundAddResourcePackPacket(uuid, url, hash, forced, prompt)
        }
    }
}