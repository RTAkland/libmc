/*
 * Copyright © 2026 RTAkland
 * Author: RTAkland
 * Date: 2026/9/4
 */


package cn.rtast.libmc.protocol.packet.play

import cn.rtast.libmc.protocol.util.readMinimalTextNbt
import cn.rtast.libmc.common.packet.MinecraftPacket
import cn.rtast.libmc.common.PacketCodec
import cn.rtast.libmc.common.BytesBuffer

public data class ClientboundDisconnectPlayPacket(val reason: String) : MinecraftPacket {
    public companion object Codec : PacketCodec<ClientboundDisconnectPlayPacket> {
        override fun encode(buffer: BytesBuffer, value: ClientboundDisconnectPlayPacket) {}
        override fun decode(buffer: BytesBuffer): ClientboundDisconnectPlayPacket {
            val reasonText = buffer.readMinimalTextNbt()
            return ClientboundDisconnectPlayPacket(reason = reasonText)
        }
    }
}