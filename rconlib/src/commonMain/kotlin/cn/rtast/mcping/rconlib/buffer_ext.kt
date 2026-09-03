/*
 * Copyright © 2026 RTAkland
 * Author: RTAkland
 * Date: 2026/9/4
 */


package cn.rtast.mcping.rconlib

import cn.rtast.mcping.platform._Buffer

internal fun _Buffer.writeNull() = writeByte(0x00)