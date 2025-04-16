package com.quake.report.screens

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Text
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.rememberTooltipState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jaikeerthick.composable_graphs.composables.bar.BarGraph
import com.jaikeerthick.composable_graphs.composables.bar.model.BarData
import com.jaikeerthick.composable_graphs.composables.bar.style.BarGraphColors
import com.jaikeerthick.composable_graphs.composables.bar.style.BarGraphFillType
import com.jaikeerthick.composable_graphs.composables.bar.style.BarGraphStyle
import com.jaikeerthick.composable_graphs.composables.bar.style.BarGraphVisibility
import com.jaikeerthick.composable_graphs.composables.line.LineGraph
import com.jaikeerthick.composable_graphs.composables.line.model.LineData
import com.jaikeerthick.composable_graphs.composables.line.style.LineGraphColors
import com.jaikeerthick.composable_graphs.composables.line.style.LineGraphFillType
import com.jaikeerthick.composable_graphs.composables.line.style.LineGraphStyle
import com.jaikeerthick.composable_graphs.composables.line.style.LineGraphVisibility
import com.quake.report.MainActivity
import com.quake.report.ui.theme.buttonColor
import com.quake.report.ui.theme.green1
import com.quake.report.ui.theme.lightYellow
import com.quake.report.ui.theme.magColor
import com.quake.report.ui.theme.magnitudeColor
import com.quake.report.ui.theme.yellow2
import com.quake.report.ui.theme.orange
import com.quake.report.ui.theme.pinTextColor
import com.quake.report.ui.theme.red
import com.quake.report.ui.theme.yellow1
import com.quake.report.util.getGraphNames
import com.quake.report.util.getLastFourWeekDates
import com.quake.report.util.getLastSixMonthsNames


@Composable
fun ChartDemoItems(
    items: List<@Composable () -> Unit>
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(lightYellow)
    ) {
        items(items) { chartItem ->
            chartItem()
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BottomNavGraphPage(action: () -> Unit) {


    // en uste belki filtreleme yada tekrar istek atacak bir fonksiyon
    // uzerine tiklama aksiyonu belirtilebilir


    //https://medium.com/@MrAndroid/tooltips-in-jetpack-compose-d7667cb5ae0d
    // tool tip grafige basildiginda
    // https://github.com/skydoves/Balloon#balloon-in-jetpack-compose-1
    // bu da library daha kolay olabilir


    // count list ve getLastweek tarihler ve datalar direk oturuyo index 0 ile index 0
    // count datalari ve tarihler oturuyor start  time olarak, ilk tablodu gunleri kullanabilirizx

    // basliklar ekle, tooltip, en alta tiklayinca gosterildigini belirt.

    val context = LocalContext.current
    val data = MainActivity.countList
    val fourWeeks = MainActivity.fourWeeksCountList
    val monthlyData = MainActivity.monthlyCountList



    val fourWeeksData = listOf(
        LineData(x = "", y = fourWeeks[0]),
        LineData(x = "", y = fourWeeks[1]),
        LineData(x = "", y = fourWeeks[2]),
        LineData(x = "This week", y = fourWeeks[3]),
    )
    val weeklyData = listOf(
        BarData(x = getGraphNames()[0], y = data[0]),
        BarData(x = getGraphNames()[1], y = data[1]),
        BarData(x = getGraphNames()[2], y = data[2]),
        BarData(x = getGraphNames()[3], y = data[3]),
        BarData(x = getGraphNames()[4], y = data[4]),
        BarData(x = getGraphNames()[5], y = data[5]),
        BarData(x = getGraphNames()[6], y = data[6])
    )

    val monthData = listOf(
        BarData(x = getLastSixMonthsNames()[0], y = monthlyData[1]),
        BarData(x = getLastSixMonthsNames()[1], y = monthlyData[2]),
        BarData(x = getLastSixMonthsNames()[2], y = monthlyData[3]),
        BarData(x = getLastSixMonthsNames()[3], y = monthlyData[4]),
        BarData(x = getLastSixMonthsNames()[4], y = monthlyData[5]),
        BarData(x = getLastSixMonthsNames()[5], y = monthlyData[6]),
    )


    ChartDemoItems(
        listOf {

            Row(
                verticalAlignment = Alignment.Top, horizontalArrangement = Arrangement.Start,
                modifier = Modifier.padding(horizontal = 20.dp, vertical = 30.dp)
            ) {
                Text(
                    text = "Earthquake Counts Worldwide", fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Row(
                verticalAlignment = Alignment.Top, horizontalArrangement = Arrangement.Start,
                modifier = Modifier.padding(20.dp)
            ) {
                Text(
                    text = "Last Week", fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
            }
            BarGraph(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 16.dp),
                data = weeklyData,
                style = BarGraphStyle(
                    colors = BarGraphColors(
                        fillType = BarGraphFillType.Gradient(
                            Brush.verticalGradient(
                                listOf(
                                    buttonColor, green1
                                )
                            )
                        )
                    ),
                    visibility = BarGraphVisibility(
                        isGridVisible = true,
                        isYAxisLabelVisible = true
                    )
                ),
                onBarClick = { value: BarData ->
                    Toast.makeText(context, value.y.toString(), Toast.LENGTH_LONG).show()
                },
            )
            Row(
                verticalAlignment = Alignment.Top, horizontalArrangement = Arrangement.Start,
                modifier = Modifier.padding(20.dp)
            ) {
                Text(
                    text = "Last Month", fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            LineGraph(
                modifier = Modifier
                    .padding(horizontal = 16.dp, vertical = 16.dp),
                data = fourWeeksData,
                style = LineGraphStyle(
                    LineGraphColors(
                        lineColor = magColor,
                        pointColor = magColor,
                        crossHairColor = magColor,
                        clickHighlightColor = magColor,
                        xAxisTextColor = Color.Black,
                        yAxisTextColor = Color.Black,
                        fillType =

                        // LineGraphFillType.Solid(lightYellow)

                        LineGraphFillType.Gradient(
                            brush = Brush.verticalGradient(
                                listOf(
                                    buttonColor, green1
                                )
                            )
                        )


                    ),
                    visibility = LineGraphVisibility(
                       isYAxisLabelVisible = true,
                        isGridVisible = true,
                        isCrossHairVisible = false
                    )
                ),
                onPointClick = { value: LineData ->
                    Toast.makeText(context, value.y.toString(), Toast.LENGTH_LONG).show()
                },
            )

            Row(
                verticalAlignment = Alignment.Top, horizontalArrangement = Arrangement.Start,
                modifier = Modifier.padding(20.dp)
            ) {
                Text(
                    text = "Last Six Months", fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            BarGraph(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 16.dp),
                data = monthData,
                style = BarGraphStyle(
                    colors = BarGraphColors(
                        fillType = BarGraphFillType.Gradient(
                            Brush.verticalGradient(
                                listOf(
                                    buttonColor, green1
                                )
                            )
                        )
                    ),
                    visibility = BarGraphVisibility(
                        isGridVisible = true,
                        isYAxisLabelVisible = true
                    )
                ),
                onBarClick = { value: BarData ->
                    Toast.makeText(context, value.y.toString(), Toast.LENGTH_LONG).show()
                }
            )
        }
    )


//
//     ChartDemoItems(
//        listOf(
//            {
//                BarChartView(
//                    dataSet = ChartDataSet(
//                        items = listOf(data[0].toFloat(), data[1].toFloat(), data[2].toFloat(), data[3].toFloat(),
//                            data[4].toFloat(),data[5].toFloat(),data[6].toFloat()),
//                        title = "Weekly Data",
//                        prefix = "count: "
//                    ),
//                    style = BarChartDefaults.style(
//                        chartViewStyle =  ChartViewDefaults.style(
//                            outerPadding = 10.dp,
//                            backgroundColor =  MaterialTheme.colorScheme.surfaceColorAtElevation(1.dp))
//                    )
//                )
//            },
//            {
//
//                val dataSet = ChartDataSet(
//                    items = listOf(data[0].toFloat(), data[1].toFloat(), data[2].toFloat(), data[3].toFloat(),
//                        data[4].toFloat(),data[5].toFloat(),data[6].toFloat()),
//                    title = "Weekly Data"
//                )
//
//                LineChartView(
//                    dataSet = dataSet,
//                    style = LineChartDefaults.style(
//                        lineColor = Color.Green,
//                        pointColor = Color.Red,
//                        pointSize = 9f,
//                        bezier = false,
//                        dragPointColor = Color.Magenta,
//                        dragPointVisible = false,
//                        dragPointSize = 8f,
//                        dragActivePointSize = 15f,
//                        chartViewStyle = ChartViewDefaults.style(
//                            outerPadding = 20.dp,
//                            backgroundColor = MaterialTheme.colorScheme.surfaceColorAtElevation(1.dp))
//                    )
//                )
//            }
//        )
//    )

//    BarChartView(
//        dataSet = ChartDataSet(
//            items = listOf(data[0].toFloat(), data[1].toFloat(), data[2].toFloat(), data[3].toFloat(),
//                data[4].toFloat(),data[5].toFloat(),data[6].toFloat()),
//            title = "Weekly Data",
//            prefix = "count: "
//        ),
//        style = BarChartDefaults.style(
//            chartViewStyle =  ChartViewDefaults.style(
//                outerPadding = 10.dp,
//                backgroundColor =  MaterialTheme.colorScheme.surfaceColorAtElevation(1.dp))
//        )
//    )


    // stacked bar chart
//    val items = listOf(
//        "Cherry St." to listOf(8261.68f, 8810.34f, 30000.57f),
//        "Strawberry Mall" to listOf(8261.68f, 8810.34f, 30000.57f),
//        "Lime Av." to listOf(1500.87f, 2765.58f, 33245.81f),
//        "Apple Rd." to listOf(5444.87f, 233.58f, 67544.81f)
//    )
//
//    val dataSet = MultiChartDataSet(
//        items = items,
//        prefix = "$",
//        categories = listOf("Jan", "Feb", "Mar"),
//        title = "Stacked Bar Chart"
//    )
//
//    StackedBarChartView(
//        dataSet = dataSet,
//        style = StackedBarChartDefaults.style(
//            chartViewStyle = ChartViewDefaults.style(
//                width = 240.dp,
//                backgroundColor =  MaterialTheme.colorScheme.surfaceColorAtElevation(1.dp))
//        )
}