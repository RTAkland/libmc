/*
 * Copyright © 2026 RTAkland
 * Author: RTAkland
 * Date: 2026/9/10
 */


package cn.rtast.libmc.protocol.protocol.game.data.component.attributes

import cn.rtast.libmc.protocol.protocol.game.Identifier

public data class AttributeModifierEntry(
    val attributeId: Int,
    val modifierId: Identifier,
    val value: Double,
    val operation: AttributeOperation,
    val slot: AttributeSlot,
)
