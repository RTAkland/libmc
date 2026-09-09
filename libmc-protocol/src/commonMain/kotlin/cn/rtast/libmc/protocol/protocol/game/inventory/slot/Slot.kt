/*
 * Copyright © 2026 RTAkland
 * Author: RTAkland
 * Date: 2026/9/9
 */


package cn.rtast.libmc.protocol.protocol.game.inventory.slot

import cn.rtast.libmc.protocol.protocol.game.component.DataComponent

public data class Slot(
    val count: Int,
    val itemId: Int = 0,
    val componentsToAdd: List<TypedDataComponent> = emptyList(),
    val componentsToRemove: List<Int> = emptyList(),
) {
    public data class TypedDataComponent(val typeId: Int, val data: DataComponent)

    public val isEmpty: Boolean get() = count <= 0

    public companion object {
        public val EMPTY: Slot = Slot(count = 0)
    }
}
