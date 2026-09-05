///*
// * Copyright © 2026 RTAkland
// * Author: RTAkland
// * Date: 2026/9/4
// */
//
//
//package cn.rtast.libmc.protocol
//
//import cn.rtast.libmc.protocol.packet.configuration.ServerboundAckFinishConfigurationPacket
//import cn.rtast.libmc.protocol.packet.configuration.ServerboundPongPacket
//import cn.rtast.libmc.protocol.packet.configuration.ServerboundSelectKnownPacksPacket
//import cn.rtast.libmc.protocol.packet.handshake.ServerboundHandshakePacket
//import cn.rtast.libmc.protocol.packet.login.ServerboundLoginAcknowledgedPacket
//import cn.rtast.libmc.protocol.packet.login.ServerboundLoginStartPacket
//import cn.rtast.libmc.protocol.packet.play.ServerboundKeepAlivePlayPacket
//import cn.rtast.libmc.protocol.protocol.state.HandshakeIntent
//import cn.rtast.libmc.protocol.protocol.state.ProtocolState
//import cn.rtast.libmc.protocol.util.generateOfflineUuid
//import cn.rtast.libmc.common.*
//import kotlinx.coroutines.Dispatchers
//import kotlinx.coroutines.coroutineScope
//import kotlinx.coroutines.currentCoroutineContext
//import kotlinx.coroutines.isActive
//import kotlinx.coroutines.launch
//import kotlin.uuid.Uuid
//
//
//public class MinecraftChatClient(
//    private val host: String,
//    private val port: Int,
//    private val username: String,
//    private val uuid: Uuid = generateOfflineUuid(username),
//    private val context: LibMCContext = LibMCContext(),
//) {
//    private var state = ProtocolState.HANDSHAKE
//
//    public suspend fun start(): Unit = coroutineScope {
//        val socket = Socket(host, port, context)
//        val input = socket.openReadChannel()
//        val output = socket.openWriteChannel()
//
//        executeInitHandshake(output)
//
//        val readerJob = launch(Dispatchers.Default) {
//            handleIncomingPackets(input, output)
//        }
//
//        readerJob.join()
//    }
//
//    private fun executeInitHandshake(output: WriteChannel) {
//        val handshakePacket = ServerboundHandshakePacket(776, host, port.toUShort(), HandshakeIntent.LOGIN)
//        output.sendPacket(handshakePacket, ServerboundHandshakePacket)
//        state = ProtocolState.LOGIN
//
//        val loginStartPacket = ServerboundLoginStartPacket(username, uuid)
//        output.sendPacket(loginStartPacket, ServerboundLoginStartPacket)
//    }
//
//    private suspend fun handleIncomingPackets(input: ReadChannel, output: WriteChannel) {
//        try {
//            while (currentCoroutineContext().isActive) {
//                val packetLength = input.readVarInt()
//                if (packetLength <= 0) continue
//
//                val packetBytes = ByteArray(packetLength)
//                input.readFully(packetBytes, 0, packetLength)
//
//                val buffer = BytesBuffer(packetBytes)
//                val packetId = buffer.readVarInt()
//                println("received -> State: $state | ID: 0x${packetId.toString(16).uppercase()} | Length: $packetLength")
//                try {
//                    when (state) {
//                        ProtocolState.LOGIN -> handleLoginPackets(packetId, output)
//                        ProtocolState.CONFIGURATION -> handleConfigurationPackets(packetId, buffer, output)
//                        ProtocolState.PLAY -> handlePlayPackets(packetId, buffer, output)
//                        else -> {}
//                    }
//                } catch (e: Exception) {
//                    println("parsing 0x${packetId.toString(16).uppercase()} Payload failed: ${e.message}")
//                }
//            }
//        } catch (e: Exception) {
//            e.printStackTrace()
//            println("disconnecting: ${e.message}")
//        }
//    }
//
//    private fun handleLoginPackets(packetId: Int, output: WriteChannel) {
//        when (packetId) {
//            0x02 -> {
//                output.sendPacket(ServerboundLoginAcknowledgedPacket(), ServerboundLoginAcknowledgedPacket)
//                state = ProtocolState.CONFIGURATION
//                println("[3/4] sent LoginAcknowledgedPacket -> switching to CONFIGURATION state")
//
//                output.sendPacket(
//                    ServerboundSelectKnownPacksPacket(knownPacks = emptyList()),
//                    ServerboundSelectKnownPacksPacket
//                )
//            }
//
//            0x00 -> {
//                println("login denied (ClientboundDisconnectLoginPacket)")
//            }
//        }
//    }
//
//    private fun handleConfigurationPackets(packetId: Int, packetBuffer: BytesBuffer, output: WriteChannel) {
//        when (packetId) {
//            0x0E -> {
//                println("received ClientboundSelectKnownPacksPacket")
//            }
//
//            0x03 -> {
//                output.sendPacket(ServerboundAckFinishConfigurationPacket, ServerboundAckFinishConfigurationPacket)
//                state = ProtocolState.PLAY
//            }
//
//            0x05 -> {
//                output.sendPacket(ServerboundPongPacket(0), ServerboundPongPacket)
//            }
//
//            0x01 -> println("configuration state disconnected")
//        }
//    }
//
//    private fun handlePlayPackets(packetId: Int, packetBuffer: BytesBuffer, output: WriteChannel) {
//        try {
//            when (packetId) {
//                0x2B -> println("[PLAY] Joined world")
//
//                0x2c -> {
//                    val keepAliveId = packetBuffer.readLong()
//                    output.sendPacket(ServerboundKeepAlivePlayPacket(id = keepAliveId), ServerboundKeepAlivePlayPacket)
//                    println("[PLAY] reply keep alive packet $keepAliveId")
//                }
//
//                0x1D -> println("[PLAY] disconnected (ClientboundDisconnectPlayPacket)")
//                else -> {}
//            }
//        } catch (e: Exception) {
//            println("parsing 0x${packetId.toString(16).uppercase()} failed, skipped: ${e.message}")
//        }
//    }
//}