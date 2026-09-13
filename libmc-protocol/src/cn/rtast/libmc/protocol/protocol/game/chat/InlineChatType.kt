/*
 * Copyright © 2026 RTAkland
 * Author: RTAkland
 * Date: 2026/9/6
 */


package cn.rtast.libmc.protocol.protocol.game.chat

import cn.rtast.libmc.network.BytesBuffer

public data class InlineChatType(
    val chat: ChatTypeDecoration,
    val narration: ChatTypeDecoration,
)

internal fun BytesBuffer.readInlineChatType(): InlineChatType {
    val chat = readChatTypeDecoration()
    val narration = readChatTypeDecoration()
    return InlineChatType(chat, narration)
}