package com.quake.report.util

import android.os.Build
import android.text.format.DateFormat
import androidx.annotation.RequiresApi
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SelectableDates
import com.quake.report.util.Constants.DATE_FORMAT
import com.quake.report.util.Constants.DATE_FORMAT_DAY
import com.quake.report.util.Constants.DATE_FORMAT_MONTH
import com.quake.report.util.Constants.LOCALE
import java.math.RoundingMode
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlinx.datetime.todayIn


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
    val today = SimpleDateFormat(DATE_FORMAT, Locale.getDefault()).format(cal.time)
    val dates: ArrayList<String> = arrayListOf()
    cal.set(Calendar.DAY_OF_MONTH, 1)
    cal.add(Calendar.MONTH, -5)
    for (i in 0 until 6) {
        dates.add(SimpleDateFormat(DATE_FORMAT, Locale.getDefault()).format(cal.time))
        cal.add(Calendar.MONTH, 1)
    }
    dates.add(today)
    return dates
}

fun getLastSixMonthsNames(): ArrayList<String> {
    val cal: Calendar = Calendar.getInstance()
    val dates: ArrayList<String> = arrayListOf()
    cal.add(Calendar.MONTH, -5)
    for (i in 0 until 7) {
        dates.add(SimpleDateFormat(DATE_FORMAT_MONTH, Locale.getDefault()).format(cal.time))
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


fun fromEpochMillis(epochMillis: Long): LocalDate = Instant.fromEpochMilliseconds(epochMillis)
    .toLocalDateTime(TimeZone.UTC)
    .date

fun today(): LocalDate = Clock.System.todayIn(TimeZone.currentSystemDefault())

@OptIn(ExperimentalMaterial3Api::class)
object PastOrPresentSelectableDates: SelectableDates {
    override fun isSelectableDate(utcTimeMillis: Long): Boolean {
        return fromEpochMillis(utcTimeMillis) <= today()
    }

    @ExperimentalMaterial3Api
    override fun isSelectableYear(year: Int): Boolean {
        return true
    }
}