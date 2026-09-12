/*
 * Copyright © 2026 RTAkland
 * Author: RTAkland
 * Date: 2026/9/13
 */


package cn.rtast.libmc.protocol.protocol.game.chat

import cn.rtast.libmc.nbt.NBTTag
import cn.rtast.libmc.nbt.NBTType
import cn.rtast.libmc.protocol.protocol.game.Identifier
import kotlin.uuid.Uuid

public fun NBTTag.toTextComponent(): TextComponent {
    return when (this) {
        is NBTTag.StringTag -> TextComponent.of(this.value)
        is NBTTag.ListTag -> {
            val tags = this.value
            if (tags.isEmpty()) return TextComponent.of("")
            val head = tags.first().toTextComponent()
            val tail = tags.drop(1).map { it.toTextComponent() }
            head.copy(extra = head.extra + tail)
        }

        is NBTTag.CompoundTag -> {
            val content = parseContentFromNbt(this)
            val style = parseStyleFromNbt(this)
            val extra = this.getListOrNull("extra")?.map { it.toTextComponent() } ?: emptyList()
            val clickEvent = this.getCompoundOrNull("click_event")?.let { parseClickEventFromNbt(it) }
            val hoverEvent = this.getCompoundOrNull("hover_event")?.let { parseHoverEventFromNbt(it) }
            val insertion = this.getStringOrNull("insertion")
            TextComponent(content, style, extra, clickEvent, hoverEvent, insertion)
        }

        else -> TextComponent.of(this.toString())
    }
}

public fun TextComponent.toNBTCompound(): NBTTag.CompoundTag {
    val map = mutableMapOf<String, NBTTag>()
    when (val c = this.content) {
        is TextComponent.Content.PlainText -> {
            map["type"] = NBTTag.StringTag("text")
            map["text"] = NBTTag.StringTag(c.text)
        }

        is TextComponent.Content.Translatable -> {
            map["type"] = NBTTag.StringTag("translatable")
            map["translate"] = NBTTag.StringTag(c.key)
            c.fallback?.let { map["fallback"] = NBTTag.StringTag(it) }
            if (c.args.isNotEmpty()) {
                map["with"] = NBTTag.ListTag(NBTType.List, c.args.map { it.toNBTCompound() as NBTTag }.toMutableList())
            }
        }

        is TextComponent.Content.Score -> {
            map["type"] = NBTTag.StringTag("score")
            map["score"] = NBTTag.CompoundTag(
                mutableMapOf(
                    "name" to NBTTag.StringTag(c.name),
                    "objective" to NBTTag.StringTag(c.objective)
                )
            )
        }

        is TextComponent.Content.Selector -> {
            map["type"] = NBTTag.StringTag("selector")
            map["selector"] = NBTTag.StringTag(c.selector)
            c.separator?.let { map["separator"] = it.toNBTCompound() }
        }

        is TextComponent.Content.Keybind -> {
            map["type"] = NBTTag.StringTag("keybind")
            map["keybind"] = NBTTag.StringTag(c.keybind)
        }

        is TextComponent.Content.Nbt -> {
            map["type"] = NBTTag.StringTag("nbt")
            map["nbt"] = NBTTag.StringTag(c.path)
            map["interpret"] = NBTTag.ByteTag(if (c.interpret) 1 else 0)
            map["plain"] = NBTTag.ByteTag(if (c.plain) 1 else 0)
            c.separator?.let { map["separator"] = it.toNBTCompound() }
            when (val s = c.source) {
                is TextComponent.Content.Nbt.NbtSource.Entity -> map["entity"] = NBTTag.StringTag(s.selector)
                is TextComponent.Content.Nbt.NbtSource.Block -> map["block"] = NBTTag.StringTag(s.coordinates)
                is TextComponent.Content.Nbt.NbtSource.Storage -> map["storage"] = NBTTag.StringTag(s.id.toString())
            }
        }

        is TextComponent.Content.ObjectContent.Atlas -> {
            map["type"] = NBTTag.StringTag("object")
            map["object"] = NBTTag.StringTag("atlas")
            map["sprite"] = NBTTag.StringTag(c.sprite.toString())
            map["atlas"] = NBTTag.StringTag(c.atlas.toString())
        }

        is TextComponent.Content.ObjectContent.Player -> {
            map["type"] = NBTTag.StringTag("object")
            map["object"] = NBTTag.StringTag("player")
            map["hat"] = NBTTag.ByteTag(if (c.hat) 1 else 0)
            when (val p = c.profile) {
                is TextComponent.PlayerProfile.Name -> map["player"] = NBTTag.StringTag(p.name)
                is TextComponent.PlayerProfile.FullProfile -> {
                    val pMap = mutableMapOf<String, NBTTag>()
                    p.name?.let { pMap["name"] = NBTTag.StringTag(it) }
                    p.id?.let { pMap["id"] = NBTTag.StringTag(it.toString()) }
                    map["player"] = NBTTag.CompoundTag(pMap)
                }
            }
        }
    }

    this.style.color?.let {
        map["color"] = NBTTag.StringTag(
            when (it) {
                is TextComponent.TextColor.Named -> it.name
                is TextComponent.TextColor.Hex -> it.hex
            }
        )
    }
    this.style.font?.let { map["font"] = NBTTag.StringTag(it.toString()) }
    this.style.bold?.let { map["bold"] = NBTTag.ByteTag(if (it) 1 else 0) }
    this.style.italic?.let { map["italic"] = NBTTag.ByteTag(if (it) 1 else 0) }
    this.style.underlined?.let { map["underlined"] = NBTTag.ByteTag(if (it) 1 else 0) }
    this.style.strikethrough?.let { map["strikethrough"] = NBTTag.ByteTag(if (it) 1 else 0) }
    this.style.obfuscated?.let { map["obfuscated"] = NBTTag.ByteTag(if (it) 1 else 0) }
    this.style.shadowColor?.let {
        when (it) {
            is TextComponent.ShadowColor.ArgbInt -> map["shadow_color"] = NBTTag.IntTag(it.argb.toInt())
            is TextComponent.ShadowColor.RgbaFloat -> map["shadow_color"] = NBTTag.ListTag(
                NBTType.List, mutableListOf(
                    NBTTag.FloatTag(it.red),
                    NBTTag.FloatTag(it.green),
                    NBTTag.FloatTag(it.blue),
                    NBTTag.FloatTag(it.alpha)
                )
            )
        }
    }

    if (this.extra.isNotEmpty()) map["extra"] =
        NBTTag.ListTag(NBTType.List, this.extra.map { it.toNBTCompound() as NBTTag }.toMutableList())
    this.insertion?.let { map["insertion"] = NBTTag.StringTag(it) }
    this.clickEvent?.let { map["click_event"] = encodeClickEventToNbt(it) }
    this.hoverEvent?.let { map["hover_event"] = encodeHoverEventToNbt(it) }

    return NBTTag.CompoundTag(map)
}

private fun parseContentFromNbt(tag: NBTTag.CompoundTag): TextComponent.Content {
    val type = tag.getStringOrNull("type")
    return when {
        type == "text" || (type == null && tag.containsKey("text")) -> TextComponent.Content.PlainText(tag.getString("text"))
        type == "translatable" || (type == null && tag.containsKey("translate")) -> {
            TextComponent.Content.Translatable(
                tag.getString("translate"),
                tag.getStringOrNull("fallback"),
                tag.getListOrNull("with")?.map { it.toTextComponent() } ?: emptyList()
            )
        }

        type == "score" || (type == null && tag.containsKey("score")) -> {
            val scoreObj = tag.getCompound("score")
            TextComponent.Content.Score(scoreObj.getString("name"), scoreObj.getString("objective"))
        }

        type == "selector" || (type == null && tag.containsKey("selector")) -> {
            TextComponent.Content.Selector(tag.getString("selector"), tag.getOrNull("separator")?.toTextComponent())
        }

        type == "keybind" || (type == null && tag.containsKey("keybind")) -> TextComponent.Content.Keybind(
            tag.getString("keybind")
        )

        type == "nbt" || (type == null && tag.containsKey("nbt")) -> {
            val path = tag.getString("nbt")
            val interpret = tag.getBooleanOrNull("interpret") ?: false
            val plain = tag.getBooleanOrNull("plain") ?: false
            val separator = tag.getOrNull("separator")?.toTextComponent()
            val source = when {
                tag.containsKey("entity") -> TextComponent.Content.Nbt.NbtSource.Entity(tag.getString("entity"))
                tag.containsKey("block") -> TextComponent.Content.Nbt.NbtSource.Block(tag.getString("block"))
                tag.containsKey("storage") -> TextComponent.Content.Nbt.NbtSource.Storage(Identifier.of(tag.getString("storage")))
                else -> throw IllegalArgumentException("Missing NBT source (entity, block, or storage)")
            }
            TextComponent.Content.Nbt(path, interpret, plain, separator, source)
        }

        type == "object" || (type == null && (tag.containsKey("atlas") || tag.containsKey("sprite") || tag.containsKey("player"))) -> {
            val objectType = tag.getStringOrNull("object") ?: if (tag.containsKey("player")) "player" else "atlas"
            if (objectType == "player") {
                val hat = tag.getBooleanOrNull("hat") ?: true
                val playerTag = tag.getOrNull("player")
                val profile = if (playerTag is NBTTag.StringTag) {
                    TextComponent.PlayerProfile.Name(playerTag.value)
                } else {
                    val pComp = playerTag as NBTTag.CompoundTag
                    TextComponent.PlayerProfile.FullProfile(
                        name = pComp.getStringOrNull("name"),
                        id = pComp.getStringOrNull("id")?.let { Uuid.parse(it) }
                    )
                }
                TextComponent.Content.ObjectContent.Player(profile, hat)
            } else TextComponent.Content.ObjectContent.Atlas(
                Identifier.of(tag.getString("sprite")),
                tag.getStringOrNull("atlas")?.let { Identifier.of(it) } ?: Identifier.of(
                    "minecraft", "blocks"
                )
            )
        }

        else -> TextComponent.Content.PlainText("")
    }
}

private fun parseStyleFromNbt(tag: NBTTag.CompoundTag): TextComponent.Style {
    val color = tag.getStringOrNull("color")?.let {
        if (it.startsWith("#")) TextComponent.TextColor.Hex(it) else TextComponent.TextColor.Named(it)
    }
    val font = tag.getStringOrNull("font")?.let { Identifier.of(it) }
    val shadowColor = when (val sc = tag.getOrNull("shadow_color")) {
        is NBTTag.IntTag -> TextComponent.ShadowColor.ArgbInt(sc.value.toLong())
        is NBTTag.ListTag -> {
            val floats = sc.value.filterIsInstance<NBTTag.FloatTag>().map { it.value }
            if (floats.size == 4) {
                TextComponent.ShadowColor.RgbaFloat(floats[0], floats[1], floats[2], floats[3])
            } else null
        }

        else -> null
    }

    return TextComponent.Style(
        color, font, tag.getBooleanOrNull("bold"),
        tag.getBooleanOrNull("italic"), tag.getBooleanOrNull("underlined"),
        tag.getBooleanOrNull("strikethrough"), tag.getBooleanOrNull("obfuscated"), shadowColor
    )
}

private fun parseClickEventFromNbt(tag: NBTTag.CompoundTag): TextComponent.ClickEvent {
    val action = TextComponent.ClickEvent.ClickEventAction.fromText(tag.getString("action"))
    return when (action) {
        TextComponent.ClickEvent.ClickEventAction.OpenURL -> TextComponent.ClickEvent.OpenUrl(tag.getString("url"))
        TextComponent.ClickEvent.ClickEventAction.OpenFile -> TextComponent.ClickEvent.OpenFile(tag.getString("path"))
        TextComponent.ClickEvent.ClickEventAction.RunCommand -> TextComponent.ClickEvent.RunCommand(tag.getString("command"))
        TextComponent.ClickEvent.ClickEventAction.SuggestCommand -> TextComponent.ClickEvent.SuggestCommand(
            tag.getString("command")
        )

        TextComponent.ClickEvent.ClickEventAction.ChangePage -> TextComponent.ClickEvent.ChangePage(tag.getInt("page"))
        TextComponent.ClickEvent.ClickEventAction.CopyToClipboard -> TextComponent.ClickEvent.CopyToClipboard(
            tag.getString(
                "value"
            )
        )

        TextComponent.ClickEvent.ClickEventAction.ShowDialog -> {
            val dialogTag = tag.getOrNull("dialog")
            val payload = if (dialogTag is NBTTag.StringTag) {
                TextComponent.ClickEvent.ShowDialog.DialogPayload.Id(Identifier.of(dialogTag.value))
            } else TextComponent.ClickEvent.ShowDialog.DialogPayload.Definition(dialogTag as NBTTag.CompoundTag)
            TextComponent.ClickEvent.ShowDialog(payload)
        }

        TextComponent.ClickEvent.ClickEventAction.Custom -> TextComponent.ClickEvent.Custom(
            Identifier.of(tag.getString("id")), tag.getStringOrNull("payload")
        )
    }
}

private fun parseHoverEventFromNbt(tag: NBTTag.CompoundTag): TextComponent.HoverEvent {
    val action = TextComponent.HoverEvent.HoverEventAction.fromText(tag.getString("action"))
    return when (action) {
        TextComponent.HoverEvent.HoverEventAction.ShowText -> {
            TextComponent.HoverEvent.ShowText(tag.getCompound("value").toTextComponent())
        }

        TextComponent.HoverEvent.HoverEventAction.ShowItem -> {
            TextComponent.HoverEvent.ShowItem(
                Identifier.of(tag.getString("id")), tag.getIntOrNull("count") ?: 1, tag.getCompoundOrNull("components")
            )
        }

        TextComponent.HoverEvent.HoverEventAction.ShowEntity -> {
            val uuidTag = tag.getOrNull("uuid")
            val uuid = if (uuidTag is NBTTag.StringTag) {
                TextComponent.HoverEvent.ShowEntity.UUIDRepresentation.StringFormat(Uuid.parse(uuidTag.value))
            } else {
                val list = (uuidTag as NBTTag.IntArrayTag).value
                TextComponent.HoverEvent.ShowEntity.UUIDRepresentation.ArrayFormat(list[0], list[1], list[2], list[3])
            }
            TextComponent.HoverEvent.ShowEntity(
                Identifier.of(tag.getString("id")), uuid, tag.getOrNull("name")?.toTextComponent()
            )
        }
    }
}

private fun encodeClickEventToNbt(event: TextComponent.ClickEvent): NBTTag.CompoundTag {
    val map = mutableMapOf<String, NBTTag>("action" to NBTTag.StringTag(event.action.text))
    when (event) {
        is TextComponent.ClickEvent.OpenUrl -> map["url"] = NBTTag.StringTag(event.url)
        is TextComponent.ClickEvent.OpenFile -> map["path"] = NBTTag.StringTag(event.path)
        is TextComponent.ClickEvent.RunCommand -> map["command"] = NBTTag.StringTag(event.command)
        is TextComponent.ClickEvent.SuggestCommand -> map["command"] = NBTTag.StringTag(event.command)
        is TextComponent.ClickEvent.ChangePage -> map["page"] = NBTTag.IntTag(event.page)
        is TextComponent.ClickEvent.CopyToClipboard -> map["value"] = NBTTag.StringTag(event.value)
        is TextComponent.ClickEvent.ShowDialog -> {
            map["dialog"] = when (val d = event.dialog) {
                is TextComponent.ClickEvent.ShowDialog.DialogPayload.Id -> NBTTag.StringTag(d.id.toString())
                is TextComponent.ClickEvent.ShowDialog.DialogPayload.Definition -> d.compound
            }
        }

        is TextComponent.ClickEvent.Custom -> {
            map["id"] = NBTTag.StringTag(event.id.toString())
            event.payload?.let { map["payload"] = NBTTag.StringTag(it) }
        }
    }
    return NBTTag.CompoundTag(map)
}

private fun encodeHoverEventToNbt(event: TextComponent.HoverEvent): NBTTag.CompoundTag {
    val map = mutableMapOf<String, NBTTag>("action" to NBTTag.StringTag(event.action.text))
    when (event) {
        is TextComponent.HoverEvent.ShowText -> map["value"] = event.component.toNBTCompound()
        is TextComponent.HoverEvent.ShowItem -> {
            map["id"] = NBTTag.StringTag(event.id.toString())
            map["count"] = NBTTag.IntTag(event.count)
            event.components?.let { map["components"] = it }
        }

        is TextComponent.HoverEvent.ShowEntity -> {
            map["id"] = NBTTag.StringTag(event.id.toString())
            map["uuid"] = when (val u = event.uuid) {
                is TextComponent.HoverEvent.ShowEntity.UUIDRepresentation.StringFormat -> NBTTag.StringTag(u.uuid.toString())
                is TextComponent.HoverEvent.ShowEntity.UUIDRepresentation.ArrayFormat -> NBTTag.IntArrayTag(
                    intArrayOf(u.i1, u.i2, u.i3, u.i4)
                )
            }
            event.name?.let { map["name"] = it.toNBTCompound() }
        }
    }
    return NBTTag.CompoundTag(map)
}

private fun NBTTag.CompoundTag.getOrNull(key: String): NBTTag? = this.value[key]
private fun NBTTag.CompoundTag.containsKey(key: String): Boolean = this.value.containsKey(key)
private fun NBTTag.CompoundTag.getString(key: String): String = (this.value[key] as NBTTag.StringTag).value
private fun NBTTag.CompoundTag.getStringOrNull(key: String): String? = (this.value[key] as? NBTTag.StringTag)?.value
private fun NBTTag.CompoundTag.getInt(key: String): Int = (this.value[key] as NBTTag.IntTag).value
private fun NBTTag.CompoundTag.getIntOrNull(key: String): Int? = (this.value[key] as? NBTTag.IntTag)?.value
private fun NBTTag.CompoundTag.getBooleanOrNull(key: String): Boolean? = when (val t = this.value[key]) {
    is NBTTag.ByteTag -> t.value != 0.toByte()
    else -> null
}

private fun NBTTag.CompoundTag.getCompound(key: String): NBTTag.CompoundTag = this.value[key] as NBTTag.CompoundTag
private fun NBTTag.CompoundTag.getCompoundOrNull(key: String): NBTTag.CompoundTag? =
    this.value[key] as? NBTTag.CompoundTag

private fun NBTTag.CompoundTag.getListOrNull(key: String): List<NBTTag>? = (this.value[key] as? NBTTag.ListTag)?.value
