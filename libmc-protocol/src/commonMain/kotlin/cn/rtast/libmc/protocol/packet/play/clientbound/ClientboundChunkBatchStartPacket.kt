/*
 * Copyright © 2026 RTAkland
 * Author: RTAkland
 * Date: 2026/9/6
 */


package cn.rtast.libmc.protocol.packet.play.clientbound

import cn.rtast.libmc.common.BytesBuffer
import cn.rtast.libmc.common.PacketCodec

public data object ClientboundChunkBatchStartPacket : ClientboundPlayPacket,
    PacketCodec<ClientboundChunkBatchStartPacket> {
    override fun encode(buffer: BytesBuffer, value: ClientboundChunkBatchStartPacket) {}
    override fun decode(buffer: BytesBuffer): ClientboundChunkBatchStartPacket = ClientboundChunkBatchStartPacket
}