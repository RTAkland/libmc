/*
 * Copyright © 2026 RTAkland
 * Author: RTAkland
 * Date: 2026/9/5
 */


package cn.rtast.libmc.protocol.protocol.game.player

import cn.rtast.libmc.protocol.protocol.game.math.LpVec3d

public sealed class InteractType(public val id: Int) {
    public data class Interact(val hand: Hand) : InteractType(0)
    public data object Attack : InteractType(1)
    public data class InteractAt(val targetOffset: LpVec3d, val hand: Hand) : InteractType(2)
}