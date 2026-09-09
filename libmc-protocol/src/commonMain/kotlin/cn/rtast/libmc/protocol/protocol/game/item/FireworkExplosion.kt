/*
 * Copyright © 2026 RTAkland
 * Author: RTAkland
 * Date: 2026/9/9
 */


package cn.rtast.libmc.protocol.protocol.game.item

public data class FireworkExplosion(
    val shape: FireworkExplosionShape,
    val colors: IntArray,
    val fadeColors: IntArray,
    val hasTrail: Boolean,
    val hasTwinkle: Boolean,
) {
    public enum class FireworkExplosionShape(public val id: Int) {
        SmallBall(0), LargeBall(1), Star(2), Creeper(3), Burst(4);

        public companion object {
            public fun fromID(id: Int): FireworkExplosionShape = entries.first { it.id == id }
        }
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || this::class != other::class) return false
        other as FireworkExplosion
        if (hasTrail != other.hasTrail) return false
        if (hasTwinkle != other.hasTwinkle) return false
        if (shape != other.shape) return false
        if (!colors.contentEquals(other.colors)) return false
        if (!fadeColors.contentEquals(other.fadeColors)) return false
        return true
    }

    override fun hashCode(): Int {
        var result = hasTrail.hashCode()
        result = 31 * result + hasTwinkle.hashCode()
        result = 31 * result + shape.hashCode()
        result = 31 * result + colors.contentHashCode()
        result = 31 * result + fadeColors.contentHashCode()
        return result
    }
}