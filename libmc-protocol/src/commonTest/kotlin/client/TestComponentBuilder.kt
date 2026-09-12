/*
 * Copyright © 2026 RTAkland
 * Author: RTAkland
 * Date: 2026/9/13
 */


package client

import cn.rtast.libmc.protocol.protocol.game.chat.textComponent
import cn.rtast.libmc.protocol.protocol.game.chat.toNBTCompound
import kotlin.test.Test

class TestComponentBuilder {
    @Test
    fun `test text component builder`() {
        val chatMessage = textComponent("1") {
            color("gold")
            bold = true
            text("2") {
                color("white")
                bold = false
            }

            text("2") {
                color("green")
                underlined = true
                onHoverShowText {
                    text("copy") {
                        color("yellow")
                        italic = true
                    }
                }

                onClickCopyToClipboard("1")
            }

            text("4") {
                color("green")
                bold = false
            }
        }
        println(chatMessage)
        println(chatMessage.toJsonString())
        println(chatMessage.toNBTCompound())
    }
}