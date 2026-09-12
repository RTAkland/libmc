/*
 * Copyright © 2026 RTAkland
 * Author: RTAkland
 * Date: 2026/9/12
 */


@file:OptIn(ExperimentalForeignApi::class)

package cn.rtast.libmc.socket

import kotlinx.cinterop.CPointer
import kotlinx.cinterop.ExperimentalForeignApi

internal expect fun formatSockAddrToIp(family: Int, addrPtr: CPointer<*>): String?