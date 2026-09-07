/*
 * Copyright © 2026 RTAkland
 * Author: RTAkland
 * Date: 2026/9/7
 */


package test

import cn.rtast.libmc.protocol.client.createMinecraftClient
import cn.rtast.libmc.protocol.crypto.DefaultProtocolContext
import cn.rtast.libmc.protocol.packet.play.clientbound.ClientboundCommandsPacket
import cn.rtast.libmc.protocol.packet.play.clientbound.ClientboundDisconnectPlayPacket
import cn.rtast.libmc.protocol.packet.play.clientbound.ClientboundDisguisedChatMessagePacket
import cn.rtast.libmc.protocol.packet.play.clientbound.ClientboundSystemChatMessagePacket
import cn.rtast.libmc.protocol.util.generateOfflineUuid
import kotlinx.coroutines.launch
import org.junit.Test
import java.io.File
import java.math.BigInteger
import java.security.KeyFactory
import java.security.MessageDigest
import java.security.spec.X509EncodedKeySpec
import javax.crypto.Cipher
import kotlin.uuid.Uuid


class TestClientTestInJvm {
    val accessToken = File("src/jvmTest/resources/accessToken.txt").readText()

    fun encrypt(publicKeyBytes: ByteArray, data: ByteArray): ByteArray {
        val keySpec = X509EncodedKeySpec(publicKeyBytes)
        val keyFactory = KeyFactory.getInstance("RSA")
        val publicKey = keyFactory.generatePublic(keySpec)
        val cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding")
        cipher.init(Cipher.ENCRYPT_MODE, publicKey)
        return cipher.doFinal(data)
    }

    fun minecraftServerIdHash(serverId: String, secretKey: ByteArray, publicKey: ByteArray): String {
        val serverIdBytes: ByteArray = serverId.encodeToByteArray()
        for (b in serverIdBytes) {
            require((b.toInt() and 0xFF) <= 0x7F) { "serverId contains non-US-ASCII character" }
        }
        val data = ByteArray(serverIdBytes.size + secretKey.size + publicKey.size)
        System.arraycopy(serverIdBytes, 0, data, 0, serverIdBytes.size)
        System.arraycopy(secretKey, 0, data, serverIdBytes.size, secretKey.size)
        System.arraycopy(publicKey, 0, data, serverIdBytes.size + secretKey.size, publicKey.size)
        val digest = MessageDigest.getInstance("SHA-1")
        val hash = digest.digest(data)
        return BigInteger(hash).toString(16)
    }

    @Test
    fun `test client`() {
        val cli = createMinecraftClient(
            "127.0.0.1",
            25565,
            "RTAkland",
//            generateOfflineUuid("RTAkland"),
            Uuid.parse("bb033844-e68e-4909-a636-1a5d1821ddc4"),
//            null,
            accessToken,
            crypto = DefaultProtocolContext
        )
//        cli.onPacket<ClientboundSystemChatMessagePacket> { println(it) }
//        cli.onPacket<ClientboundLoginSuccessPacket> { println(it) }
        cli.on { packet, direction -> println("$direction -> $packet") }
        cli.launch { cli.connect() }
        while (true) {
        }
    }

    @Test
    fun `test client offline mode`() {
        val cli = createMinecraftClient(
            "127.0.0.1",
            25566,
            "11",
            generateOfflineUuid("11"),
            null,
            crypto = DefaultProtocolContext
        )
//        cli.on { packet, direction -> println("$direction -> $packet") }
        cli.onPacket<ClientboundSystemChatMessagePacket> { println(it.content.content) }
        cli.launch { cli.connect() }
        while (true) {
        }
    }
}