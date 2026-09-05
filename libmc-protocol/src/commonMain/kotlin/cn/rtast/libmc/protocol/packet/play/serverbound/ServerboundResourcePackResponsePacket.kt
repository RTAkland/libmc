/*
 * Copyright © 2026 RTAkland
 * Author: RTAkland
 * Date: 2026/9/6
 */


package cn.rtast.libmc.protocol.packet.play.serverbound

import cn.rtast.libmc.common.BytesBuffer
import cn.rtast.libmc.common.PacketCodec
import cn.rtast.libmc.common.packet.MinecraftPacket
import cn.rtast.libmc.common.writeUuid
import cn.rtast.libmc.common.writeVarInt
import cn.rtast.libmc.protocol.protocol.game.resources.ResourcePackResult
import kotlin.uuid.Uuid

public data class ServerboundResourcePackResponsePacket(
    val uuid: Uuid,
    val result: ResourcePackResult,
) : MinecraftPacket {
    public companion object Codec : PacketCodec<ServerboundResourcePackResponsePacket> {
        override fun encode(buffer: BytesBuffer, value: ServerboundResourcePackResponsePacket) {
            buffer.writeUuid(value.uuid)
            buffer.writeVarInt(value.result.id)
        }

        override fun decode(buffer: BytesBuffer): ServerboundResourcePackResponsePacket =
            throw UnsupportedOperationException()
    }
}