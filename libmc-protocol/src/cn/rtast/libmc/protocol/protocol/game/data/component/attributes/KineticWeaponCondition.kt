/*
 * Copyright © 2026 RTAkland
 * Author: RTAkland
 * Date: 2026/9/10
 */


package cn.rtast.libmc.protocol.protocol.game.data.component.attributes

import cn.rtast.libmc.network.BytesBuffer
import cn.rtast.libmc.primitives.readVarInt
import cn.rtast.libmc.primitives.writeVarInt

public data class KineticWeaponCondition(
    val maxDurationTicks: Int,
    val minSpeed: Float,
    val minRelativeSpeed: Float,
)

internal fun BytesBuffer.readKineticWeaponCondition(): KineticWeaponCondition {
    val duration = readVarInt()
    val minSpeed = readFloat()
    val minRelativeSpeed = readFloat()
    return KineticWeaponCondition(duration, minSpeed, minRelativeSpeed)
}

internal fun BytesBuffer.writeKineticWeaponCondition(condition: KineticWeaponCondition) {
    writeVarInt(condition.maxDurationTicks)
    writeFloat(condition.minSpeed)
    writeFloat(condition.minRelativeSpeed)
}