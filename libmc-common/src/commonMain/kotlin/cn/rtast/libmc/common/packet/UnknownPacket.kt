/*
 * Copyright © 2026 RTAkland
 * Author: RTAkland
 * Date: 2026/9/5
 */


package cn.rtast.libmc.common.packet

/**
 * reserved packet
 */
public data class UnknownPacket(val packetId: Int, val data: ByteArray) : MinecraftPacket {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || this::class != other::class) return false

        other as UnknownPacket

        if (packetId != other.packetId) return false
        if (!data.contentEquals(other.data)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = packetId
        result = 31 * result + data.contentHashCode()
        return result
    }
}