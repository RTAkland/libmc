/*
 * Copyright © 2026 RTAkland
 * Author: RTAkland
 * Date: 2026/9/9
 */


package cn.rtast.libmc.protocol.protocol.game.block

import cn.rtast.libmc.nbt.NBTCompound
import cn.rtast.libmc.network.BytesBuffer
import cn.rtast.libmc.primitives.IdSet
import cn.rtast.libmc.primitives.readIdSet
import cn.rtast.libmc.primitives.readPrefixOptional
import cn.rtast.libmc.primitives.readPrefixed
import cn.rtast.libmc.protocol.protocol.game.component.DataProperty
import cn.rtast.libmc.protocol.protocol.game.component.ExactDataComponentMatcher
import cn.rtast.libmc.protocol.protocol.game.component.PartialDataComponentMatcher
import cn.rtast.libmc.protocol.protocol.game.component.readDataProperty
import cn.rtast.libmc.protocol.protocol.util.readNetworkNBTCompound

public data class BlockPredicate(
    val blocks: IdSet?,
    val properties: List<DataProperty>?,
    val nbt: NBTCompound?,
    val dataComponents: List<ExactDataComponentMatcher>,
    val partialDataComponentPredicates: List<PartialDataComponentMatcher>,
)

internal fun BytesBuffer.readBlockPredicate(): BlockPredicate {
    val blocks = readPrefixOptional { readIdSet() }
    val properties = readPrefixOptional { readPrefixed { readDataProperty() } }
    val nbt = readPrefixOptional { readNetworkNBTCompound() }
    val dataComponents = readPrefixed {  }
}