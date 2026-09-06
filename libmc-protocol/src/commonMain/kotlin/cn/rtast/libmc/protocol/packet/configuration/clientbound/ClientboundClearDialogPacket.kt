/*
 * Copyright © 2026 RTAkland
 * Author: RTAkland
 * Date: 2026/9/6
 */


package cn.rtast.libmc.protocol.packet.configuration.clientbound

import cn.rtast.libmc.common.BytesBuffer
import cn.rtast.libmc.common.PacketCodec

public data object ClientboundClearDialogPacket : ClientboundConfigurationPacket,
    PacketCodec<ClientboundClearDialogPacket> {
    override fun encode(buffer: BytesBuffer, value: ClientboundClearDialogPacket) {}
    override fun decode(buffer: BytesBuffer): ClientboundClearDialogPacket = ClientboundClearDialogPacket
}