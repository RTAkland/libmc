/*
 * Copyright © 2026 RTAkland
 * Author: RTAkland
 * Date: 2026/9/13
 */


package cn.rtast.libmc.protocol.protocol.game.chat

import cn.rtast.libmc.protocol.protocol.game.Identifier

@DslMarker
public annotation class TextComponentDsl

@TextComponentDsl
public class TextComponentBuilder(private val content: TextComponent.Content) {
    public var color: TextComponent.TextColor? = null
    public var font: Identifier? = null
    public var bold: Boolean? = null
    public var italic: Boolean? = null
    public var underlined: Boolean? = null
    public var strikethrough: Boolean? = null
    public var obfuscated: Boolean? = null
    public var shadowColor: TextComponent.ShadowColor? = null

    public var clickEvent: TextComponent.ClickEvent? = null
    public var hoverEvent: TextComponent.HoverEvent? = null
    public var insertion: String? = null

    private val extraComponents = mutableListOf<TextComponent>()

    public fun color(name: String) {
        this.color = TextComponent.TextColor.Named(name)
    }

    public fun hexColor(hex: String) {
        this.color = TextComponent.TextColor.Hex(hex)
    }

    public fun shadowColor(argb: Long) {
        this.shadowColor = TextComponent.ShadowColor.ArgbInt(argb)
    }

    public fun onClickOpenUrl(url: String) {
        this.clickEvent = TextComponent.ClickEvent.OpenUrl(url)
    }

    public fun onClickRunCommand(command: String) {
        this.clickEvent = TextComponent.ClickEvent.RunCommand(command)
    }

    public fun onClickSuggestCommand(command: String) {
        this.clickEvent = TextComponent.ClickEvent.SuggestCommand(command)
    }

    public fun onClickCopyToClipboard(value: String) {
        this.clickEvent = TextComponent.ClickEvent.CopyToClipboard(value)
    }

    public fun onHoverShowText(component: TextComponent) {
        this.hoverEvent = TextComponent.HoverEvent.ShowText(component)
    }

    public fun onHoverShowText(builder: TextComponentBuilder.() -> Unit) {
        this.hoverEvent = TextComponent.HoverEvent.ShowText(textComponent(builder = builder))
    }

    public fun text(text: String, builder: (TextComponentBuilder.() -> Unit)? = null): TextComponent {
        val child = textComponent(text, builder)
        extraComponents.add(child)
        return child
    }

    public fun translatable(
        key: String,
        fallback: String? = null,
        args: List<TextComponent> = emptyList(),
        builder: (TextComponentBuilder.() -> Unit)? = null,
    ): TextComponent {
        val content = TextComponent.Content.Translatable(key, fallback, args)
        val child = TextComponentBuilder(content).apply { builder?.invoke(this) }.build()
        extraComponents.add(child)
        return child
    }

    public fun append(component: TextComponent) {
        extraComponents.add(component)
    }

    public fun build(): TextComponent {
        val style = TextComponent.Style(
            color = color,
            font = font,
            bold = bold,
            italic = italic,
            underlined = underlined,
            strikethrough = strikethrough,
            obfuscated = obfuscated,
            shadowColor = shadowColor
        )
        return TextComponent(
            content = content,
            style = style,
            extra = extraComponents,
            clickEvent = clickEvent,
            hoverEvent = hoverEvent,
            insertion = insertion
        )
    }
}

public fun textComponent(
    defaultText: String = "",
    builder: (TextComponentBuilder.() -> Unit)? = null,
): TextComponent {
    val b = TextComponentBuilder(TextComponent.Content.PlainText(defaultText))
    builder?.invoke(b)
    return b.build()
}

public fun translatableComponent(
    key: String,
    fallback: String? = null,
    args: List<TextComponent> = emptyList(),
    builder: (TextComponentBuilder.() -> Unit)? = null,
): TextComponent {
    val b = TextComponentBuilder(TextComponent.Content.Translatable(key, fallback, args))
    builder?.invoke(b)
    return b.build()
}