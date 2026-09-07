/*
 * Copyright © 2026 RTAkland
 * Author: RTAkland
 * Date: 2026/9/7
 */


package cn.rtast.libmc.primitives

import cn.rtast.libmc.stream.BytesBuffer

public sealed class Either<out L, out R> {
    public data class Left<out L>(val value: L) : Either<L, Nothing>()
    public data class Right<out R>(val value: R) : Either<Nothing, R>()

    public val isLeft: Boolean get() = this is Left
    public val isRight: Boolean get() = this is Right
}

public suspend inline fun <L, R> BytesBuffer.readEither(
    readLeft: BytesBuffer.() -> L,
    readRight: BytesBuffer.() -> R,
): Either<L, R> {
    val isLeft = readBoolean()
    return if (isLeft) Either.Left(readLeft(this)) else Either.Right(readRight(this))
}

public suspend inline fun <L, R> BytesBuffer.writeEither(
    either: Either<L, R>,
    writeLeft: BytesBuffer.(L) -> Unit,
    writeRight: BytesBuffer.(R) -> Unit,
) {
    when (either) {
        is Either.Left -> {
            writeBoolean(true)
            writeLeft(this, either.value)
        }

        is Either.Right -> {
            writeBoolean(false)
            writeRight(this, either.value)
        }
    }
}