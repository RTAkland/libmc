/*
 * Copyright © 2026 RTAkland
 * Author: RTAkland
 * Date: 2026/9/11
 */


package cn.rtast.libmc.protocol.protocol.game.advancement

import cn.rtast.libmc.protocol.protocol.game.Identifier

public data class AdvancementProgress(val criteria: Map<Identifier, Long?>)