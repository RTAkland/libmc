/*
 * Copyright © 2026 RTAkland
 * Author: RTAkland
 * Date: 2026/9/6
 */


package cn.rtast.libmc.protocol.packet.configuration.clientbound

import cn.rtast.libmc.common.BytesBuffer
import cn.rtast.libmc.common.PacketCodec
import cn.rtast.libmc.common.readUuid
import kotlin.uuid.Uuid

public data class ClientboundRemoveResourcePackPacket(val uuid: Uuid) : ClientboundConfigurationPacket {
    public companion object Codec : PacketCodec<ClientboundRemoveResourcePackPacket> {
        override fun encode(buffer: BytesBuffer, value: ClientboundRemoveResourcePackPacket) {}
        override fun decode(buffer: BytesBuffer): ClientboundRemoveResourcePackPacket {
            val uuid = buffer.readUuid()
            return ClientboundRemoveResourcePackPacket(uuid)
        }
    }
}