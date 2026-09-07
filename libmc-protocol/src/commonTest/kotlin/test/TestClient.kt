/*
 * Copyright © 2026 RTAkland
 * Author: RTAkland
 * Date: 2026/9/4
 */


package test

import cn.rtast.libmc.protocol.client.createMinecraftClient
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.uuid.Uuid

class TestClient {

    @Test
    fun `test client`() = runTest {
        val cli = createMinecraftClient(
            "127.0.0.1",
            25565,
            "RTAkland",
            Uuid.parse("bb033844-e68e-4909-a636-1a5d1821ddc4"),
            null
        ) {
//            rsaEncryptor = RSA1024Encryptor { data, sharedKey -> }
        }
        cli.launch { cli.connect() }
        cli.on { packet, direction -> println("${direction} -> $packet") }
        while (true) {
        }
    }
}