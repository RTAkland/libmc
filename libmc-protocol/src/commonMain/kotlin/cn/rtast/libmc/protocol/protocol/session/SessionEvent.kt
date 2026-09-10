/*
 * Copyright © 2026 RTAkland
 * Author: RTAkland
 * Date: 2026/9/10
 */


package cn.rtast.libmc.protocol.protocol.session

import cn.rtast.libmc.protocol.protocol.game.chat.TextComponent
import cn.rtast.libmc.protocol.protocol.state.ProtocolState

public sealed interface SessionEvent {
    public data class DisconnectedEvent(val reason: TextComponent, val state: ProtocolState) : SessionEvent
    public object ConnectedEvent : SessionEvent
    public sealed interface ChangedState : SessionEvent {
        public data object STATUS : ChangedState
        public data object HANDSHAKE : ChangedState
        public data object LOGIN : ChangedState
        public data object CONFIGURATION : ChangedState
        public data object PLAY : ChangedState
        public data object DISCONNECTED : ChangedState
    }
}