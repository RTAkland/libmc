/*
 * Copyright © 2026 RTAkland
 * Author: RTAkland
 * Date: 2026/9/6
 */


package cn.rtast.libmc.protocol.packet.configuration.clientbound

import cn.rtast.libmc.network.BytesBuffer
import cn.rtast.libmc.packet.MinecraftPacket
import cn.rtast.libmc.packet.PacketCodec
import cn.rtast.libmc.primitives.readMcString
import cn.rtast.libmc.primitives.readVarInt
import cn.rtast.libmc.primitives.writeMcString

public data class ClientboundCustomReportDetailsPacket(val details: List<ReportDetail>) : MinecraftPacket {
    public data class ReportDetail(val title: String, val description: String) {
        internal companion object Codec : PacketCodec<ReportDetail> {
            override fun encode(buffer: BytesBuffer, value: ReportDetail) {
                buffer.writeMcString(value.title)
                buffer.writeMcString(value.description)
            }

            override fun decode(buffer: BytesBuffer): ReportDetail {
                val title = buffer.readMcString()
                val description = buffer.readMcString()
                return ReportDetail(title, description)
            }
        }
    }

    internal companion object Codec : PacketCodec<ClientboundCustomReportDetailsPacket> {
        override fun encode(buffer: BytesBuffer, value: ClientboundCustomReportDetailsPacket) {}
        override fun decode(buffer: BytesBuffer): ClientboundCustomReportDetailsPacket {
            val detailCount = buffer.readVarInt()
            val details = ArrayList<ReportDetail>(detailCount)
            repeat(detailCount) { details.add(ReportDetail.decode(buffer)) }
            return ClientboundCustomReportDetailsPacket(details)
        }
    }
}