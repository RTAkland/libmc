/*
 * Copyright © 2026 RTAkland
 * Author: RTAkland
 * Date: 2026/9/5
 */


package test

import cn.rtast.libmc.zlibCompress
import cn.rtast.libmc.zlibDecompress
import kotlin.test.Test
import kotlin.test.assertContentEquals
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class TestZlib {

    @Test
    fun `test zlib compress decompress`() {
        val originalText = "Minecraft Protocol Compression".repeat(10)
        val originalBytes = originalText.encodeToByteArray()
        val compressedBytes = originalBytes.zlibCompress()
        assertTrue(compressedBytes.size < originalBytes.size)
        val decompressedBytes = compressedBytes.zlibDecompress(originalBytes.size)
        assertEquals(originalBytes.size, decompressedBytes.size)
        assertContentEquals(originalBytes, decompressedBytes)
    }
}