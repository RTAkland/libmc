/*
 * Copyright © 2026 RTAkland
 * Author: RTAkland
 * Date: 2026/9/5
 */


package cn.rtast.libmc.protocol.block

public enum class LecternButton(public val id: Int) {
    PREVIOUS_PAGE(1),
    NEXT_PAGE(2),
    TAKE_BOOK(3);

    public companion object {
        public fun fromID(id: Int): LecternButton =
            requireNotNull(entries.firstOrNull { it.id == id }) { "Unknown lectern button id $id" }

        public fun specificPage(page: Int): Int = 100 + page
    }
}