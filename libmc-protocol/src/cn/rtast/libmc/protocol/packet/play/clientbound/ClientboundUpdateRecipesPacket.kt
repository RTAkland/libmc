/*
 * Copyright © 2026 RTAkland
 * Author: RTAkland
 * Date: 2026/9/11
 */


package cn.rtast.libmc.protocol.packet.play.clientbound

import cn.rtast.libmc.network.BytesBuffer
import cn.rtast.libmc.packet.MinecraftPacket
import cn.rtast.libmc.packet.PacketCodec
import cn.rtast.libmc.primitives.IdSet
import cn.rtast.libmc.primitives.readIdSet
import cn.rtast.libmc.primitives.readPrefixed
import cn.rtast.libmc.primitives.readVarInt
import cn.rtast.libmc.protocol.protocol.game.Identifier
import cn.rtast.libmc.protocol.protocol.game.item.slot.SlotDisplay
import cn.rtast.libmc.protocol.protocol.game.item.slot.readSlotDisplay
import cn.rtast.libmc.protocol.protocol.game.readIdentifier

public data class ClientboundUpdateRecipesPacket(
    val propertySets: List<PropertySet>,
    val stonecutterRecipes: List<StonecutterRecipes>,
) : MinecraftPacket {
    public data class PropertySet(val id: Identifier, val items: List<Int>)
    public data class StonecutterRecipes(val ingredients: IdSet, val slotDisplay: SlotDisplay)
    internal companion object Codec : PacketCodec<ClientboundUpdateRecipesPacket> {
        override fun encode(buffer: BytesBuffer, value: ClientboundUpdateRecipesPacket) {}
        override fun decode(buffer: BytesBuffer): ClientboundUpdateRecipesPacket {
            val propertySets = buffer.readPrefixed {
                val id = readIdentifier()
                val items = readPrefixed { readVarInt() }
                PropertySet(id, items)
            }
            val stonecutterRecipes = buffer.readPrefixed {
                val ingredients = readIdSet()
                val slotDisplay = readSlotDisplay()
                StonecutterRecipes(ingredients, slotDisplay)
            }
            return ClientboundUpdateRecipesPacket(propertySets, stonecutterRecipes)
        }
    }
}