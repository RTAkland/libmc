/*
 * Copyright © 2026 RTAkland
 * Author: RTAkland
 * Date: 2026/9/4
 */


package test

import cn.rtast.libmc.common.wrap
import cn.rtast.libmc.nbt.NbtReader
import org.junit.Test
import java.io.File

class TestNBTReader {

    private val javaNBTBuffer = File("src/commonTest/resources/level.dat").readBytes().wrap()

    @Test
    fun `test read java nbt`() {
        val readRoot = NbtReader(javaNBTBuffer).readRoot()
        println(readRoot)
    }
}