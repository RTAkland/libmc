/*
 * Copyright © 2026 RTAkland
 * Author: RTAkland
 * Date: 2026/9/5
 */


package cn.rtast.libmc.protocol.packet.play.clientbound

import cn.rtast.libmc.common.BytesBuffer
import cn.rtast.libmc.common.PacketCodec

public data object ClientboundStartConfigurationPacket : ClientboundPlayPacket,
    PacketCodec<ClientboundStartConfigurationPacket> {
    override fun encode(buffer: BytesBuffer, value: ClientboundStartConfigurationPacket) {}
    override fun decode(buffer: BytesBuffer): ClientboundStartConfigurationPacket {
        return ClientboundStartConfigurationPacket
    }
}