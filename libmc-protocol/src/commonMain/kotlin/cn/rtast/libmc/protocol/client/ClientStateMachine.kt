/*
 * Copyright © 2026 RTAkland
 * Author: RTAkland
 * Date: 2026/9/5
 */


package cn.rtast.libmc.protocol.client

import cn.rtast.libmc.protocol.protocol.state.ProtocolState
import kotlin.concurrent.Volatile

internal class ClientStateMachine {
    @Volatile
    var currentState: ProtocolState = ProtocolState.HANDSHAKE
        private set

    fun transitionTo(newState: ProtocolState) {
        println("Changing State $currentState to $newState")
        currentState = newState
    }
}