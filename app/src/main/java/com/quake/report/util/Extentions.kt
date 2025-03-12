package com.quake.report.util

import java.math.RoundingMode
import java.text.DecimalFormat
import java.util.Locale

fun Double.round(): Double {
    val df = DecimalFormat("#.#")
    df.roundingMode = RoundingMode.HALF_DOWN
    return df.format(this).toDouble()
}

fun String.subStringBeforeComma(): String {
    return this.substringBeforeLast(",").capitalize(Locale.ROOT)
}

fun String.subStringDetail(): String {
    if (this.substringAfter(",") == this) return this
    return this.substringBefore("-") + "-" + this.substringAfter(",")
}