/*
 * Copyright © 2026 RTAkland
 * Author: RTAkland
 * Date: 2026/9/4
 */


package cn.rtast.libmc.protocol.packet.configuration

import cn.rtast.libmc.common.BytesBuffer
import cn.rtast.libmc.common.PacketCodec
import cn.rtast.libmc.common.readMcString
import cn.rtast.libmc.common.writeMcString

public data class KnownPacks(val namespace: String, val id: String, val version: String) {
    public companion object Codec : PacketCodec<KnownPacks> {
        override fun encode(buffer: BytesBuffer, value: KnownPacks) {
            buffer.writeMcString(value.namespace)
            buffer.writeMcString(value.id)
            buffer.writeMcString(value.version)
        }

        override fun decode(buffer: BytesBuffer): KnownPacks {
            val namespace = buffer.readMcString()
            val id = buffer.readMcString()
            val version = buffer.readMcString()
            return KnownPacks(namespace, id, version)
        }
    }
}

