/*
 * Copyright © 2026 RTAkland
 * Author: RTAkland
 * Date: 2026/9/11
 */


package cn.rtast.libmc.protocol.packet.play.clientbound

import cn.rtast.libmc.network.BytesBuffer
import cn.rtast.libmc.packet.MinecraftPacket
import cn.rtast.libmc.packet.PacketCodec
import cn.rtast.libmc.primitives.readPrefixOptional
import cn.rtast.libmc.primitives.readPrefixed
import cn.rtast.libmc.primitives.readVarInt
import cn.rtast.libmc.protocol.protocol.game.data.component.DataComponent
import cn.rtast.libmc.protocol.protocol.game.entity.VillagerLevel
import cn.rtast.libmc.protocol.protocol.game.item.slot.Slot
import cn.rtast.libmc.protocol.protocol.game.item.slot.readSlot
import cn.rtast.libmc.protocol.registry.readDataComponent

public data class ClientboundMerchantOffersPacket(
    val windowId: Int,
    val trades: List<MerchantTrade>,
    val villagerLevel: VillagerLevel,
    val experience: Int,
    val isRegularVillager: Boolean,
    val canRestock: Boolean,
) : MinecraftPacket {
    public data class MerchantTrade(
        val inputItem1: TradeItem,
        val outputItem: Slot,
        val inputItem2: TradeItem?,
        val tradable: Boolean,
        val tradeUsed: Int,
        val maxTradeUsed: Int,
        val xp: Int,
        val specialPrice: Int,
        val priceMultiplier: Float,
        val demand: Int,
    )

    public data class TradeItem(
        val itemId: Int,
        val itemCount: Int,
        val components: Map<Int, DataComponent>,
    )

    internal companion object Codec : PacketCodec<ClientboundMerchantOffersPacket> {
        private fun BytesBuffer.readTradeItem(): TradeItem {
            val itemId = readVarInt()
            val itemCount = readVarInt()
            val componentCount = readVarInt()
            val components = HashMap<Int, DataComponent>(componentCount)
            repeat(componentCount) {
                val typeId = readVarInt()
                val componentData = readDataComponent(typeId)
                components[typeId] = componentData
            }
            return TradeItem(itemId, itemCount, components)
        }

        override fun encode(buffer: BytesBuffer, value: ClientboundMerchantOffersPacket) {}
        override fun decode(buffer: BytesBuffer): ClientboundMerchantOffersPacket {
            val windowId = buffer.readVarInt()
            val trades = buffer.readPrefixed {
                val inputItem1 = readTradeItem()
                val outputItem = readSlot()
                val inputItem2 = readPrefixOptional { readTradeItem() }
                val tradable = !readBoolean()  // reverse
                val tradeUsed = readInt()
                val maxTradeUsed = readInt()
                val xp = readInt()
                val specialPrice = readInt()
                val priceMultiplier = readFloat()
                val demand = readInt()
                MerchantTrade(
                    inputItem1, outputItem, inputItem2, tradable,
                    tradeUsed, maxTradeUsed, xp, specialPrice,
                    priceMultiplier, demand,
                )
            }
            val villagerLevel = VillagerLevel.fromLevel(buffer.readVarInt())
            val experience = buffer.readVarInt()
            val isRegularVillager = buffer.readBoolean()
            val canRestock = buffer.readBoolean()
            return ClientboundMerchantOffersPacket(
                windowId, trades, villagerLevel,
                experience, isRegularVillager, canRestock
            )
        }
    }
}