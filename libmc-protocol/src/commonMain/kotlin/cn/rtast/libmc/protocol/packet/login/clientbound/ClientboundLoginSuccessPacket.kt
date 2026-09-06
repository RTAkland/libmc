/*
 * Copyright © 2026 RTAkland
 * Author: RTAkland
 * Date: 2026/9/4
 */


package cn.rtast.libmc.protocol.packet.login.clientbound

import cn.rtast.libmc.common.BytesBuffer
import cn.rtast.libmc.common.PacketCodec
import cn.rtast.libmc.common.readUuid
import cn.rtast.libmc.protocol.session.GameProfile
import kotlin.uuid.Uuid

public data class ClientboundLoginSuccessPacket(val gameProfile: GameProfile, val sessionId: Uuid) :
    ClientboundLoginPacket {
    internal companion object Codec : PacketCodec<ClientboundLoginSuccessPacket> {
        override fun encode(buffer: BytesBuffer, value: ClientboundLoginSuccessPacket) {}
        override fun decode(buffer: BytesBuffer): ClientboundLoginSuccessPacket {
            val gameProfile = GameProfile.decode(buffer)
            val sessionId = buffer.readUuid()
            return ClientboundLoginSuccessPacket(gameProfile, sessionId)
        }
    }
}