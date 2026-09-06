/*
 * Copyright © 2026 RTAkland
 * Author: RTAkland
 * Date: 2026/9/6
 */


package cn.rtast.libmc.protocol.packet.play.clientbound

import cn.rtast.libmc.common.BytesBuffer
import cn.rtast.libmc.common.PacketCodec
import cn.rtast.libmc.common.packet.MinecraftPacket
import cn.rtast.libmc.common.readVarInt
import cn.rtast.libmc.nbt.NBTCompound
import cn.rtast.libmc.protocol.protocol.util.readNetworkNBTCompound

public data class ClientboundOpenScreenPacket(
    val windowId: Int,
    val windowType: Int,
    val title: NBTCompound,
) : MinecraftPacket {
    internal companion object Codec : PacketCodec<ClientboundOpenScreenPacket> {
        override fun encode(buffer: BytesBuffer, value: ClientboundOpenScreenPacket) {}
        override fun decode(buffer: BytesBuffer): ClientboundOpenScreenPacket {
            val windowId = buffer.readVarInt()
            val windowType = buffer.readVarInt()
            val title = buffer.readNetworkNBTCompound()
            return ClientboundOpenScreenPacket(windowId, windowType, title)
        }
    }
}