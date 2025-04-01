package com.quake.report

import android.os.Bundle
import android.view.View
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.quake.report.data.ResponseMapper
import com.quake.report.data.model.UiResponse
import com.quake.report.navigation.Screen
import com.quake.report.navigation.SetupNavGraph
import com.quake.report.util.getLastFourWeekDates
import com.quake.report.util.getLastWeek
import com.quake.report.util.getLastSixMonths
import com.quake.report.util.getToday

class MainActivity : ComponentActivity() {
    private val viewModel: MainViewModel by viewModels()
    private lateinit var navController: NavHostController
    private lateinit var navControllerBottom: NavHostController
    private var isTodayFetched: Boolean = false
    private var isCountDataFetched: Boolean = false
    private var isFourWeeksFetched: Boolean = false
    private var isMonthlyCountDataFetched: Boolean = false

    // uygulama ikon

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        fetchToday()
        fetchLastSixMonths()
        fetchCountLastWeek()
        fetchCountFourWeeks()
        setContent {
            navController = rememberNavController()
            navControllerBottom = rememberNavController()
            SetupNavGraph(navController = navController, navControllerBottom = navControllerBottom) {
                finish()
            }
        }
    }

    override fun onResume() {
        super.onResume()
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN
    }

    override fun onStart() {
        super.onStart()
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN
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