/*
 * Copyright © 2026 RTAkland
 * Author: RTAkland
 * Date: 2026/9/9
 */


package cn.rtast.libmc.protocol.protocol.game.inventory.slot

import cn.rtast.libmc.network.BytesBuffer
import cn.rtast.libmc.primitives.readVarInt
import cn.rtast.libmc.primitives.writeVarInt
import cn.rtast.libmc.protocol.protocol.game.Identifier
import cn.rtast.libmc.protocol.protocol.game.readIdentifier
import cn.rtast.libmc.protocol.protocol.game.writeIdentifier

public sealed interface SlotDisplay {
    public object Empty : SlotDisplay
    public object AnyFuel : SlotDisplay
    public object WithAnyPotion : SlotDisplay
    public data class OnlyWithComponent(val base: SlotDisplay, val componentTypeId: Int) : SlotDisplay
    public data class Item(val itemTypeId: Int) : SlotDisplay
    public data class ItemStack(val itemStack: Slot) : SlotDisplay
    public data class Tag(val tagName: Identifier) : SlotDisplay
    public data class Dyed(val dye: SlotDisplay, val target: SlotDisplay) : SlotDisplay
    public data class SmithingTrim(val base: SlotDisplay, val material: SlotDisplay, val pattern: Int) : SlotDisplay
    public data class WithRemainder(val ingredient: SlotDisplay, val remainder: SlotDisplay) : SlotDisplay
    public data class Composite(val options: List<SlotDisplay>) : SlotDisplay
}

internal fun BytesBuffer.readSlotDisplay(): SlotDisplay {
    return when (val typeId = this.readVarInt()) {
        0 -> SlotDisplay.Empty
        1 -> SlotDisplay.AnyFuel
        2 -> SlotDisplay.WithAnyPotion
        3 -> SlotDisplay.OnlyWithComponent(this.readSlotDisplay(), this.readVarInt())
        4 -> SlotDisplay.Item(this.readVarInt())
        5 -> SlotDisplay.ItemStack(this.readSlot())
        6 -> SlotDisplay.Tag(this.readIdentifier())
        7 -> SlotDisplay.Dyed(dye = this.readSlotDisplay(), target = this.readSlotDisplay())
        8 -> SlotDisplay.SmithingTrim(this.readSlotDisplay(), this.readSlotDisplay(), this.readVarInt())
        9 -> SlotDisplay.WithRemainder(this.readSlotDisplay(), this.readSlotDisplay())
        10 -> {
            val count = this.readVarInt()
            require(count >= 0) { "Invalid Composite SlotDisplay size: $count" }
            val options = List(count) { this.readSlotDisplay() }
            SlotDisplay.Composite(options)
        }

        else -> error("Unknown SlotDisplay type ID: $typeId")
    }
}


internal fun BytesBuffer.writeSlotDisplay(slotDisplay: SlotDisplay) {
    when (slotDisplay) {
        is SlotDisplay.Empty -> {
            this.writeVarInt(0)
        }

        is SlotDisplay.AnyFuel -> {
            this.writeVarInt(1)
        }

        is SlotDisplay.WithAnyPotion -> {
            this.writeVarInt(2)
        }

        is SlotDisplay.OnlyWithComponent -> {
            this.writeVarInt(3)
            this.writeSlotDisplay(slotDisplay.base)
            this.writeVarInt(slotDisplay.componentTypeId)
        }

        is SlotDisplay.Item -> {
            this.writeVarInt(4)
            this.writeVarInt(slotDisplay.itemTypeId)
        }

        is SlotDisplay.ItemStack -> {
            this.writeVarInt(5)
            this.writeSlot(slotDisplay.itemStack)
        }

        is SlotDisplay.Tag -> {
            this.writeVarInt(6)
            this.writeIdentifier(slotDisplay.tagName)
        }

        is SlotDisplay.Dyed -> {
            this.writeVarInt(7)
            this.writeSlotDisplay(slotDisplay.dye)
            this.writeSlotDisplay(slotDisplay.target)
        }

        is SlotDisplay.SmithingTrim -> {
            this.writeVarInt(8)
            this.writeSlotDisplay(slotDisplay.base)
            this.writeSlotDisplay(slotDisplay.material)
            this.writeVarInt(slotDisplay.pattern)
        }

        is SlotDisplay.WithRemainder -> {
            this.writeVarInt(9)
            this.writeSlotDisplay(slotDisplay.ingredient)
            this.writeSlotDisplay(slotDisplay.remainder)
        }

        is SlotDisplay.Composite -> {
            this.writeVarInt(10)
            this.writeVarInt(slotDisplay.options.size)
            for (option in slotDisplay.options) {
                this.writeSlotDisplay(option)
            }
        }
    }
}