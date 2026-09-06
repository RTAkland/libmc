/*
 * Copyright © 2026 RTAkland
 * Author: RTAkland
 * Date: 2026/9/6
 */


package cn.rtast.libmc.protocol.tick

import kotlin.time.Duration
import kotlin.time.Duration.Companion.milliseconds

/**
 * Convert an int value to minecraft tick
 */
public inline val Int.ticks: Duration
    get() = (this * 50).milliseconds

/**
 * Convert a long value to minecraft tick
 */
public inline val Long.ticks: Duration
    get() = (this * 50).milliseconds

/**
 * Convert a double value to minecraft tick
 */
public inline val Double.ticks: Duration
    get() = (this * 50.0).milliseconds

/**
 * Convert [Duration] to ticks([Long])
 */
public inline val Duration.inWholeTicks: Long
    get() = this.inWholeMilliseconds / 50

/**
 * Convert [Duration] to ticks([Double])
 */
public inline val Duration.inTicksDouble: Double
    get() = this.inWholeNanoseconds / 50_000_000.0