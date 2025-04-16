package com.quake.report

import android.app.Activity
import android.os.Build
import android.os.Bundle
import android.view.Window
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.quake.report.data.ResponseMapper
import com.quake.report.data.model.UiResponse
import com.quake.report.navigation.Screen
import com.quake.report.navigation.SetupNavGraph
import com.quake.report.ui.theme.lightYellow
import com.quake.report.util.getLastFourWeekDates
import com.quake.report.util.getLastSixMonths
import com.quake.report.util.getLastWeek
import com.quake.report.util.getToday

class MainActivity : ComponentActivity() {
    private val viewModel: MainViewModel by viewModels()
    private lateinit var navController: NavHostController
    private lateinit var navControllerBottom: NavHostController
    private var isTodayFetched: Boolean = false
    private var isCountDataFetched: Boolean = false
    private var isFourWeeksFetched: Boolean = false
    private var isMonthlyCountDataFetched: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        fetchToday()
        fetchLastSixMonths()
        fetchCountLastWeek()
        fetchCountFourWeeks()
        setContent {
            Box(
                modifier = Modifier
                    .systemBarsPadding()
            ) {
                navController = rememberNavController()
                navControllerBottom = rememberNavController()
                SetupNavGraph(
                    navController = navController,
                    navControllerBottom = navControllerBottom
                ) {
                    finish()
                }
                SetColor()
            }
        }
    }

    @Composable
    fun SetColor() {
        val view = LocalView.current
        if (!view.isInEditMode) {
            LaunchedEffect(key1 = true) {
                val window = (view.context as Activity).window
                window.statusBarColor = lightYellow.toArgb()
                val wic = WindowCompat.getInsetsController(window, window.decorView)
                wic.isAppearanceLightStatusBars = true
            }
        }
        setStatusBarColor(window, lightYellow.toArgb())
    }

    private fun setStatusBarColor(window: Window, color: Int) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.VANILLA_ICE_CREAM) { // Android 15+
            window.decorView.setOnApplyWindowInsetsListener { view, insets ->
                //    val statusBarInsets = insets.getInsets(WindowInsets.Type.statusBars())
                view.setBackgroundColor(color)
                // Adjust padding to avoid overlap
                //  view.setPadding(0, statusBarInsets.top, 0, 0)
                insets
            }
        }
    }

    private fun fetchCountLastWeek() {
        val dates = getLastWeek()
        var dateCounter = 1
        viewModel.countData.observe(this) { data ->
            when (dateCounter) {
                6 -> {
                    countList.add(data)
                    viewModel.getCountDataToday(getToday(), "0")
                    dateCounter++
                }

                7 -> {
                    countList.add(data)
                    isCountDataFetched = true
                    navigateToHome()
                }

                else -> {
                    countList.add(data)
                    viewModel.getCountData(
                        startTime = dates[dateCounter],
                        endTime = dates[dateCounter + 1]
                    )
                    dateCounter++
                }
            }

        }
        viewModel.getCountData(startTime = dates[0], endTime = dates[1])
    }


    private fun fetchToday() {
        viewModel.splashData.observe(this) { data ->
            splashData = ResponseMapper.map(data)
            isTodayFetched = true
            navigateToHome()

        }

        viewModel.getSplashData(
            startTime = getToday(),
            minMagnitude = "3"
        )
    }

    private fun fetchCountFourWeeks() {

        val weekDates = getLastFourWeekDates()

        var dateCounter = 1


        viewModel.secondCountData.observe(this) { data ->
            when (dateCounter) {
                1, 2, 3 -> {
                    fourWeeksCountList.add(data)
                    viewModel.getSecondCountData(
                        startTime = weekDates[dateCounter],
                        endTime = weekDates[dateCounter + 1]
                    )
                    dateCounter++
                }

                else -> {
                    fourWeeksCountList.add(data)
                    isFourWeeksFetched = true
                    navigateToHome()
                }
            }

        }
        viewModel.getSecondCountData(startTime = weekDates[0], endTime = weekDates[1])
    }

    private fun fetchLastSixMonths() {

        val monthDates = getLastSixMonths()
        var monthCounter = 0

        viewModel.monthlyCountData.observe(this) { data ->
            when (monthCounter) {
                6 -> {
                    monthlyCountList.add(data)
                    isMonthlyCountDataFetched = true
                    navigateToHome()
                }

                else -> {
                    monthlyCountList.add(data)
                    viewModel.getMonthlyCountData(
                        startTime = monthDates[monthCounter],
                        endTime = monthDates[monthCounter + 1]
                    )
                    monthCounter++
                }
            }

        }
        viewModel.getMonthlyCountData(startTime = monthDates[0], endTime = monthDates[1])
    }

    private fun navigateToHome() {
        if (isTodayFetched && isCountDataFetched && isFourWeeksFetched && isMonthlyCountDataFetched) {
            navController.navigate(route = Screen.Home.route)
        }
    }

    companion object {
        var splashData: ArrayList<UiResponse> = arrayListOf()
        var countList: ArrayList<Int> = arrayListOf()
        var fourWeeksCountList: ArrayList<Int> = arrayListOf()
        var monthlyCountList: ArrayList<Int> = arrayListOf()
    }

}