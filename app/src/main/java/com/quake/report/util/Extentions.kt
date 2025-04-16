package com.quake.report.util

import java.math.RoundingMode
import java.util.Locale

fun Double.round(): Double {
    return this.toBigDecimal().setScale(1, RoundingMode.DOWN).toDouble()
}

fun String.subStringBeforeComma(): String {
    return this.substringBeforeLast(",").capitalize(Locale.ROOT)
}

fun String.subStringDetail(): String {
    if (this.substringAfter(",") == this) return this
    return this.substringBefore("-") + "-" + this.substringAfter(",")
}