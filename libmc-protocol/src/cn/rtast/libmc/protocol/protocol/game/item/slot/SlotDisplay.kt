/*
 * Copyright © 2026 RTAkland
 * Author: RTAkland
 * Date: 2026/9/11
 */


package cn.rtast.libmc.protocol.protocol.game.item.slot

import cn.rtast.libmc.network.BytesBuffer
import cn.rtast.libmc.primitives.readVarInt
import cn.rtast.libmc.primitives.writeVarInt
import cn.rtast.libmc.protocol.registry.readSlotDisplayData
import cn.rtast.libmc.protocol.registry.writeSlotDisplayData

public data class SlotDisplay(val itemDisplayType: Int, val data: SlotDisplayData)

internal fun BytesBuffer.readSlotDisplay(): SlotDisplay {
    val typeId = readVarInt()
    val data = readSlotDisplayData(typeId)
    return SlotDisplay(typeId, data)
}

internal fun BytesBuffer.writeSlotDisplay(slot: SlotDisplay) {
    writeVarInt(slot.itemDisplayType)
    writeSlotDisplayData(slot.data)
}