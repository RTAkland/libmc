/*
 * Copyright © 2026 RTAkland
 * Author: RTAkland
 * Date: 2026/9/5
 */

package cn.rtast.libmc.protocol.protocol.event

import cn.rtast.libmc.packet.MinecraftPacket
import cn.rtast.libmc.protocol.protocol.PacketDirection
import cn.rtast.libmc.protocol.protocol.session.Session
import cn.rtast.libmc.protocol.threads.coroutines.Lock
import cn.rtast.libmc.protocol.threads.coroutines.withLock
import kotlin.reflect.KClass

private typealias Handler = Session.(MinecraftPacket) -> Unit
private typealias DirectionalHandler = Session.(MinecraftPacket, PacketDirection) -> Unit

public abstract class PacketEventDispatcher {
    private val lock = Lock()

    @PublishedApi
    internal var receiveHandlers: Map<KClass<out MinecraftPacket>, List<Handler>> = emptyMap()

    @PublishedApi
    internal var sentHandlers: Map<KClass<out MinecraftPacket>, List<Handler>> = emptyMap()

    @PublishedApi
    internal var globalHandlers: List<DirectionalHandler> = emptyList()

    @PublishedApi
    internal fun <T : MinecraftPacket> addTypedHandler(
        isReceive: Boolean,
        key: KClass<T>,
        handler: Handler,
    ): ListenerRegistration {
        lock.withLock {
            if (isReceive) {
                val current = receiveHandlers[key] ?: emptyList()
                receiveHandlers = receiveHandlers + (key to (current + handler))
            } else {
                val current = sentHandlers[key] ?: emptyList()
                sentHandlers = sentHandlers + (key to (current + handler))
            }
        }
        return ListenerRegistration { removeTypedHandler(isReceive, key, handler) }
    }

    @PublishedApi
    internal fun <T : MinecraftPacket> removeTypedHandler(
        isReceive: Boolean,
        key: KClass<T>,
        handler: Handler,
    ) {
        lock.withLock {
            if (isReceive) {
                val current = receiveHandlers[key] ?: return@withLock
                val updated = current - handler
                receiveHandlers = if (updated.isEmpty()) receiveHandlers - key else receiveHandlers + (key to updated)
            } else {
                val current = sentHandlers[key] ?: return@withLock
                val updated = current - handler
                sentHandlers = if (updated.isEmpty()) sentHandlers - key else sentHandlers + (key to updated)
            }
        }
    }

    private fun dispatch(
        session: Session,
        isReceive: Boolean,
        packet: MinecraftPacket,
        direction: PacketDirection,
    ) {
        val (typeHandlers, globals) = lock.withLock {
            val map = if (isReceive) receiveHandlers else sentHandlers
            Pair(map[packet::class], globalHandlers)
        }
        typeHandlers?.forEach { handler -> handler(session, packet) }
        globals.forEach { handler -> handler(session, packet, direction) }
    }

    internal fun dispatchReceive(packet: MinecraftPacket, session: Session) =
        dispatch(session, true, packet, PacketDirection.CLIENTBOUND)

    internal fun dispatchSent(packet: MinecraftPacket, session: Session) =
        dispatch(session, false, packet, PacketDirection.SERVERBOUND)

    public inline fun <reified T : MinecraftPacket> onPacket(noinline block: Session.(T) -> Unit): ListenerRegistration {
        val handler: Handler = { block(it as T) }
        return addTypedHandler(true, T::class, handler)
    }

    public inline fun <reified T : MinecraftPacket> onSent(noinline block: Session.(T) -> Unit): ListenerRegistration {
        val handler: Handler = { block(it as T) }
        return addTypedHandler(false, T::class, handler)
    }

    public fun on(block: Session.(packet: MinecraftPacket, direction: PacketDirection) -> Unit): ListenerRegistration {
        lock.withLock { globalHandlers = globalHandlers + block }
        return ListenerRegistration { lock.withLock { globalHandlers = globalHandlers - block } }
    }
}