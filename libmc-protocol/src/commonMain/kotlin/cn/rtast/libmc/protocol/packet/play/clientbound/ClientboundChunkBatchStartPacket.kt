/*
 * Copyright © 2026 RTAkland
 * Author: RTAkland
 * Date: 2026/9/6
 */


package cn.rtast.libmc.protocol.packet.play.clientbound

import cn.rtast.libmc.network.BytesBuffer
import cn.rtast.libmc.packet.MinecraftPacket
import cn.rtast.libmc.packet.PacketCodec

public data object ClientboundChunkBatchStartPacket : MinecraftPacket,
    PacketCodec<ClientboundChunkBatchStartPacket> {
    override fun encode(buffer: BytesBuffer, value: ClientboundChunkBatchStartPacket) {}
    override fun decode(buffer: BytesBuffer): ClientboundChunkBatchStartPacket = ClientboundChunkBatchStartPacket
}