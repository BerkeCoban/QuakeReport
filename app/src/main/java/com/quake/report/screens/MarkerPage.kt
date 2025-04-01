package com.quake.report.screens

import android.graphics.Paint
import android.graphics.drawable.Drawable
import android.location.Location
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDefaults
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.lifecycle.viewmodel.compose.viewModel
import com.quake.report.MainActivity.Companion.splashData
import com.quake.report.MainViewModel
import com.quake.report.R
import com.quake.report.data.model.UiResponse
import com.quake.report.osm.MarkerLabeled
import com.quake.report.osm.OpenStreetMap
import com.quake.report.osm.model.LabelProperties
import com.quake.report.osm.rememberCameraState
import com.quake.report.osm.rememberMarkerState
import com.quake.report.osm.rememberOverlayManagerState
import com.quake.report.screens.components.LineSlider
import com.quake.report.ui.theme.buttonColor
import com.quake.report.ui.theme.green1
import com.quake.report.ui.theme.lightYellow
import com.quake.report.ui.theme.pinTextColor
import com.quake.report.util.Constants.CLUSTER_RANGE
import com.quake.report.util.PastOrPresentSelectableDates
import com.quake.report.util.convertDateWithoutHours
import com.quake.report.util.getToday
import com.quake.report.util.round
import org.osmdroid.util.GeoPoint


@Composable
fun MarkerPage() {
    val viewModel = viewModel<MainViewModel>()
    val showDialog = viewModel.emptyData.collectAsState()
    val showProgressDialog = viewModel.progressDialog.collectAsState()

    BackHandler(true) {}
    val overlayManagerState = rememberOverlayManagerState()
    val context = LocalContext.current

    val pinIcon: Drawable? by remember {
        mutableStateOf(context.getDrawable(R.drawable.pin_2))
    }

    val labelProperties = remember {
        mutableStateOf(
            LabelProperties(
                labelColor = pinTextColor.toArgb(),
                labelTextSize = 40f,
                labelAlign = Paint.Align.CENTER,
                labelTextOffset = 30f
            )
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(buttonColor)
    ) {
        Surface(
            shape = RoundedCornerShape(corner = CornerSize(20.dp)),
            border = BorderStroke(width = 4.dp, color = buttonColor)
        ) {
            SearchBox(viewModel)
        }
        key(viewModel.splashData.observeAsState().value) {
            viewModel.updateProgressValue(false)
            val data = splashData
            val clusterIndexList = getClusterGroups(splashData)

            val cameraState = rememberCameraState {
                geoPoint = GeoPoint(data[0].latitude!!, data[0].longitude!!)
                zoom = 0.0
            }
            Surface {
                OpenStreetMap(
                    cameraState = cameraState,
                    overlayManagerState = overlayManagerState
                ) {
                    data.forEachIndexed { index, markerData ->
                        if (isMarkerValid(index, clusterIndexList)) {
                            markerData.latitude?.let {
                                markerData.longitude?.let { it1 ->
                                    GeoPoint(
                                        it,
                                        it1
                                    )
                                }
                            }?.let {
                                rememberMarkerState(
                                    geoPoint = it,
                                    rotation = 0f
                                )
                            }?.let {
                                val label =
                                    if (clusterIndexList[index]?.isNotEmpty() == true) "earthquake group" else
                                        markerData.magnitude?.round().toString()
                                MarkerLabeled(
                                    state = it,
                                    icon = pinIcon,
                                    title = markerData.title,
                                    snippet = markerData.place,
                                    label = label,
                                    labelProperties = labelProperties.value
                                ) {
                                    if (clusterIndexList[index]?.isNotEmpty() == true) {
                                        val clusterGroup = clusterIndexList[index]
                                        val clusterList = arrayListOf<UiResponse>()
                                        clusterList.add(splashData[index])
                                        clusterGroup?.forEach {
                                            clusterList.add(splashData[it])
                                        }
                                        LazyColumn(
                                            modifier = Modifier
                                                .padding(5.dp)
                                                .heightIn(0.dp, 250.dp)
                                                .widthIn(0.dp, 350.dp)
                                                .background(
                                                    color = White,
                                                    shape = RoundedCornerShape(7.dp)
                                                ),
                                            verticalArrangement = Arrangement.Center,
                                            horizontalAlignment = Alignment.CenterHorizontally
                                        ) {
                                            items(items = clusterList) {
                                                Text(
                                                    modifier = Modifier.padding(horizontal = 10.dp),
                                                    text = it.title.toString()
                                                )
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
    if (showDialog.value) {
        AlertDialog(
            onDismissRequest = {
            },
            title = { Text(text = "Error!") },
            text = { Text(text = "There is no earthquake data for selected filters.") },
            confirmButton = {
                Button(
                    onClick = {
                        viewModel.updateEmptyData(false)
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = buttonColor)
                ) {
                    Text(
                        text = "OK",
                        color = White
                    )
                }
            }
        )
    }
    if (showProgressDialog.value) {
        Dialog(
            onDismissRequest = {},
            DialogProperties(dismissOnBackPress = false, dismissOnClickOutside = false)
        ) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .size(120.dp)
                    .background(White, shape = RoundedCornerShape(8.dp))
            ) {
                Pulsating {
                    Image(
                        modifier = Modifier.size(100.dp),
                        imageVector = ImageVector.vectorResource(id = R.drawable.earthquake_icon_2),
                        contentDescription = null,
                    )
                }
            }
        }
    }
}

fun isMarkerValid(
    index: Int,
    clusterIndexList: HashMap<Int, ArrayList<Int>>
): Boolean {
    for ((key, value) in clusterIndexList) {
        value.forEach {
            if (index == it) {
                return false
            }
        }
    }
    return true
}

fun getClusterGroups(data: ArrayList<UiResponse>): HashMap<Int, ArrayList<Int>> {
    val results = FloatArray(1)
    val indexList = arrayListOf<Int>()
    val clusterHashMap = hashMapOf<Int, ArrayList<Int>>()
    val duplicateList = arrayListOf<Int>()
    data.forEachIndexed { index, item ->
        data.forEachIndexed { innerIndex, innerItem ->
            if (index != innerIndex) {
                Location.distanceBetween(
                    innerItem.latitude!!,
                    innerItem.longitude!!,
                    item.latitude!!,
                    item.longitude!!, results
                )
                val distance = try {
                    Integer.valueOf(
                        results[0].toInt().toString().substringBefore("E")
                    )
                } catch (e: Exception) {
                    0
                }
                if (distance < CLUSTER_RANGE) {
                    indexList.add(innerIndex)
                }
            }
        }
        if (indexList.size > 0) {
            val list = arrayListOf<Int>().apply { addAll(indexList) }
            if (!duplicateList.contains(index)) {
                clusterHashMap[index] = list
                list.forEach { indices ->
                    if (!duplicateList.contains(indices)) duplicateList.add(indices)
                }
            }
        }
        indexList.clear()
    }
    return clusterHashMap
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchBox(viewModel: MainViewModel) {
    val dateState = rememberDatePickerState(
        selectableDates = PastOrPresentSelectableDates
    )
    var showDialog by remember { mutableStateOf(false) }
    var magnitudeValue by rememberSaveable { mutableFloatStateOf(.3f) }
    var dateText by rememberSaveable { mutableStateOf(getToday()) }
    var selectedDateEpoch by remember { mutableStateOf(0L) }
    // hide show bunun uzunluguna bagli
    var cardHeight by rememberSaveable { mutableIntStateOf(200) }
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(cardHeight.dp)
            .background(green1),
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(vertical = 15.dp),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (showDialog) {
                DatePickerDialog(
                    onDismissRequest = { showDialog = false },
                    colors = DatePickerDefaults.colors(containerColor = lightYellow),
                    confirmButton = {
                        Button(
                            onClick = {
                                showDialog = false
                                if (dateState.selectedDateMillis != null) {
                                    dateText = convertDateWithoutHours(
                                        (dateState.selectedDateMillis?.plus(
                                            86400000
                                        )).toString()
                                    )
                                    selectedDateEpoch = dateState.selectedDateMillis?.plus(
                                        86400000
                                    )!!
                                }
                            }
                        ) {
                            Text(text = "OK")
                        }
                    },
                    dismissButton = {
                        Button(
                            onClick = {
                                showDialog = false
                            }
                        ) {
                            Text(text = "Cancel")
                        }
                    }
                ) {
                    DatePicker(
                        state = dateState,
                        showModeToggle = true
                    )
                }
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(15.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    modifier = Modifier
                        .padding(end = 10.dp)
                        .clickable {
                            showDialog = true
                        },
                    painter = painterResource(R.drawable.calendar_icon),
                    contentDescription = null
                )

                Surface(
                    modifier = Modifier.weight(0.6f),
                    shape = RoundedCornerShape(corner = CornerSize(5.dp)),
                    border = BorderStroke(width = 1.dp, color = lightYellow),
                    color = lightYellow
                ) {
                    Text(
                        text = dateText,
                        Modifier
                            .clickable {
                                showDialog = true
                            }
                            .align(Alignment.CenterVertically)
                            .height(24.dp)
                            .padding(top = 4.dp, start = 5.dp),
                    )
                }

                Text(
                    text = if (cardHeight == 200) "Hide" else "Show", modifier = Modifier
                        .padding(start = 10.dp)
                        .clickable {
                            cardHeight = if (cardHeight == 200) 85
                            else 200
                        }
                )
            }
            if (cardHeight == 200) {

                Row(
                    modifier = Modifier
                        .padding(horizontal = 20.dp)
                        .fillMaxWidth(), verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Min Magnitude: ", fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )
                    LineSlider(
                        value = magnitudeValue,
                        onValueChange = {
                            magnitudeValue = it
                        },
                        modifier = Modifier
                            .padding(start = 10.dp)
                            .fillMaxWidth(),
                        steps = 10,
                        thumbDisplay = { (it * 10).toInt().toString() }
                    )
                }


                Button(
                    onClick = {
                        viewModel.updateProgressValue(true)
                        if (dateState.selectedDateMillis == null) {

                            viewModel.getSplashData(
                                startTime = getToday(),
                                minMagnitude = (magnitudeValue * 10).toInt().toString()
                            )
                        } else {
                            viewModel.getFilteredHomeData(
                                startTime = convertDateWithoutHours(
                                    selectedDateEpoch.toString()
                                ),
                                endTime = convertDateWithoutHours(
                                    (selectedDateEpoch.plus(
                                        86400000
                                    )).toString()
                                ),
                                minMagnitude = (magnitudeValue * 10).toInt().toString()
                            )
                        }
                    }, shape = RoundedCornerShape(50),
                    colors = ButtonDefaults.buttonColors(containerColor = buttonColor)
                )
                {
                    Text(text = "Search")
                }
            }
        }
    }
}