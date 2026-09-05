/*
 * Copyright © 2026 RTAkland
 * Author: RTAkland
 * Date: 2026/9/5
 */


package cn.rtast.libmc.protocol.protocol.state

import cn.rtast.libmc.common.packet.PacketRegistry
import kotlin.enums.enumEntries

public class ProtocolStateRegistry {
    // create registries for different state
    private val registries = enumEntries<ProtocolState>().toTypedArray().associateWith { PacketRegistry() }

    public fun getRegistry(state: ProtocolState): PacketRegistry =
        requireNotNull(registries[state]) { "No registry found for state $state" }

    public fun register(state: ProtocolState, block: PacketRegistry.() -> Unit) {
        registries[state]?.apply(block)
    }
}