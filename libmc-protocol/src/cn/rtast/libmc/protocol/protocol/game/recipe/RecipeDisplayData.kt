/*
 * Copyright © 2026 RTAkland
 * Author: RTAkland
 * Date: 2026/9/11
 */


package cn.rtast.libmc.protocol.protocol.game.recipe

import cn.rtast.libmc.network.BytesBuffer
import cn.rtast.libmc.packet.PacketCodec
import cn.rtast.libmc.primitives.readVarInt
import cn.rtast.libmc.primitives.writePrefixed
import cn.rtast.libmc.primitives.writeVarInt
import cn.rtast.libmc.protocol.protocol.game.item.slot.SlotDisplay
import cn.rtast.libmc.protocol.protocol.game.item.slot.readSlotDisplay
import cn.rtast.libmc.protocol.protocol.game.item.slot.writeSlotDisplay

public sealed interface RecipeDisplayData {
    public val result: SlotDisplay
    public val craftingStation: SlotDisplay

    public data class CraftingShapeless(
        val ingredientCount: Int,
        val ingredients: List<SlotDisplay>,
        override val result: SlotDisplay,
        override val craftingStation: SlotDisplay,
    ) : RecipeDisplayData {
        internal companion object Codec : PacketCodec<CraftingShapeless> {
            override fun encode(buffer: BytesBuffer, value: CraftingShapeless) {
                buffer.writePrefixed(value.ingredients) { writeSlotDisplay(it) }
                buffer.writeSlotDisplay(value.result)
                buffer.writeSlotDisplay(value.craftingStation)
            }

            override fun decode(buffer: BytesBuffer): CraftingShapeless {
                val ingredientCount = buffer.readVarInt()
                val ingredients = ArrayList<SlotDisplay>(ingredientCount)
                repeat(ingredientCount) { ingredients.add(buffer.readSlotDisplay()) }
                val result = buffer.readSlotDisplay()
                val craftingStation = buffer.readSlotDisplay()
                return CraftingShapeless(ingredientCount, ingredients, result, craftingStation)
            }
        }
    }

    public data class CraftingShaped(
        val width: Int,
        val height: Int,
        val ingredientCount: Int,
        val ingredients: List<SlotDisplay>,
        override val result: SlotDisplay,
        override val craftingStation: SlotDisplay,
    ) : RecipeDisplayData {
        internal companion object Codec : PacketCodec<CraftingShaped> {
            override fun encode(buffer: BytesBuffer, value: CraftingShaped) {
                buffer.writeVarInt(value.width)
                buffer.writeVarInt(value.height)
                buffer.writePrefixed(value.ingredients) { writeSlotDisplay(it) }
                buffer.writeSlotDisplay(value.result)
                buffer.writeSlotDisplay(value.craftingStation)
            }

            override fun decode(buffer: BytesBuffer): CraftingShaped {
                val width = buffer.readVarInt()
                val height = buffer.readVarInt()
                val ingredientCount = buffer.readVarInt()
                val ingredients = ArrayList<SlotDisplay>(ingredientCount)
                repeat(ingredientCount) { ingredients.add(buffer.readSlotDisplay()) }
                val result = buffer.readSlotDisplay()
                val craftingStation = buffer.readSlotDisplay()
                return CraftingShaped(width, height, ingredientCount, ingredients, result, craftingStation)
            }
        }
    }

    public data class Furnace(
        val ingredient: SlotDisplay,
        val fuel: SlotDisplay,
        override val result: SlotDisplay,
        override val craftingStation: SlotDisplay,
        val cookingTime: Int,  // ticks
        val experience: Float,
    ) : RecipeDisplayData {
        internal companion object Codec : PacketCodec<Furnace> {
            override fun encode(buffer: BytesBuffer, value: Furnace) {
                buffer.writeSlotDisplay(value.ingredient)
                buffer.writeSlotDisplay(value.fuel)
                buffer.writeSlotDisplay(value.result)
                buffer.writeSlotDisplay(value.craftingStation)
                buffer.writeVarInt(value.cookingTime)
                buffer.writeFloat(value.experience)
            }

            override fun decode(buffer: BytesBuffer): Furnace {
                val ingredient = buffer.readSlotDisplay()
                val fuel = buffer.readSlotDisplay()
                val result = buffer.readSlotDisplay()
                val craftingStation = buffer.readSlotDisplay()
                val cookingTime = buffer.readVarInt()
                val experience = buffer.readFloat()
                return Furnace(ingredient, fuel, result, craftingStation, cookingTime, experience)
            }
        }
    }

    public data class Stonecutter(
        val ingredient: SlotDisplay,
        override val result: SlotDisplay,
        override val craftingStation: SlotDisplay,
    ) : RecipeDisplayData {
        internal companion object Codec : PacketCodec<Stonecutter> {
            override fun encode(buffer: BytesBuffer, value: Stonecutter) {
                buffer.writeSlotDisplay(value.ingredient)
                buffer.writeSlotDisplay(value.result)
                buffer.writeSlotDisplay(value.craftingStation)
            }

            override fun decode(buffer: BytesBuffer): Stonecutter {
                val ingredient = buffer.readSlotDisplay()
                val result = buffer.readSlotDisplay()
                val craftingStation = buffer.readSlotDisplay()
                return Stonecutter(ingredient, result, craftingStation)
            }
        }
    }

    public data class Smithing(
        val template: SlotDisplay,
        val base: SlotDisplay,
        val addition: SlotDisplay,
        override val result: SlotDisplay,
        override val craftingStation: SlotDisplay,
    ) : RecipeDisplayData {
        internal companion object Codec : PacketCodec<Smithing> {
            override fun encode(buffer: BytesBuffer, value: Smithing) {
                buffer.writeSlotDisplay(value.template)
                buffer.writeSlotDisplay(value.base)
                buffer.writeSlotDisplay(value.addition)
                buffer.writeSlotDisplay(value.result)
                buffer.writeSlotDisplay(value.craftingStation)
            }

            override fun decode(buffer: BytesBuffer): Smithing {
                val template = buffer.readSlotDisplay()
                val base = buffer.readSlotDisplay()
                val addition = buffer.readSlotDisplay()
                val result = buffer.readSlotDisplay()
                val craftingStation = buffer.readSlotDisplay()
                return Smithing(template, base, addition, result, craftingStation)
            }
        }
    }
}