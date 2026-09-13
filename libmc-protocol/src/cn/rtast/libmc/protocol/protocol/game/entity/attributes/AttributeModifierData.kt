/*
 * Copyright © 2026 RTAkland
 * Author: RTAkland
 * Date: 2026/9/6
 */


package cn.rtast.libmc.protocol.protocol.game.entity.attributes

import cn.rtast.libmc.protocol.protocol.game.Identifier

public data class AttributeModifierData(
    val id: Identifier,
    val amount: Double,
    val operation: AttributeModifierOperation,
)