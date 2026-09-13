/*
 * Copyright © 2026 RTAkland
 * Author: RTAkland
 * Date: 2026/9/11
 */


package cn.rtast.libmc.protocol.protocol.game.particle

public data class BlockParticleAlternative(
    val particleId: Int,
    val particleData: ParticleData,
    val scaling: Float,
    val speed: Float,
    val weight: Int,
)