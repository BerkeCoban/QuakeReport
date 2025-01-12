package com.quake.report.screens

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.jaikeerthick.composable_graphs.composables.bar.BarGraph
import com.jaikeerthick.composable_graphs.composables.bar.model.BarData
import com.jaikeerthick.composable_graphs.composables.bar.style.BarGraphColors
import com.jaikeerthick.composable_graphs.composables.bar.style.BarGraphFillType
import com.jaikeerthick.composable_graphs.composables.bar.style.BarGraphStyle
import com.jaikeerthick.composable_graphs.composables.line.LineGraph
import com.jaikeerthick.composable_graphs.composables.line.model.LineData
import com.jaikeerthick.composable_graphs.composables.line.style.LineGraphColors
import com.jaikeerthick.composable_graphs.composables.line.style.LineGraphFillType
import com.jaikeerthick.composable_graphs.composables.line.style.LineGraphStyle
import com.quake.report.MainActivity
import com.quake.report.ui.theme.lightYellow
import com.quake.report.ui.theme.yellow2
import com.quake.report.ui.theme.orange
import com.quake.report.ui.theme.red
import com.quake.report.util.getGraphNames


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

@Composable
fun BottomNavGraph(action: () -> Unit) {
// altina baslik text yazilabilir haftalik rapor gibi
    // en uste belki filtreleme yada tekrar istek atacak bir fonksiyon
// uzerine tiklama aksiyonu belirtilebilir
    // getGraphNames() gunleri cekmek icin tabloya yaz
    // grafik verisine tiklayinca toast ile count goster yada kucuk dialog gibi birsey olabilir


    // count list ve getLastweek tarihler ve datalar direk oturuyo index 0 ile index 0
    // count datalari ve tarihler oturuyor start  time olarak, ilk tablodu gunleri kullanabilirizx
    //  getLastFourWeekDates getLastSixMonths getLastWeek
    // 3 grafik yapariz 1 tane line graph 4 hafta verisi
    // basliklar ekle




    action.invoke()
    val data = MainActivity.countList



    val data2 = listOf(
        LineData(x = getGraphNames()[0], y = data[0]),
        LineData(x = getGraphNames()[1], y = data[1]),
        LineData(x = getGraphNames()[2], y = data[2]),
        LineData(x = getGraphNames()[3], y = data[3]),
        LineData(x = getGraphNames()[4], y = data[4]),
        LineData(x = getGraphNames()[5], y = data[5]),
        LineData(x = getGraphNames()[6], y = data[6]),
    )

    val data3 = listOf(
        BarData(x = "Mon", y = data[0]),
        BarData(x = "Tue", y = data[1]),
        BarData(x = "Wed", y = data[2]),
        BarData(x = "Thu", y = data[3]),
        BarData(x = "Fri", y = data[4]),
        BarData(x = "Sat", y = data[5]),
        BarData(x = "Sun", y = data[6])
    )


    ChartDemoItems(
        listOf {
            BarGraph(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 16.dp),
                data = data3,
                style = BarGraphStyle(
                    colors = BarGraphColors(
                        fillType = BarGraphFillType.Gradient(
                            Brush.verticalGradient(
                                listOf(
                                    red, orange
                                )
                            )
                        )
                    )
                )
            )

            LineGraph(
                modifier = Modifier
                    .padding(horizontal = 16.dp, vertical = 16.dp),
                data = data2,
                style = LineGraphStyle(
                    LineGraphColors(
                        lineColor = red,
                        pointColor = red,
                        crossHairColor = Color.Red,
                        clickHighlightColor = yellow2,
                        xAxisTextColor = Color.Black,
                        yAxisTextColor = Color.Black,
                        fillType =

                        // LineGraphFillType.Solid(lightYellow)

                        LineGraphFillType.Gradient(
                            brush = Brush.verticalGradient(
                                listOf(
                                    orange, yellow2
                                )
                            )
                        )


                    )
//                    val colors: LineGraphColors = LineGraphColors(),
//                val visibility: LineGraphVisibility = LineGraphVisibility(),
//            val yAxisLabelPosition: LabelPosition = LabelPosition.RIGHT
                ),
                onPointClick = { value: LineData ->
                    Log.d("", "")
                },
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