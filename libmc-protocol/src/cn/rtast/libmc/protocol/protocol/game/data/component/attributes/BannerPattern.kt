/*
 * Copyright © 2026 RTAkland
 * Author: RTAkland
 * Date: 2026/9/10
 */


package cn.rtast.libmc.protocol.protocol.game.data.component.attributes

import cn.rtast.libmc.network.BytesBuffer
import cn.rtast.libmc.primitives.*
import cn.rtast.libmc.protocol.protocol.game.Identifier
import cn.rtast.libmc.protocol.protocol.game.color.DyeColor
import cn.rtast.libmc.protocol.protocol.game.readIdentifier
import cn.rtast.libmc.protocol.protocol.game.writeIdentifier

public data class BannerPattern(val assetId: Identifier, val translationKey: String)

public data class BannerPatternLayer(val pattern: IdOrX<BannerPattern>, val color: DyeColor)

internal fun BytesBuffer.readBannerPatternLayer(): BannerPatternLayer {
    val pattern = readIdOrX { readBannerPattern() }
    val color = DyeColor.fromID(readVarInt())
    return BannerPatternLayer(pattern, color)
}

internal fun BytesBuffer.writeBannerPatternLayer(layer: BannerPatternLayer) {
    writeIdOrX(layer.pattern) { writeBannerPattern(it) }
    writeVarInt(layer.color.id)
}

internal fun BytesBuffer.readBannerPattern(): BannerPattern {
    val assetId = readIdentifier()
    val translationKey = readMcString()
    return BannerPattern(assetId, translationKey)
}

internal fun BytesBuffer.writeBannerPattern(pattern: BannerPattern) {
    writeIdentifier(pattern.assetId)
    writeMcString(pattern.translationKey)
}