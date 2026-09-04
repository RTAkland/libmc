/*
 * Copyright © 2026 RTAkland
 * Author: RTAkland
 * Date: 2026/9/3
 */


package test

import cn.rtast.libmc.common.LibMCContext
import cn.rtast.libmc.mcping.ServerType
import cn.rtast.libmc.mcping.mcping
import io.ktor.network.selector.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.test.runTest
import kotlin.test.Test

class TestMingwPing {

    @Test
    fun `test ping java on mingw with selectorManager`() = runTest {
        val sm = SelectorManager(Dispatchers.IO)
        val response =
            mcping(host = "org.mc-complex.com", port = 25565, type = ServerType.Java, context = LibMCContext(sm))
        println(response)
    }

    @Test
    fun `test ping bedrock on mingw with selectorManager`() = runTest {
        val sm = SelectorManager(Dispatchers.IO)
        val response =
            mcping(host = "play.wildnetwork.net", port = 19132, type = ServerType.Bedrock, context = LibMCContext(sm))
        println(response)
        println(response.toBedrockResponse())
    }
}