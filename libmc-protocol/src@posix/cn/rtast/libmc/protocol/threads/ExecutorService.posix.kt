/*
 * Copyright © 2026 RTAkland
 * Author: RTAkland
 * Date: 2026/9/13
 */



package cn.rtast.libmc.protocol.threads

import platform.posix.sched_yield

internal actual fun spinWait(): Unit = run { sched_yield() }