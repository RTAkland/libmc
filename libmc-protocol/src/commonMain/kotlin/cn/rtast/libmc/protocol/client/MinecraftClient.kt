/*
 * Copyright © 2026 RTAkland
 * Author: RTAkland
 * Date: 2026/9/5
 */

package cn.rtast.libmc.protocol.client

import cn.rtast.libmc.crypto.ProtocolContext
import cn.rtast.libmc.crypto.ProtocolContextBuilder
import cn.rtast.libmc.protocol.network.NetworkChannel
import cn.rtast.libmc.protocol.packet.handshake.ServerboundHandshakePacket
import cn.rtast.libmc.protocol.packet.login.serverbound.ServerboundLoginStartPacket
import cn.rtast.libmc.protocol.protocol.state.HandshakeIntent
import cn.rtast.libmc.protocol.protocol.state.ProtocolState
import cn.rtast.libmc.protocol.util.TransactionIdManager
import cn.rtast.libmc.protocol.util.generateOfflineUuid
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext
import kotlin.uuid.Uuid

public class MinecraftClient internal constructor(
    private val host: String,
    private val port: Int,
    private val username: String,
    internal val uuid: Uuid,
    internal val accessToken: String?,
    parentJob: Job?,
    private val ioDispatcher: CoroutineDispatcher,
    internal val protocolContext: ProtocolContext,
) : PacketEventDispatcher(), CoroutineScope {
    internal val stateMachine = ClientStateMachine()
    public val networkChannel: NetworkChannel = NetworkChannel(host, port, stateMachine, this, protocolContext)
    private val clientJob = SupervisorJob(parentJob)
    private var listenJob: Job? = null

    public val isOnlineMode: Boolean = accessToken != null

    public val transactionManager: TransactionIdManager = TransactionIdManager()
    public val clientTickingLoop: ClientTickingLoop = ClientTickingLoop(this)

    /**
     * Register a client ticking event callback.
     * NOTE: Blocking operations will **block** the bot thread.
     * Using #launch to avoid blocking.
     */
    public fun onTick(action: suspend (Long) -> Unit): Unit = run { clientTickingLoop.registerListener(action) }

    public suspend fun connect(protocolVersion: Int = CURRENT_MINECRAFT_PROTOCOL_VERSION) {
        networkChannel.connect()
        startListening()
        clientTickingLoop.start()
        networkChannel.sendPacket(
            ServerboundHandshakePacket(
                protocolVersion,
                host, port.toUShort(),
                HandshakeIntent.LOGIN
            )
        )
        stateMachine.transitionTo(ProtocolState.LOGIN)
        networkChannel.sendPacket(ServerboundLoginStartPacket(username, uuid))
    }

    public fun setCompression(threshold: Int): Unit = networkChannel.setCompression(threshold)

    private fun startListening() {
        listenJob = launch {
            try {
                while (isActive) networkChannel.readNextPacket()
            } catch (e: Exception) {
                if (e is CancellationException) throw e
                if (isActive) {
                    e.printStackTrace()
                    println("Network read loop exception: ${e.message}")
                    close()
                }
            }
        }
    }

    public fun close() {
        networkChannel.close()
        clientTickingLoop.stop()
        clientJob.cancel()
    }

    public override val coroutineContext: CoroutineContext
        get() = clientJob + ioDispatcher + CoroutineName("LibMC-MinecraftClient-$username")
}

public fun createMinecraftClient(
    host: String,
    port: Int = 25565,
    username: String,
    uuid: Uuid = generateOfflineUuid(username),
    accessToken: String?,
    parentJob: Job? = null,
    ioDispatcher: CoroutineDispatcher = Dispatchers.IO,
    context: ProtocolContextBuilder.() -> Unit,
): MinecraftClient {
    val context = ProtocolContextBuilder(accessToken != null).apply(context).build()
    return MinecraftClient(
        host = host,
        port = port,
        username = username,
        uuid = uuid,
        accessToken = accessToken,
        parentJob = parentJob,
        ioDispatcher = ioDispatcher,
        protocolContext = context
    )
}

internal const val CURRENT_MINECRAFT_PROTOCOL_VERSION: Int = 776