/*
 * Copyright © 2026 RTAkland
 * Author: RTAkland
 * Date: 2026/9/10
 */


package cn.rtast.libmc.protocol.protocol.game.data.component.attributes

import cn.rtast.libmc.network.BytesBuffer
import cn.rtast.libmc.primitives.readPrefixed
import cn.rtast.libmc.primitives.readVarInt
import cn.rtast.libmc.primitives.writePrefixed
import cn.rtast.libmc.primitives.writeVarInt

public data class FireworkExplosionAttribute(
    val shape: FireworkExplosionShape,
    val colors: List<Int>,
    val fadeColors: List<Int>,
    val hasTrail: Boolean,
    val hasTwinkle: Boolean,
)

public enum class FireworkExplosionShape(public val id: Int) {
    SmallBall(0), LargeBall(1), Star(2), Creeper(3), Burst(4);

    public companion object {
        public fun fromID(id: Int): FireworkExplosionShape = entries.first { it.id == id }
    }
}

internal fun BytesBuffer.readFireworkExplosion(): FireworkExplosionAttribute {
    val shape = FireworkExplosionShape.Companion.fromID(readVarInt())
    val colors = readPrefixed { readInt() }
    val fadeColors = readPrefixed { readInt() }
    val hasTrail = readBoolean()
    val hasTwinkle = readBoolean()
    return FireworkExplosionAttribute(shape, colors, fadeColors, hasTrail, hasTwinkle)
}

internal fun BytesBuffer.writeFireworkExplosion(explosion: FireworkExplosionAttribute) {
    writeVarInt(explosion.shape.id)
    writePrefixed(explosion.colors) { writeInt(it) }
    writePrefixed(explosion.fadeColors) { writeInt(it) }
    writeBoolean(explosion.hasTrail)
    writeBoolean(explosion.hasTwinkle)
}