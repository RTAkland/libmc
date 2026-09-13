/*
 * Copyright © 2026 RTAkland
 * Author: RTAkland
 * Date: 2026/9/4
 */


package cn.rtast.libmc.protocol.protocol.state

import kotlin.jvm.JvmInline

@JvmInline
public value class HandshakeIntent internal constructor(public val intentID: Int) {
    public companion object {
        public val STATUS: HandshakeIntent = HandshakeIntent(1)
        public val LOGIN: HandshakeIntent = HandshakeIntent(2)
        public val TRANSFER: HandshakeIntent = HandshakeIntent(3)

        public fun fromID(intentID: Int): HandshakeIntent = when (intentID) {
            1 -> STATUS; 2 -> LOGIN; 3 -> TRANSFER
            else -> throw IllegalArgumentException("Unknown Handshake Intent ID")
        }
    }
}