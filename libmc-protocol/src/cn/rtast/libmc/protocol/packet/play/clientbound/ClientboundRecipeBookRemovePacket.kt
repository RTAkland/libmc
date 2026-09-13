/*
 * Copyright © 2026 RTAkland
 * Author: RTAkland
 * Date: 2026/9/9
 */


package cn.rtast.libmc.protocol.packet.play.clientbound

import cn.rtast.libmc.network.BytesBuffer
import cn.rtast.libmc.packet.MinecraftPacket
import cn.rtast.libmc.packet.PacketCodec
import cn.rtast.libmc.primitives.readPrefixed
import cn.rtast.libmc.primitives.readVarInt

public data class ClientboundRecipeBookRemovePacket(val recipes: List<Int>) : MinecraftPacket {
    internal companion object Codec : PacketCodec<ClientboundRecipeBookRemovePacket> {
        override fun encode(buffer: BytesBuffer, value: ClientboundRecipeBookRemovePacket) {}
        override fun decode(buffer: BytesBuffer): ClientboundRecipeBookRemovePacket {
            return ClientboundRecipeBookRemovePacket(buffer.readPrefixed { readVarInt() })
        }
    }
}