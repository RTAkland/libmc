/*
 * Copyright © 2026 RTAkland
 * Author: RTAkland
 * Date: 2026/9/7
 */


package client

import cn.rtast.libmc.crypto.AuthenticationProvider
import cn.rtast.libmc.packet.ClientboundUnknownPacket
import cn.rtast.libmc.protocol.client.createMinecraftClient
import cn.rtast.libmc.protocol.packet.play.clientbound.ClientboundLevelParticlePacket
import cn.rtast.libmc.protocol.packet.play.clientbound.ClientboundPlayerChatMessagePacket
import cn.rtast.libmc.protocol.packet.play.clientbound.ClientboundRecipeBookRemovePacket
import cn.rtast.libmc.protocol.packet.play.clientbound.ClientboundRecipeBookSettingsPacket
import cn.rtast.libmc.protocol.packet.play.clientbound.ClientboundStepTickPacket
import cn.rtast.libmc.protocol.packet.play.serverbound.ServerboundChatMessagePacket
import cn.rtast.libmc.protocol.util.generateOfflineUuid
import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.utils.io.*
import kotlinx.coroutines.launch
import kotlinx.io.buffered
import kotlinx.io.files.Path
import kotlinx.io.files.SystemFileSystem
import test.KtorNetworkEngine
import kotlin.random.Random
import kotlin.test.Test
import kotlin.time.Clock
import kotlin.uuid.Uuid


class TestClient {
    val accessToken = SystemFileSystem.source(Path("src/commonTest/resources/accessToken.txt"))
        .buffered().use { it.readText() }
    private val chatTracker = ClientChatTracker()
    private val httpClient = HttpClient()

    @Test
    fun `test client`() {
        val cli = createMinecraftClient(
            "127.0.0.1", 25565, "RTAkland",
            Uuid.parse("bb033844-e68e-4909-a636-1a5d1821ddc4"),
            accessToken,
            context = {
                socketEngine = KtorNetworkEngine()
                authProvider = AuthenticationProvider { url, accessToken, uuid, serverIdHash ->
                    val status = httpClient.post(url) {
                        headers { header("Content-Type", "application/json") }
                        setBody("{\"accessToken\":\"$accessToken\", \"selectedProfile\":\"$uuid\", \"serverId\":\"$serverIdHash\"}")
                    }
                    require(status.status == HttpStatusCode.NoContent) { status.bodyAsText() }
                }
            }
        )
        cli.on { packet, direction -> println("$direction -> $packet") }
        cli.launch { cli.connect() }
        while (true) {
        }
    }

    @Test
    fun `test client offline mode`() {
        val cli = createMinecraftClient(
            "127.0.0.1", 25566, "11",
            generateOfflineUuid("11"), null,
            context = {
                socketEngine = KtorNetworkEngine()
            }
        )
//        cli.onPacket<ClientboundUnknownPacket> {
//            println(it)
//            val snapshot = chatTracker.prepareForOutgoingMessage()
//            cli.networkChannel.sendPacket(
//                ServerboundChatMessagePacket(
//                    "114514", Clock.System.now().toEpochMilliseconds(),
//                    Random.nextLong(),
//                    null, snapshot.messageCount, createAcknowledgedBitSet(snapshot.lastSeenSignatures).toByteArray(),
//                    ChatPacketUtils.computePacketChecksum(snapshot.lastSeenSignatures)
//                )
//            )
//        }
        cli.onPacket<ClientboundPlayerChatMessagePacket> { chatTracker.onReceivePlayerChat(it.messageSignature) }
        cli.onPacket<ClientboundStepTickPacket> { println(it) }
        cli.launch { cli.connect() }
        while (true) {
        }
    }
}