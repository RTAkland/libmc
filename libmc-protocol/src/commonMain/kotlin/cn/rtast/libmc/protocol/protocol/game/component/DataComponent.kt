/*
 * Copyright © 2026 RTAkland
 * Author: RTAkland
 * Date: 2026/9/9
 */


package cn.rtast.libmc.protocol.protocol.game.component

import cn.rtast.libmc.nbt.NBTCompound
import cn.rtast.libmc.protocol.protocol.game.Identifier
import cn.rtast.libmc.protocol.protocol.game.chat.TextComponent
import cn.rtast.libmc.protocol.protocol.game.enchantement.ItemEnchantment

public sealed interface DataComponent {
    public data class CustomData(val nbt: NBTCompound) : DataComponent
    public data class MaxStackSize(val size: Int) : DataComponent
    public data class MaxDamage(val damage: Int) : DataComponent
    public data class Damage(val damage: Int) : DataComponent
    public object Unbreakable : DataComponent
    public data class CustomName(val name: TextComponent) : DataComponent
    public data class ItemName(val name: TextComponent) : DataComponent
    public data class ItemModel(val model: Identifier) : DataComponent
    public data class Lore(val line: List<TextComponent>) : DataComponent
    public data class Rarity(val rarity: ItemRarity) : DataComponent {
        public enum class ItemRarity(public val id: Int) {
            Common(0), Uncommon(1), Rare(2), Epic(3);

            public companion object {
                public fun fromID(id: Int): ItemRarity = entries.first { it.id == id }
            }
        }
    }

    public data class Enchantments(val enchantments: List<ItemEnchantment>) : DataComponent
    public data class Food(val nutrition: Int, val saturationModifier: Float, val canAlwaysEat: Boolean) : DataComponent
    public data class Unknown(val typeId: Int, val rawBytes: ByteArray) : DataComponent {
        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (other !is Unknown) return false
            return typeId == other.typeId && rawBytes.contentEquals(other.rawBytes)
        }

        override fun hashCode(): Int = 31 * typeId + rawBytes.contentHashCode()
    }
}