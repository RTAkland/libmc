/*
 * Copyright © 2026 RTAkland
 * Author: RTAkland
 * Date: 2026/9/9
 */


package cn.rtast.libmc.protocol.protocol.game.component

import cn.rtast.libmc.nbt.NBTCompound

public data class PartialDataComponentMatcher(
    val type: PartialDataComponentMatcherType,
    val predicate: NBTCompound,
) {
    public enum class PartialDataComponentMatcherType(public val id: Int) {
        Damage(0), Enchantments(1), StoredEnchantment(2), PotionContents(3),
        CustomData(4), Container(5), BundleContents(6), FireworkExplosion(7),
        Fireworks(8), WritableBookContents(9), WrittenBookContents(10),
        AttributeModifiers(12), Trim(12), JukeboxPlayable(13);

        public companion object {
            public fun fromID(id: Int): PartialDataComponentMatcherType = entries.first { it.id == id }
        }
    }
}