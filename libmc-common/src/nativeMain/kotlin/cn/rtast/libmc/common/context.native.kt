/*
 * Copyright © 2026 RTAkland
 * Author: RTAkland
 * Date: 2026/9/3
 */

@file:Suppress("PropertyName")

package cn.rtast.libmc.common

import io.ktor.network.selector.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO

public actual class LibMCContext actual constructor() {
    internal var _selectorManager: SelectorManager = SelectorManager(Dispatchers.IO)
    internal var _autoCloseSelectorManager: Boolean = false

    public constructor(selectorManager: SelectorManager, autoClose: Boolean = false) : this() {
        _selectorManager = selectorManager
        _autoCloseSelectorManager = autoClose
    }
}