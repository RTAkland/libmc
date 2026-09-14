/*
 * Copyright © 2026 RTAkland
 * Author: RTAkland
 * Date: 2026/9/7
 */


package cn.rtast.libmc.protocol.primitives

import cn.rtast.libmc.network.BytesBuffer
import cn.rtast.libmc.primitives.readVarInt
import cn.rtast.libmc.primitives.writeVarInt
import cn.rtast.libmc.protocol.protocol.game.Identifier
import cn.rtast.libmc.protocol.protocol.game.readIdentifier
import cn.rtast.libmc.protocol.protocol.game.writeIdentifier

public sealed interface IdSet {
    public data class Tag(val tag: Identifier) : IdSet
    public data class Entries(val ids: List<Int>) : IdSet
}

public fun BytesBuffer.readIdSet(): IdSet {
    val type = this.readVarInt()
    return if (type == 0) IdSet.Tag(tag = this.readIdentifier()) else {
        val count = type - 1
        val ids = ArrayList<Int>(count)
        (0 until count).forEach { _ -> ids.add(this.readVarInt()) }
        IdSet.Entries(ids)
    }
}

public fun BytesBuffer.writeIdSet(idSet: IdSet) {
    when (idSet) {
        is IdSet.Tag -> {
            this.writeVarInt(0)
            this.writeIdentifier(idSet.tag)
        }

        is IdSet.Entries -> {
            this.writeVarInt(idSet.ids.size + 1)
            for (id in idSet.ids) this.writeVarInt(id)
        }
    }
}