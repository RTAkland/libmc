/*
 * Copyright © 2026 RTAkland
 * Author: RTAkland
 * Date: 2026/9/5
 */


package cn.rtast.libmc.protocol.client

import cn.rtast.libmc.protocol.protocol.session.Session
import cn.rtast.libmc.protocol.protocol.session.SessionEvent
import cn.rtast.libmc.protocol.protocol.state.ProtocolState
import kotlin.concurrent.Volatile

internal class ClientStateMachine(private val session: Session) {
    @Volatile
    var currentState: ProtocolState = ProtocolState.HANDSHAKE
        private set

    suspend fun transitionTo(newState: ProtocolState) {
        println("Changing State $currentState to $newState")
        currentState = newState
        session.emitEvent(
            when (newState) {
                ProtocolState.HANDSHAKE -> SessionEvent.ChangedState.HANDSHAKE
                ProtocolState.LOGIN -> SessionEvent.ChangedState.LOGIN
                ProtocolState.CONFIGURATION -> SessionEvent.ChangedState.CONFIGURATION
                ProtocolState.PLAY -> SessionEvent.ChangedState.PLAY
                ProtocolState.STATUS -> SessionEvent.ChangedState.STATUS
                ProtocolState.DISCONNECTED -> SessionEvent.ChangedState.DISCONNECTED
            }
        )
    }
}