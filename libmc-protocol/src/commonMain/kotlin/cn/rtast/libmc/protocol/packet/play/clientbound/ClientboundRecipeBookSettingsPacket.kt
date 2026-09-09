/*
 * Copyright © 2026 RTAkland
 * Author: RTAkland
 * Date: 2026/9/9
 */


package cn.rtast.libmc.protocol.packet.play.clientbound

import cn.rtast.libmc.network.BytesBuffer
import cn.rtast.libmc.packet.MinecraftPacket
import cn.rtast.libmc.packet.PacketCodec

public data class ClientboundRecipeBookSettingsPacket(
    val craftingBookOpen: Boolean,
    val craftingFilterActive: Boolean,
    val smeltingBookOpen: Boolean,
    val smeltingFilterActive: Boolean,
    val blastFurnaceBookOpen: Boolean,
    val blastFurnaceFilterActive: Boolean,
    val smokerBookOpen: Boolean,
    val smokerFilterActive: Boolean,
) : MinecraftPacket {
    internal companion object Codec : PacketCodec<ClientboundRecipeBookSettingsPacket> {
        override fun encode(buffer: BytesBuffer, value: ClientboundRecipeBookSettingsPacket) {}
        override fun decode(buffer: BytesBuffer): ClientboundRecipeBookSettingsPacket {
            val craftingBookOpen = buffer.readBoolean()
            val craftingFilterActive = buffer.readBoolean()
            val smeltingBookOpen = buffer.readBoolean()
            val smeltingFilterActive = buffer.readBoolean()
            val blastFurnaceBookOpen = buffer.readBoolean()
            val blastFurnaceFilterActive = buffer.readBoolean()
            val smokerBookOpen = buffer.readBoolean()
            val smokerFilterActive = buffer.readBoolean()
            return ClientboundRecipeBookSettingsPacket(
                craftingBookOpen, craftingFilterActive,
                smeltingBookOpen, smeltingFilterActive,
                blastFurnaceBookOpen, blastFurnaceFilterActive,
                smokerBookOpen, smokerFilterActive
            )
        }
    }
}