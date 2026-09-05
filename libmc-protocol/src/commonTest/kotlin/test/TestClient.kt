/*
 * Copyright © 2026 RTAkland
 * Author: RTAkland
 * Date: 2026/9/4
 */


package test

import cn.rtast.libmc.protocol.client.MinecraftClient
import kotlinx.coroutines.test.runTest
import kotlin.test.Test

class TestClient {

    @Test
    fun `test client`() = runTest {
        val cli = MinecraftClient("127.0.0.1", 25565, "123")
        cli.connect()
    }
}