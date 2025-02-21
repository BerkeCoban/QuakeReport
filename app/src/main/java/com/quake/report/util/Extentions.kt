package com.quake.report.util

import java.math.RoundingMode
import java.text.DecimalFormat

fun Double.round(): Double {
    val df = DecimalFormat("#.#")
    df.roundingMode = RoundingMode.HALF_DOWN
    return df.format(this).toDouble()
}