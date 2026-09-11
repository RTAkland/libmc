/*
 * Copyright © 2026 RTAkland
 * Author: RTAkland
 * Date: 2026/9/11
 */


package cn.rtast.libmc.protocol.registry

import cn.rtast.libmc.network.BytesBuffer
import cn.rtast.libmc.protocol.protocol.game.item.slot.SlotDisplayData

internal object SlotDisplayDataRegistry : ArrayIndexedRegistry<SlotDisplayData>("SlotDisplayRegistry") {
    init {
        register(SlotDisplayData.Empty)
        register(SlotDisplayData.AnyFuel)
        register(SlotDisplayData.WithAnyPotion)
        register(SlotDisplayData.OnlyWithComponent)
        register(SlotDisplayData.Item)
        register(SlotDisplayData.ItemStack)
        register(SlotDisplayData.Tags)
        register(SlotDisplayData.Dyed)
        register(SlotDisplayData.SmithingTrim)
        register(SlotDisplayData.WithRemainder)
        register(SlotDisplayData.Composite)

        freeze()
    }
}

internal fun BytesBuffer.readSlotDisplayData(typeId: Int): SlotDisplayData = SlotDisplayDataRegistry.read(typeId, this)
internal fun BytesBuffer.writeSlotDisplayData(display: SlotDisplayData) = SlotDisplayDataRegistry.write(this, display)