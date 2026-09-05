/*
 * Copyright © 2026 RTAkland
 * Author: RTAkland
 * Date: 2026/9/4
 */


package cn.rtast.libmc.protocol.packet.login

import cn.rtast.libmc.common.BytesBuffer
import cn.rtast.libmc.common.PacketCodec
import cn.rtast.libmc.common.packet.MinecraftPacket
import cn.rtast.libmc.common.readMcString

public data class ClientboundDisconnectLoginPacket(val reason: String) : MinecraftPacket {
    public companion object Codec : PacketCodec<ClientboundDisconnectLoginPacket> {
        override fun encode(buffer: BytesBuffer, value: ClientboundDisconnectLoginPacket) {}
        override fun decode(buffer: BytesBuffer): ClientboundDisconnectLoginPacket {
            val reasonJson = buffer.readMcString()
            return ClientboundDisconnectLoginPacket(reason = reasonJson)
        }
    }
}