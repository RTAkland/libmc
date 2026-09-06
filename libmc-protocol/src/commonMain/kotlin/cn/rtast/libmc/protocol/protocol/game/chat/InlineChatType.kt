/*
 * Copyright © 2026 RTAkland
 * Author: RTAkland
 * Date: 2026/9/6
 */


package cn.rtast.libmc.protocol.protocol.game.chat

import cn.rtast.libmc.common.BytesBuffer

public data class InlineChatType(
    val chat: ChatTypeDecoration,
    val narration: ChatTypeDecoration,
)

internal fun BytesBuffer.readInlineChatType(): InlineChatType {
    return InlineChatType(
        chat = readChatTypeDecoration(),
        narration = readChatTypeDecoration()
    )
}