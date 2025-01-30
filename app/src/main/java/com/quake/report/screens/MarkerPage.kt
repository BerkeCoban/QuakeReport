package com.quake.report.screens

import android.graphics.Color
import android.graphics.Paint
import android.graphics.drawable.Drawable
import android.location.Location
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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
import com.quake.report.util.Constants.CLUSTER_RANGE
import com.quake.report.util.convertDateWithoutHours
import org.osmdroid.util.GeoPoint


@Composable
fun MarkerPage() {

    val viewModel = viewModel<MainViewModel>()

    // state konusu , ilk giriste de min mag 3 ve bugunun tarihi yaziyor olacak ilk istek splash
    // stateler tarih text, min magnitude, map zoom, hide show
    // https://stackoverflow.com/questions/72913451/how-to-save-and-restore-navigation-state-in-jetpack-compose


    // cluster olayini cozduk , ancak tek marker olarak ekledigimiz grupta, kalamnlari da goster
    // ustune basildiginda siddetler gosterilebilir, ayrica basildigindaki ui duzelt 168


    // hint duzeltilebilir click to select date gibi
    // https://stackoverflow.com/questions/75377259/how-to-change-datepicker-dialog-color-in-jetpack-compose
    // servis  min mag 4.5 seklinde calisiyor belki kusuratli filtreleme ekleyebiliriz.
    // tarihe hic dokunmadan ok basinca null basiyor
    // en yuksek siddetteki depreme kamerayi alabiliriz.
    // cluster sorunu ve markera basinca cikan dialog , marker gorunumu
    // butona surekli basilmasin istek atma olayi
    // loading cikarma servislerde
    // tarih sectikten sonra bottomdan gidip gelince kalmiyor sanki kaliyor ama textte tarih gidiyor gibi kontrol et
    // tarih secilmedi ise istek attirma uyari ver
    // buton ismi go mu kalsin
    // TARIH secip go dedikten sonra veri gelmesse uyari goster
    // min magnitude 0 a alinca okunmuyor
    // markerda magnitude 2 den fazla virgulle gelebiliyor teke indir

    val overlayManagerState = rememberOverlayManagerState()
    val context = LocalContext.current

    val pinIcon: Drawable? by remember {
        mutableStateOf(context.getDrawable(R.drawable.pin))
    }

    val labelProperties = remember {
        mutableStateOf(
            LabelProperties(
                labelColor = Color.RED,
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
                                MarkerLabeled(
                                    state = it,
                                    icon = pinIcon,
                                    title = markerData.title,
                                    snippet = markerData.place,
                                    label = markerData.magnitude.toString(),
                                    labelProperties = labelProperties.value
                                ) {
                                    Column(
                                        modifier = Modifier
                                            .size(100.dp)
                                            .background(
                                                color = androidx.compose.ui.graphics.Color.Gray,
                                                shape = RoundedCornerShape(7.dp)
                                            ),
                                        verticalArrangement = Arrangement.Center,
                                        horizontalAlignment = Alignment.CenterHorizontally
                                    ) {
                                        Text(text = it.title)
                                        Text(text = it.snippet, fontSize = 10.sp)
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
    val dateState = rememberDatePickerState()
    var showDialog by remember { mutableStateOf(false) }
    var magnitudeValue by remember { mutableFloatStateOf(.4f) }
    var dateText by remember { mutableStateOf("yyyy-mm-dd") }
    var selectedDateEpoch by remember { mutableStateOf(0L) }
    // hide show bunun uzunluguna bagli
    var cardHeight by remember { mutableIntStateOf(200) }

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
                    confirmButton = {
                        Button(
                            onClick = {
                                showDialog = false
                                dateText = convertDateWithoutHours(
                                    (dateState.selectedDateMillis?.plus(
                                        86400000
                                    )).toString()
                                )

                                selectedDateEpoch = dateState.selectedDateMillis?.plus(
                                    86400000
                                )!!

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
                        .padding(horizontal = 30.dp)
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
                            .fillMaxWidth(),
                        steps = 10,
                        thumbDisplay = { (it * 10).toInt().toString() }
                    )
                }


                Button(
                    onClick = {
                        // 86400000 fark mili saniye her gun arasinda

                        viewModel.getFilteredHomeData(
                            startTime = convertDateWithoutHours(
                                selectedDateEpoch.toString()
                            ),
                            endTime = convertDateWithoutHours(
                                (selectedDateEpoch.plus(
                                    86400000
                                )).toString()
                            ),
                            minMagnitude = (magnitudeValue * 10).toString()
                        )
                    }, shape = RoundedCornerShape(50),
                    colors = ButtonDefaults.buttonColors(containerColor = buttonColor)
                )
                {
                    Text(text = "Go")
                }
            }
        }
    }
}