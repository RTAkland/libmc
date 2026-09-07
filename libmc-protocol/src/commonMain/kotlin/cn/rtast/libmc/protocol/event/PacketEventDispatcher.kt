/*
 * Copyright © 2026 RTAkland
 * Author: RTAkland
 * Date: 2026/9/5
 */

package cn.rtast.libmc.protocol.event

import cn.rtast.libmc.packet.MinecraftPacket
import cn.rtast.libmc.protocol.protocol.PacketDirection
import kotlin.concurrent.Volatile
import kotlin.reflect.KClass

private typealias Handler = suspend (MinecraftPacket) -> Unit
private typealias DirectionalHandler = suspend (MinecraftPacket, PacketDirection) -> Unit

public open class PacketEventDispatcher {
    @Volatile
    @PublishedApi
    internal var receiveHandlers: Map<KClass<out MinecraftPacket>, List<Handler>> = emptyMap()

    @Volatile
    @PublishedApi
    internal var sentHandlers: Map<KClass<out MinecraftPacket>, List<Handler>> = emptyMap()

    @Volatile
    @PublishedApi
    internal var globalHandlers: List<DirectionalHandler> = emptyList()

    @PublishedApi
    internal fun <T : MinecraftPacket> addTypedHandler(
        isReceive: Boolean,
        key: KClass<T>,
        handler: Handler,
    ) {
        if (isReceive) {
            val current = receiveHandlers[key] ?: emptyList()
            receiveHandlers = receiveHandlers + (key to (current + handler))
        } else {
            val current = sentHandlers[key] ?: emptyList()
            sentHandlers = sentHandlers + (key to (current + handler))
        }
    }

    private suspend fun dispatch(
        handlersMap: Map<KClass<out MinecraftPacket>, List<Handler>>,
        packet: MinecraftPacket,
        direction: PacketDirection,
    ) {
        handlersMap[packet::class]?.forEach { handler -> handler(packet) }
        globalHandlers.forEach { handler -> handler(packet, direction) }
    }

    internal suspend fun dispatchReceive(packet: MinecraftPacket) =
        dispatch(receiveHandlers, packet, PacketDirection.CLIENTBOUND)

    internal suspend fun dispatchSent(packet: MinecraftPacket) =
        dispatch(sentHandlers, packet, PacketDirection.SERVERBOUND)

    /**
     * Lambda will be invoked when received a packet
     */
    public inline fun <reified T : MinecraftPacket> onPacket(crossinline block: suspend (T) -> Unit) {
        addTypedHandler(true, T::class) { block(it as T) }
    }

    /**
     * Lambda will be invoked after a packet sent
     */
    public inline fun <reified T : MinecraftPacket> onSent(crossinline block: suspend (T) -> Unit) {
        addTypedHandler(false, T::class) { block(it as T) }
    }

    /**
     * All packets will be appeared here, including `Outbound(Serverbound)` and `Inbound(Clientbound)` packet
     */
    public fun on(block: suspend (packet: MinecraftPacket, direction: PacketDirection) -> Unit) {
        globalHandlers = globalHandlers + block
    }
}