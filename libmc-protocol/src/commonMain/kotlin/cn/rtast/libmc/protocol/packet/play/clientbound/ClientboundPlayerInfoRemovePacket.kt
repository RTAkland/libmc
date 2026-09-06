/*
 * Copyright © 2026 RTAkland
 * Author: RTAkland
 * Date: 2026/9/6
 */


package cn.rtast.libmc.protocol.packet.play.clientbound

import cn.rtast.libmc.common.BytesBuffer
import cn.rtast.libmc.common.PacketCodec
import cn.rtast.libmc.common.packet.MinecraftPacket
import cn.rtast.libmc.common.readPrefixed
import cn.rtast.libmc.common.readUuid
import kotlin.uuid.Uuid

public data class ClientboundPlayerInfoRemovePacket(val uuids: List<Uuid>) : MinecraftPacket {
    internal companion object Codec : PacketCodec<ClientboundPlayerInfoRemovePacket> {
        override fun encode(buffer: BytesBuffer, value: ClientboundPlayerInfoRemovePacket) {}
        override fun decode(buffer: BytesBuffer): ClientboundPlayerInfoRemovePacket {
            return ClientboundPlayerInfoRemovePacket(buffer.readPrefixed { readUuid() })
        }
    }
}