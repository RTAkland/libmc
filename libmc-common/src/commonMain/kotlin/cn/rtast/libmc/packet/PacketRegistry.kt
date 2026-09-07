/*
 * Copyright © 2026 RTAkland
 * Author: RTAkland
 * Date: 2026/9/5
 */


package cn.rtast.libmc.packet

import cn.rtast.libmc.primitives.writeVarInt
import cn.rtast.libmc.stream.BytesBuffer
import kotlin.reflect.KClass

public class PacketRegistry {
    private val idToCodec = mutableMapOf<Int, PacketCodec<out MinecraftPacket>>()
    private val classToInfo = mutableMapOf<KClass<out MinecraftPacket>, RegisteredPacket<*>>()

    private data class RegisteredPacket<P : MinecraftPacket>(val id: Int, val codec: PacketCodec<P>)

    public fun <T : MinecraftPacket> register(id: Int, kClass: KClass<T>, codec: PacketCodec<T>) {
        idToCodec[id] = codec
        classToInfo[kClass] = RegisteredPacket(id, codec)
    }

    public inline fun <reified T : MinecraftPacket> register(id: Int, codec: PacketCodec<T>) {
        register(id, T::class, codec)
    }

    public suspend fun decodePacket(packetId: Int, buffer: BytesBuffer): MinecraftPacket =
        idToCodec[packetId]?.decode(buffer) ?: ClientboundUnknownPacket(packetId, buffer.readRemainingBytes())

    public suspend fun <T : MinecraftPacket> encodePacket(buffer: BytesBuffer, packet: T) {
        @Suppress("UNCHECKED_CAST")
        val info = requireNotNull(classToInfo[packet::class]) {
            "Unregistered Packet ${packet::class.simpleName}"
        } as RegisteredPacket<T>
        buffer.writeVarInt(info.id)
        info.codec.encode(buffer, packet)
    }
}