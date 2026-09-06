/*
 * Copyright © 2026 RTAkland
 * Author: RTAkland
 * Date: 2026/9/6
 */


package cn.rtast.libmc.protocol.packet.play.clientbound

import cn.rtast.libmc.common.BytesBuffer
import cn.rtast.libmc.common.PacketCodec
import cn.rtast.libmc.common.packet.MinecraftPacket

public data object ClientboundLowDiskSpaceWarningPacket : MinecraftPacket,
    PacketCodec<ClientboundLowDiskSpaceWarningPacket> {
    override fun encode(buffer: BytesBuffer, value: ClientboundLowDiskSpaceWarningPacket) {}
    override fun decode(buffer: BytesBuffer): ClientboundLowDiskSpaceWarningPacket =
        ClientboundLowDiskSpaceWarningPacket
}