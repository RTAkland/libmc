/*
 * Copyright © 2026 RTAkland
 * Author: RTAkland
 * Date: 2026/9/6
 */


package cn.rtast.libmc.protocol.protocol.game.resources

public enum class ResourcePackResult(public val id: Int) {
    SuccessfullyDownloaded(0),
    Declined(1),
    FailedToDownload(2),
    Accepted(3),
    Downloaded(4),
    InvalidURL(5),
    FailedToReload(6),
    Discarded(7);

    public companion object {
        public fun fromID(id: Int): ResourcePackResult = entries.first { it.id == id }
    }
}