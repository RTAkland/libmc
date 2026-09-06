/*
 * Copyright © 2026 RTAkland
 * Author: RTAkland
 * Date: 2026/9/6
 */


package cn.rtast.libmc.protocol.packet.configuration.clientbound

import cn.rtast.libmc.common.*
import kotlinx.serialization.Serializable

public data class ClientboundCustomReportDetailsPacket(val details: List<ReportDetail>) :
    ClientboundConfigurationPacket {
    @Serializable
    public data class ReportDetail(val title: String, val description: String) {
        public companion object Codec : PacketCodec<ReportDetail> {
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

    public companion object Codec : PacketCodec<ClientboundCustomReportDetailsPacket> {
        override fun encode(buffer: BytesBuffer, value: ClientboundCustomReportDetailsPacket) {}
        override fun decode(buffer: BytesBuffer): ClientboundCustomReportDetailsPacket {
            val detailCount = buffer.readVarInt()
            val details = ArrayList<ReportDetail>(detailCount)
            repeat(detailCount) { details.add(ReportDetail.decode(buffer)) }
            return ClientboundCustomReportDetailsPacket(details)
        }
    }
}