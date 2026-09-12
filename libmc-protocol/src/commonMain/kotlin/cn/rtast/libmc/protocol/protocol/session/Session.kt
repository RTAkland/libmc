/*
 * Copyright © 2026 RTAkland
 * Author: RTAkland
 * Date: 2026/9/10
 */


package cn.rtast.libmc.protocol.protocol.session

import cn.rtast.libmc.packet.MinecraftPacket
import cn.rtast.libmc.protocol.client.CURRENT_MINECRAFT_PROTOCOL_VERSION
import cn.rtast.libmc.protocol.protocol.state.HandshakeIntent
import kotlin.reflect.KClass

public interface Session {
    @Suppress("FunctionName")
    public fun <T : SessionEvent> _onEvent(clazz: KClass<T>, block: Session.(T) -> Unit)
    public fun emitEvent(event: SessionEvent): Unit?
    public fun login(protocolVersion: Int = CURRENT_MINECRAFT_PROTOCOL_VERSION)
    public fun handshake(
        protocolVersion: Int = CURRENT_MINECRAFT_PROTOCOL_VERSION,
        intent: HandshakeIntent = HandshakeIntent.LOGIN,
    )

    public fun status(protocolVersion: Int = CURRENT_MINECRAFT_PROTOCOL_VERSION): String
    public fun disconnect()
    public fun init()
    public fun sendPacket(packet: MinecraftPacket)
}

public inline fun <reified T : SessionEvent> Session.onEvent(noinline block: Session.(T) -> Unit) {
    _onEvent(T::class, block)
}