/*
 * Copyright © 2026 RTAkland
 * Author: RTAkland
 * Date: 2026/9/6
 */


package cn.rtast.libmc.protocol.protocol.game.entity.attributes

public data class EntityAttributeProperty(
    val attributeId: Int,
    val value: Double,
    val modifiers: List<AttributeModifierData>,
)