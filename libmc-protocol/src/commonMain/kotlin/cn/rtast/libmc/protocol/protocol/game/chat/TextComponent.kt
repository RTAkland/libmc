/*
 * Copyright © 2026 RTAkland
 * Author: RTAkland
 * Date: 2026/9/7
 */

package cn.rtast.libmc.protocol.protocol.game.chat

import cn.rtast.libmc.nbt.NBTCompound
import cn.rtast.libmc.nbt.NBTTag
import cn.rtast.libmc.nbt.toJsonString
import cn.rtast.libmc.network.BytesBuffer
import cn.rtast.libmc.protocol.protocol.game.Identifier
import cn.rtast.libmc.protocol.protocol.util.readNetworkNBTCompound
import cn.rtast.libmc.protocol.protocol.util.writeNetworkNBTCompound
import kotlin.uuid.Uuid

public data class TextComponent(
    val content: Content,
    val style: Style = Style.EMPTY,
    val extra: List<TextComponent> = emptyList(),
    val clickEvent: ClickEvent? = null,
    val hoverEvent: HoverEvent? = null,
    val insertion: String? = null,
) {
    public sealed interface Content {
        public val type: String

        public data class PlainText(val text: String) : Content {
            override val type: String = "text"
        }

        public data class Translatable(
            val key: String,
            val fallback: String? = null,
            val args: List<TextComponent> = emptyList(),
        ) : Content {
            override val type: String = "translatable"
        }

        public data class Score(val name: String, val objective: String) : Content {
            override val type: String = "score"
        }

        public data class Selector(val selector: String, val separator: TextComponent? = null) : Content {
            override val type: String = "selector"
        }

        public data class Keybind(val keybind: String) : Content {
            override val type: String = "keybind"
        }

        public data class Nbt(
            val path: String,
            val interpret: Boolean = false,
            val plain: Boolean = false,
            val separator: TextComponent? = null,
            val source: NbtSource,
        ) : Content {
            override val type: String = "nbt"

            public sealed interface NbtSource {
                public data class Entity(val selector: String) : NbtSource
                public data class Block(val coordinates: String) : NbtSource
                public data class Storage(val id: Identifier) : NbtSource
            }
        }

        public sealed interface ObjectContent : Content {
            override val type: String get() = "object"

            public data class Atlas(
                val sprite: Identifier,
                val atlas: Identifier = Identifier.of("minecraft", "blocks"),
            ) : ObjectContent

            public data class Player(
                val profile: PlayerProfile,
                val hat: Boolean = true,
            ) : ObjectContent
        }
    }

    public data class Style(
        val color: TextColor? = null,
        val font: Identifier? = null,
        val bold: Boolean? = null,
        val italic: Boolean? = null,
        val underlined: Boolean? = null,
        val strikethrough: Boolean? = null,
        val obfuscated: Boolean? = null,
        val shadowColor: ShadowColor? = null,
    ) {
        public companion object {
            public val EMPTY: Style = Style()
        }
    }

    public sealed interface PlayerProfile {
        public data class Name(val name: String) : PlayerProfile
        public data class FullProfile(
            val name: String? = null,
            val id: Uuid? = null,
            val properties: List<Property> = emptyList(),
        ) : PlayerProfile {
            public data class Property(
                val name: String,
                val value: String,
                val signature: String? = null,
            )
        }
    }

    public sealed interface TextColor {
        public data class Named(val name: String) : TextColor
        public data class Hex(val hex: String) : TextColor
    }

    public sealed interface ShadowColor {
        public data class ArgbInt(val argb: Long) : ShadowColor
        public data class RgbaFloat(
            val red: Float,
            val green: Float,
            val blue: Float,
            val alpha: Float,
        ) : ShadowColor
    }

    public sealed interface ClickEvent {
        public val action: ClickEventAction

        public enum class ClickEventAction(public val text: String) {
            OpenURL("open_url"),
            OpenFile("open_file"),
            RunCommand("run_command"),
            SuggestCommand("suggest_command"),
            ChangePage("change_page"),
            CopyToClipboard("copy_to_clipboard"),
            ShowDialog("show_dialog"),
            Custom("custom");

            public companion object {
                public fun fromText(text: String): ClickEventAction =
                    entries.first { it.text == text }
            }
        }

        public data class OpenUrl(val url: String) : ClickEvent {
            override val action: ClickEventAction = ClickEventAction.OpenURL
        }

        public data class OpenFile(val path: String) : ClickEvent {
            override val action: ClickEventAction = ClickEventAction.OpenFile
        }

        public data class RunCommand(val command: String) : ClickEvent {
            override val action: ClickEventAction = ClickEventAction.RunCommand
        }

        public data class SuggestCommand(val command: String) : ClickEvent {
            override val action: ClickEventAction = ClickEventAction.SuggestCommand
        }

        public data class ChangePage(val page: Int) : ClickEvent {
            override val action: ClickEventAction = ClickEventAction.ChangePage
        }

        public data class CopyToClipboard(val value: String) : ClickEvent {
            override val action: ClickEventAction = ClickEventAction.CopyToClipboard
        }

        public data class ShowDialog(val dialog: DialogPayload) : ClickEvent {
            override val action: ClickEventAction = ClickEventAction.ShowDialog

            public sealed interface DialogPayload {
                public data class Id(val id: Identifier) : DialogPayload
                public data class Definition(val compound: NBTTag.CompoundTag) : DialogPayload
            }
        }

        public data class Custom(val id: Identifier, val payload: String? = null) : ClickEvent {
            override val action: ClickEventAction = ClickEventAction.Custom
        }
    }

    public sealed interface HoverEvent {
        public val action: HoverEventAction

        public enum class HoverEventAction(public val text: String) {
            ShowText("show_text"),
            ShowItem("show_item"),
            ShowEntity("show_entity");

            public companion object {
                public fun fromText(text: String): HoverEventAction =
                    entries.first { it.text == text }
            }
        }

        public data class ShowText(val component: TextComponent) : HoverEvent {
            override val action: HoverEventAction = HoverEventAction.ShowText
        }

        public data class ShowItem(
            val id: Identifier,
            val count: Int = 1,
            val components: NBTTag.CompoundTag? = null,
        ) : HoverEvent {
            override val action: HoverEventAction = HoverEventAction.ShowItem
        }

        public data class ShowEntity(
            val id: Identifier,
            val uuid: UUIDRepresentation,
            val name: TextComponent? = null,
        ) : HoverEvent {
            override val action: HoverEventAction = HoverEventAction.ShowEntity

            public sealed interface UUIDRepresentation {
                public data class StringFormat(val uuid: Uuid) : UUIDRepresentation
                public data class ArrayFormat(val i1: Int, val i2: Int, val i3: Int, val i4: Int) : UUIDRepresentation
            }
        }
    }

    public fun toJsonString(): String = this.toNBTCompound().toJsonString()

    public companion object {
        public fun of(text: String): TextComponent = TextComponent(content = Content.PlainText(text))
    }
}

internal fun BytesBuffer.readTextComponent(): TextComponent =
    this.readNetworkNBTCompound().element.toTextComponent()

internal fun BytesBuffer.writeTextComponent(component: TextComponent) {
    val nbt = component.toNBTCompound()
    this.writeNetworkNBTCompound(NBTCompound("", nbt))
}