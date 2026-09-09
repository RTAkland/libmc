/*
 * Copyright © 2026 RTAkland
 * Author: RTAkland
 * Date: 2026/9/9
 */


package cn.rtast.libmc.protocol.protocol.game.enchantement

import cn.rtast.libmc.network.BytesBuffer
import cn.rtast.libmc.primitives.readVarInt
import cn.rtast.libmc.primitives.writeVarInt

public data class ItemEnchantment(val enchantmentId: Int, val level: Int)

internal fun BytesBuffer.readItemEnchantment(): ItemEnchantment {
    val id = readVarInt()
    val level = readVarInt()
    return ItemEnchantment(id, level)
}

internal fun BytesBuffer.writeItemEnchantment(enchantment: ItemEnchantment) {
    writeVarInt(enchantment.enchantmentId)
    writeVarInt(enchantment.level)
}