/*
 * Copyright © 2026 RTAkland
 * Author: RTAkland
 * Date: 2026/9/6
 */


package cn.rtast.libmc.protocol.packet.configuration.clientbound

import cn.rtast.libmc.common.packet.MinecraftPacket
import cn.rtast.libmc.common.packet.PacketCodec
import cn.rtast.libmc.common.primitives.readMcString
import cn.rtast.libmc.common.primitives.readUuid
import cn.rtast.libmc.common.primitives.readVarInt
import cn.rtast.libmc.common.stream.BytesBuffer
import cn.rtast.libmc.nbt.NBTCompound
import cn.rtast.libmc.protocol.protocol.util.readNetworkNBTCompound
import kotlin.uuid.Uuid

public data class ClientboundAddResourcePackPacket(
    val uuid: Uuid,
    val url: String,
    val hash: String,
    val forced: Boolean,
    val prompt: NBTCompound,
) : MinecraftPacket {
    internal companion object Codec : PacketCodec<ClientboundAddResourcePackPacket> {
        override suspend fun encode(buffer: BytesBuffer, value: ClientboundAddResourcePackPacket) {}
        override suspend fun decode(buffer: BytesBuffer): ClientboundAddResourcePackPacket {
            val uuid = buffer.readUuid()
            val url = buffer.readMcString()
            val hash = buffer.readMcString()
            val forced = buffer.readBoolean()
            buffer.readVarInt()  // ?
            val prompt = buffer.readNetworkNBTCompound()  // ?
            return ClientboundAddResourcePackPacket(uuid, url, hash, forced, prompt)
        }
    }
}