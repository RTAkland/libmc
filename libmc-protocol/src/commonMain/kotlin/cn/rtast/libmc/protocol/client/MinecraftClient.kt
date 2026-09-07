/*
 * Copyright © 2026 RTAkland
 * Author: RTAkland
 * Date: 2026/9/5
 */

package cn.rtast.libmc.protocol.client

import cn.rtast.libmc.LibMCContext
import cn.rtast.libmc.crypto.ProtocolContext
import cn.rtast.libmc.crypto.ProtocolContextBuilder
import cn.rtast.libmc.protocol.event.InternalPacketDispatcher
import cn.rtast.libmc.protocol.event.PacketEventDispatcher
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
    private val port: Int = 25565,
    private val username: String,
    internal val uuid: Uuid,
    internal val accessToken: String?,
    context: LibMCContext,
    parentJob: Job?,
    private val ioDispatcher: CoroutineDispatcher,
    cryptoContext: ProtocolContext,
) : PacketEventDispatcher(), CoroutineScope {
    internal val rsa1024Encryptor = cryptoContext.rsaEncryptor
    internal val serverIdHasher = cryptoContext.sha1Hasher
    internal val authProvider = cryptoContext.authProvider
    internal val stateMachine = ClientStateMachine()

    public val networkChannel: NetworkChannel = NetworkChannel(
        host, port, context, stateMachine, cryptoContext.cipherFactory, this
    )

    private val internalPacketDispatcher = InternalPacketDispatcher(this, authProvider)
    private val clientJob = SupervisorJob(parentJob)
    private var listenJob: Job? = null

    public val isOnlineMode: Boolean get() = accessToken != null
    public val transactionManager: TransactionIdManager = TransactionIdManager()

    public suspend fun connect(protocolVersion: Int = 776) {
        networkChannel.connect()
        startListening()
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
                while (isActive) internalPacketDispatcher.handleIncomingPackets(networkChannel.readNextPacket())
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
    context: LibMCContext = LibMCContext(),
    parentJob: Job? = null,
    ioDispatcher: CoroutineDispatcher = Dispatchers.IO,
    crypto: ProtocolContextBuilder.() -> Unit,
): MinecraftClient {
    val cryptoContext = ProtocolContextBuilder(accessToken != null).apply(crypto).build()
    return MinecraftClient(
        host = host,
        port = port,
        username = username,
        uuid = uuid,
        accessToken = accessToken,
        context = context,
        parentJob = parentJob,
        ioDispatcher = ioDispatcher,
        cryptoContext = cryptoContext
    )
}