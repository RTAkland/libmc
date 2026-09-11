/*
 * Copyright © 2026 RTAkland
 * Author: RTAkland
 * Date: 2026/9/11
 */


package cn.rtast.libmc.protocol.protocol.game.item.slot

import cn.rtast.libmc.network.BytesBuffer
import cn.rtast.libmc.packet.PacketCodec
import cn.rtast.libmc.primitives.readVarInt
import cn.rtast.libmc.primitives.writePrefixed
import cn.rtast.libmc.primitives.writeVarInt
import cn.rtast.libmc.protocol.protocol.game.Identifier
import cn.rtast.libmc.protocol.protocol.game.readIdentifier
import cn.rtast.libmc.protocol.protocol.game.writeIdentifier

public sealed interface SlotDisplayData {
    public data object Empty : SlotDisplayData, PacketCodec<Empty> {
        override fun encode(buffer: BytesBuffer, value: Empty) {}
        override fun decode(buffer: BytesBuffer): Empty = Empty
    }

    public data object AnyFuel : SlotDisplayData, PacketCodec<AnyFuel> {
        override fun encode(buffer: BytesBuffer, value: AnyFuel) {}
        override fun decode(buffer: BytesBuffer): AnyFuel = AnyFuel
    }

    public data class WithAnyPotion(val base: SlotDisplay) : SlotDisplayData {
        internal companion object Codec : PacketCodec<WithAnyPotion> {
            override fun encode(buffer: BytesBuffer, value: WithAnyPotion) {
                buffer.writeSlotDisplay(value.base)
            }

            override fun decode(buffer: BytesBuffer): WithAnyPotion = WithAnyPotion(buffer.readSlotDisplay())
        }
    }

    public data class OnlyWithComponent(val base: SlotDisplay, val componentTypeId: Int) : SlotDisplayData {
        internal companion object Codec : PacketCodec<OnlyWithComponent> {
            override fun encode(buffer: BytesBuffer, value: OnlyWithComponent) {
                buffer.writeSlotDisplay(value.base)
                buffer.writeVarInt(value.componentTypeId)
            }

            override fun decode(buffer: BytesBuffer): OnlyWithComponent {
                val base = buffer.readSlotDisplay()
                val componentTypeId = buffer.readVarInt()
                return OnlyWithComponent(base, componentTypeId)
            }
        }
    }

    public data class Item(val itemType: Int) : SlotDisplayData {
        internal companion object Codec : PacketCodec<Item> {
            override fun encode(buffer: BytesBuffer, value: Item) {
                buffer.writeVarInt(value.itemType)
            }

            override fun decode(buffer: BytesBuffer): Item = Item(buffer.readVarInt())
        }
    }

    public data class ItemStack(val item: Slot) : SlotDisplayData {
        internal companion object Codec : PacketCodec<ItemStack> {
            override fun encode(buffer: BytesBuffer, value: ItemStack) {
                buffer.writeSlot(value.item)
            }

            override fun decode(buffer: BytesBuffer): ItemStack = ItemStack(buffer.readSlot())
        }
    }

    public data class Tag(val tag: Identifier) : SlotDisplayData {
        internal companion object Codec : PacketCodec<Tag> {
            override fun encode(buffer: BytesBuffer, value: Tag) {
                buffer.writeIdentifier(value.tag)
            }

            override fun decode(buffer: BytesBuffer): Tag = Tag(buffer.readIdentifier())
        }
    }

    public data class Dyed(val dye: SlotDisplay, val target: SlotDisplay) : SlotDisplayData {
        internal companion object Codec : PacketCodec<Dyed> {
            override fun encode(buffer: BytesBuffer, value: Dyed) {
                buffer.writeSlotDisplay(value.dye)
                buffer.writeSlotDisplay(value.target)
            }

            override fun decode(buffer: BytesBuffer): Dyed {
                val dye = buffer.readSlotDisplay()
                val target = buffer.readSlotDisplay()
                return Dyed(dye, target)
            }
        }
    }

    public data class SmithingTrim(val base: SlotDisplay, val material: SlotDisplay, val pattern: Int) :
        SlotDisplayData {
        internal companion object Codec : PacketCodec<SmithingTrim> {
            override fun encode(buffer: BytesBuffer, value: SmithingTrim) {
                buffer.writeSlotDisplay(value.base)
                buffer.writeSlotDisplay(value.material)
                buffer.writeVarInt(value.pattern)
            }

            override fun decode(buffer: BytesBuffer): SmithingTrim {
                val base = buffer.readSlotDisplay()
                val material = buffer.readSlotDisplay()
                val pattern = buffer.readVarInt()
                return SmithingTrim(base, material, pattern)
            }
        }
    }

    public data class WithRemainder(val ingredient: SlotDisplay, val remainder: SlotDisplay) : SlotDisplayData {
        internal companion object Codec : PacketCodec<WithRemainder> {
            override fun encode(buffer: BytesBuffer, value: WithRemainder) {
                buffer.writeSlotDisplay(value.ingredient)
                buffer.writeSlotDisplay(value.remainder)
            }

            override fun decode(buffer: BytesBuffer): WithRemainder {
                val ingredient = buffer.readSlotDisplay()
                val remainder = buffer.readSlotDisplay()
                return WithRemainder(ingredient, remainder)
            }
        }
    }

    public data class Composite(val optionCount: Int, val options: List<SlotDisplay>) : SlotDisplayData {
        internal companion object Codec : PacketCodec<Composite> {
            override fun encode(buffer: BytesBuffer, value: Composite) {
                buffer.writePrefixed(value.options) { writeSlotDisplay(it) }
            }

            override fun decode(buffer: BytesBuffer): Composite {
                val count = buffer.readVarInt()
                val options = ArrayList<SlotDisplay>(count)
                repeat(count) { options.add(buffer.readSlotDisplay()) }
                return Composite(count, options)
            }
        }
    }
}