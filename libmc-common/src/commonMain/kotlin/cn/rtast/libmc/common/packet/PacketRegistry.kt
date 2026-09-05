/*
 * Copyright © 2026 RTAkland
 * Author: RTAkland
 * Date: 2026/9/5
 */


package cn.rtast.libmc.common.packet

import cn.rtast.libmc.common.BytesBuffer
import cn.rtast.libmc.common.PacketCodec
import cn.rtast.libmc.common.writeVarInt
import kotlin.reflect.KClass

public class PacketRegistry {
    private val idToCodec = mutableMapOf<Int, PacketCodec<out MinecraftPacket>>()
    private val classToInfo = mutableMapOf<KClass<out MinecraftPacket>, RegisteredPacket<*>>()

    private data class RegisteredPacket<P : MinecraftPacket>(
        val id: Int, val codec: PacketCodec<P>,
    )

    public fun <T : MinecraftPacket> register(id: Int, kClass: KClass<T>, codec: PacketCodec<T>) {
        idToCodec[id] = codec
        classToInfo[kClass] = RegisteredPacket(id, codec)
    }

    public inline fun <reified T : MinecraftPacket> register(id: Int, codec: PacketCodec<T>) {
        register(id, T::class, codec)
    }

    public fun decodePacket(packetId: Int, buffer: BytesBuffer): MinecraftPacket {
        val codec = idToCodec[packetId]
        if (codec != null) return codec.decode(buffer) else {
//            println("Ignored unknown packet 0x${packetId.toString(16).uppercase()}")
            return UnknownPacket(packetId, buffer.readBytes(buffer.remaining.toInt()))
        }
    }

    public fun <T : MinecraftPacket> encodePacket(buffer: BytesBuffer, packet: T) {
        @Suppress("UNCHECKED_CAST")
        val info = requireNotNull(classToInfo[packet::class]) {
            "Unregistered Packet ${packet::class.simpleName}"
        } as RegisteredPacket<T>
        buffer.writeVarInt(info.id)
        info.codec.encode(buffer, packet)
    }
}