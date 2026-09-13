/*
 * Copyright © 2026 RTAkland
 * Author: RTAkland
 * Date: 2026/9/11
 */


package cn.rtast.libmc.protocol.protocol.game.inventory.equipment

import cn.rtast.libmc.protocol.protocol.game.item.slot.Slot

public data class EntityEquipment(val slot: EntityEquipmentSlot, val item: Slot)