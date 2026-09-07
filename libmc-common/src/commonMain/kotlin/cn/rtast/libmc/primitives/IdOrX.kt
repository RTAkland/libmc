/*
 * Copyright © 2026 RTAkland
 * Author: RTAkland
 * Date: 2026/9/7
 */


package cn.rtast.libmc.primitives

import cn.rtast.libmc.network.BytesBuffer

public sealed interface IdOrX<out T> {
    public data class Inline<T>(val value: T) : IdOrX<T>
    public data class Reference(val registryId: Int) : IdOrX<Nothing>
}

public inline fun <T> BytesBuffer.writeIdOrX(value: IdOrX<T>, writeX: BytesBuffer.(T) -> Unit) {
    when (value) {
        is IdOrX.Inline -> {
            this.writeVarInt(0)
            this.writeX(value.value)
        }

        is IdOrX.Reference -> this.writeVarInt(value.registryId + 1)
    }
}

public inline fun <T> BytesBuffer.readIdOrX(readX: BytesBuffer.() -> T): IdOrX<T> {
    val id = this.readVarInt()
    return if (id == 0) IdOrX.Inline(this.readX()) else IdOrX.Reference(id - 1)
}