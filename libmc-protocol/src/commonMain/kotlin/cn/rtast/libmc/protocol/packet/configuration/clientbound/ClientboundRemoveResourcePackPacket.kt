/*
 * Copyright © 2026 RTAkland
 * Author: RTAkland
 * Date: 2026/9/6
 */


package cn.rtast.libmc.protocol.packet.configuration.clientbound

import cn.rtast.libmc.network.BytesBuffer
import cn.rtast.libmc.packet.MinecraftPacket
import cn.rtast.libmc.packet.PacketCodec
import cn.rtast.libmc.primitives.readUuid
import kotlin.uuid.Uuid

public data class ClientboundRemoveResourcePackPacket(val uuid: Uuid) : MinecraftPacket {
    internal companion object Codec : PacketCodec<ClientboundRemoveResourcePackPacket> {
        override fun encode(buffer: BytesBuffer, value: ClientboundRemoveResourcePackPacket) {}
        override fun decode(buffer: BytesBuffer): ClientboundRemoveResourcePackPacket {
            val uuid = buffer.readUuid()
            return ClientboundRemoveResourcePackPacket(uuid)
        }
    }
}