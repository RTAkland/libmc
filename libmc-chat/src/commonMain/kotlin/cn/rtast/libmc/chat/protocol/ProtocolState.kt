/*
 * Copyright © 2026 RTAkland
 * Author: RTAkland
 * Date: 2026/9/4
 */


package cn.rtast.libmc.chat.protocol

internal enum class ProtocolState {
    HANDSHAKE,
    LOGIN,
    CONFIGURATION,
    PLAY
}