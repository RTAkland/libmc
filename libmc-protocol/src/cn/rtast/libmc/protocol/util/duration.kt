/*
 * Copyright © 2026 RTAkland
 * Author: RTAkland
 * Date: 2026/9/6
 */


package cn.rtast.libmc.protocol.util

import kotlin.time.Duration
import kotlin.time.Duration.Companion.milliseconds

/**
 * Converts an [Int] representing a count of ticks into a [Duration]
 */
public inline val Int.ticks: Duration
    get() = (this * 50).milliseconds

/**
 * Converts a [Long] representing a count of ticks into a [Duration]
 */
public inline val Long.ticks: Duration
    get() = (this * 50).milliseconds

/**
 * Converts a [Double] representing a count of
 * ticks (including fractional ticks) into a [Duration]
 */
public inline val Double.ticks: Duration
    get() = (this * 50.0).milliseconds

/**
 * Converts this [Duration] to the total number of whole ticks (floored)
 */
public inline val Duration.inWholeTicks: Long
    get() = this.inWholeMilliseconds / 50

/**
 * Converts this [Duration] to the number of ticks as a [Double],
 * preserving fractional ticks for high-precision time calculations
 */
public inline val Duration.inTicksDouble: Double
    get() = this.inWholeNanoseconds / 50_000_000.0