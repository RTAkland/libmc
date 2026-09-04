/*
 * Copyright © 2026 RTAkland
 * Author: RTAkland
 * Date: 2026/9/3
 */


package test

import cn.rtast.libmc.mcping.ServerType
import cn.rtast.libmc.mcping.mcping
import kotlinx.coroutines.test.runTest
import kotlin.test.Test

class TestPing {

    @Test
    fun `test ping java server`() = runTest {
        val response = mcping("org.mc-complex.com", 25565)
        println(response)
    }

    @Test
    fun `test ping bedrock server`() = runTest {
        val response = mcping("play.wildnetwork.net", 19132, ServerType.Bedrock)
        println(response)
        println(response.toBedrockResponse())
    }
}