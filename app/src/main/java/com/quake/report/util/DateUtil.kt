package com.quake.report.util

import android.text.format.DateFormat
import com.quake.report.util.Constants.DATE_FORMAT
import com.quake.report.util.Constants.DATE_FORMAT_DAY
import com.quake.report.util.Constants.LOCALE
import java.math.RoundingMode
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale


fun getToday(): String {
    return SimpleDateFormat(DATE_FORMAT, Locale.getDefault()).format(Calendar.getInstance().time)
}

fun getYesterday(): String {
    val cal: Calendar = Calendar.getInstance()
    cal.add(Calendar.DAY_OF_YEAR, -1)
    return SimpleDateFormat(DATE_FORMAT, Locale.getDefault()).format(cal.time)
}

fun getLastFourWeekDates(): ArrayList<String> {
    // 5 tarih donuyor sonuncusu bugun
    val cal: Calendar = Calendar.getInstance()
    val dates: ArrayList<String> = arrayListOf()
    cal.add(Calendar.DAY_OF_YEAR, -28)
    for (i in 0 until 5) {
        dates.add(SimpleDateFormat(DATE_FORMAT, Locale.getDefault()).format(cal.time))
        cal.add(Calendar.DAY_OF_YEAR, 7)
    }
    return dates
}

fun getLastSixMonths(): ArrayList<String> {
    val cal: Calendar = Calendar.getInstance()
    val dates: ArrayList<String> = arrayListOf()
    cal.add(Calendar.MONTH, -6)
    for (i in 0 until 7) {
        dates.add(SimpleDateFormat(DATE_FORMAT, Locale.getDefault()).format(cal.time))
        cal.add(Calendar.MONTH, 1)
    }
    return dates
}

fun getLastWeek(): ArrayList<String> {
    val dates: ArrayList<String> = arrayListOf()
    val cal: Calendar = Calendar.getInstance()
    cal.add(Calendar.DAY_OF_YEAR, -6)
    dates.add(SimpleDateFormat(DATE_FORMAT, Locale.getDefault()).format(cal.time))
    for (i in 0 until 6) {
        cal.add(Calendar.DAY_OF_YEAR, 1)
        dates.add(SimpleDateFormat(DATE_FORMAT, Locale.getDefault()).format(cal.time))
    }
    return dates
}

fun getPreviousLastWeek(): ArrayList<String> {
    val dates: ArrayList<String> = arrayListOf()
    val cal: Calendar = Calendar.getInstance()
    cal.add(Calendar.DAY_OF_YEAR, -14)
    dates.add(SimpleDateFormat(DATE_FORMAT, Locale.getDefault()).format(cal.time))
    for (i in 0 until 7) {
        cal.add(Calendar.DAY_OF_YEAR, 1)
        dates.add(SimpleDateFormat(DATE_FORMAT, Locale.getDefault()).format(cal.time))
    }
    return dates
}

fun getLastTwoWeek(): ArrayList<String> {
    val dates: ArrayList<String> = arrayListOf()
    val cal: Calendar = Calendar.getInstance()
    cal.add(Calendar.DAY_OF_YEAR, -14)
    dates.add(SimpleDateFormat(DATE_FORMAT, Locale.getDefault()).format(cal.time))
    for (i in 0 until 14) {
        cal.add(Calendar.DAY_OF_YEAR, 1)
        dates.add(SimpleDateFormat(DATE_FORMAT, Locale.getDefault()).format(cal.time))
    }
    return dates
}

fun getGraphNames(): ArrayList<String> {
    val dates: ArrayList<String> = arrayListOf()
    val cal = Calendar.getInstance(Locale(LOCALE))
    cal.add(Calendar.DAY_OF_YEAR, -6)
    dates.add(DateFormat.format(DATE_FORMAT_DAY, cal).toString().take(3))
    for (i in 0 until 6) {
        cal.add(Calendar.DAY_OF_YEAR, 1)
        dates.add(DateFormat.format(DATE_FORMAT_DAY, cal).toString().take(3))
    }
    return dates
}

fun convertDate(dateInMilliseconds: String): String {
    return DateFormat.format(
        "E yyyy-MM-dd 'at' hh:mm:ss a zzz",
        dateInMilliseconds.substringBefore("E").replace(".", "").toLong()
    ).toString()
}

fun convertDateHours(dateInMilliseconds: String): String {
    return DateFormat.format(
        "'at' hh:mm a zzz",
        dateInMilliseconds.substringBefore("E").replace(".", "").toLong()
    ).toString()
}

fun convertDateWithoutHours(dateInMilliseconds: String): String {
    return DateFormat.format(
        "yyyy-MM-dd",
        dateInMilliseconds.substringBefore("E").replace(".", "").toLong()
    ).toString()
}

fun roundOffDecimal(number: Double): Double {
    val df = DecimalFormat("#.#")
    df.roundingMode = RoundingMode.CEILING
    return df.format(number).toDouble()
}