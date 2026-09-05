/*
 * Copyright © 2026 RTAkland
 * Author: RTAkland
 * Date: 2026/9/4
 */


package cn.rtast.libmc.common

public expect fun ByteArray.zlibDecompress(): ByteArray

public expect fun ByteArray.zlibDecompress(expectedSize: Int): ByteArray

public expect fun ByteArray.zlibCompress(): ByteArray