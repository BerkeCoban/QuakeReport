package com.quake.report.screens

import android.content.Intent
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.GenericShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.layout.layout
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.offset
import androidx.compose.ui.unit.sp
import com.quake.report.data.model.UiResponse
import com.quake.report.screens.components.CustomDialog
import com.quake.report.ui.theme.green1
import com.quake.report.ui.theme.lightYellow
import com.quake.report.ui.theme.buttonColor
import com.quake.report.ui.theme.magColor
import com.quake.report.ui.theme.magnitudeColor
import com.quake.report.ui.theme.orange
import com.quake.report.ui.theme.splashYellow
import com.quake.report.util.convertDate
import com.quake.report.util.convertDateHours
import com.quake.report.util.round
import com.quake.report.util.subStringBeforeComma
import com.quake.report.MainActivity.Companion.splashData
import com.quake.report.util.subStringDetail
import kotlinx.coroutines.launch
import org.osmdroid.util.GeoPoint

@Composable
fun BottomNavListPage(action: () -> Unit) {
    // TODO: phase 2 filtreleme asc desc siralama mag, search
    var url by remember { mutableStateOf("") }
    var coordinates by remember { mutableStateOf(GeoPoint(0.0, 0.0)) }
    val data = splashData.sortedWith(compareBy { it.magnitude }).reversed()
    val context = LocalContext.current
    var showDialog by remember { mutableStateOf(false) }
    var showAlertDialog by remember { mutableStateOf(false) }
    var isLiteViews by rememberSaveable { mutableStateOf(false) }
    val radius = with(LocalDensity.current) { 10.dp.toPx() }
    val cornerRadiusDp = 50.dp
    val cornerRadius = with(LocalDensity.current) { cornerRadiusDp.toPx() }

    val shape = remember {
        GenericShape { size, _ ->
            val width = size.width
            val height = size.height
            moveTo(0f, radius)
            quadraticBezierTo(0f, 0f, radius, 0f)
            lineTo(width - cornerRadius - radius, 0f)
            quadraticBezierTo(width - cornerRadius, 0f, width - cornerRadius, radius)

            quadraticBezierTo(
                width - 1.2f * cornerRadius,
                1.2f * cornerRadius,
                width - radius,
                cornerRadius
            )

            quadraticBezierTo(width, cornerRadius, width, cornerRadius + radius)

            lineTo(width, height - radius)
            quadraticBezierTo(width, height, width - radius, height)

            lineTo(radius, height)
            quadraticBezierTo(0f, height, 0f, height - radius)

            lineTo(0f, radius)
        }
    }



    CardList(items = arrayListOf<@Composable () -> Unit>().apply {
        for (item: UiResponse in data) {
            add {
                CustomCard(
                    title = item.place?.subStringBeforeComma(),
                    detail = item.title?.subStringDetail(),
                    time = item.time,
                    lastUpdated = item.lastUpdated,
                    mag = item.magnitude.toString(),
                    doubleMag = item.magnitude,
                    shape = shape,
                    url = item.detailUrl,
                    isLiteMode = isLiteViews,
                    onMapOpen = {
                        if (item.latitude != null && item.longitude != null) {
                            try {
                                coordinates = GeoPoint(item.latitude!!, item.longitude!!)
                                showDialog = true
                            } catch (e: Exception) {
                                Log.d("e:", "geopoint null.")
                            }
                        } else {
                            Toast.makeText(context, "Location is missing.", Toast.LENGTH_LONG)
                                .show()
                        }
                    },
                    onDetailOpen = { itemUrl ->
                        url = itemUrl
                        showAlertDialog = true
                    }
                )
            }
        }
    }) {
        isLiteViews = !isLiteViews
    }

    when {
        showDialog -> {
            CustomDialog(
                coordinates,
                onDismissRequest = { showDialog = false }
            )
        }

        showAlertDialog -> {
            CustomAlertDialog(
                onDismiss = { showAlertDialog = false },
                onApply = {
                    showAlertDialog = false
                    context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(url)))
                }
            )
        }
    }


}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun CardList(
    items: ArrayList<@Composable () -> Unit>,
    onSwitchChecked: () -> Unit
) {
    val mState = remember { LazyListState() }
    val coroutineScope = rememberCoroutineScope()
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(lightYellow),
        state = mState
    ) {
        item {
            Text(
                "Update data from home page to see the details here.", modifier = Modifier
                    .padding(start = 30.dp, end = 30.dp, top = 50.dp, bottom = 30.dp)
                    .fillMaxWidth()
                    .background(lightYellow),
                fontWeight = FontWeight.Bold
            )
        }
        stickyHeader {
            Row(
                horizontalArrangement = Arrangement.End,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(end = 20.dp)
                    .background(lightYellow)
            ) {
                Text(
                    "Lite Views", modifier = Modifier.padding(end = 15.dp, top = 15.dp),
                    fontWeight = FontWeight.Bold
                )
                CustomSwitch {
                    val firstVisibleItem = mState.firstVisibleItemIndex
                    onSwitchChecked.invoke()
                    coroutineScope.launch {
                        mState.animateScrollToItem(firstVisibleItem)
                    }
                }
            }
        }
        items(items) { chartItem ->
            chartItem()
        }
    }
}

@Composable
fun CustomSwitch(isChecked: () -> Unit) {
    var checked by rememberSaveable { mutableStateOf(false) }
    Switch(
        modifier = Modifier
            .semantics { contentDescription = "cd" },
        checked = checked,
        onCheckedChange = {
            checked = it
            isChecked.invoke()
        },
        thumbContent = {
            if (checked) {
                Icon(
                    imageVector = Icons.Filled.Check,
                    contentDescription = null,
                    modifier = Modifier.size(SwitchDefaults.IconSize),
                )
            }
        },
        colors = SwitchDefaults.colors(
            checkedThumbColor = splashYellow,
            checkedTrackColor = orange,
            uncheckedThumbColor = splashYellow,
            uncheckedTrackColor = orange,
            uncheckedBorderColor = orange,
            checkedBorderColor = orange
        ),
    )
}

@Composable
fun CustomCard(
    title: String?, detail: String?,
    time: Double?, lastUpdated: Double?,
    shape: Shape,
    mag: String,
    doubleMag: Double?,
    url: String?,
    isLiteMode: Boolean,
    onMapOpen: () -> Unit,
    onDetailOpen: (String) -> Unit
) {

    val context = LocalContext.current
    val intent = remember {
        Intent(Intent.ACTION_VIEW, Uri.parse(url))
    }
    val infiniteTransition = rememberInfiniteTransition(label = "")
    val scale by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 1.2f,
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = when {
                    doubleMag!! > 6 -> 200
                    doubleMag > 5 -> 300
                    doubleMag > 3 -> 700
                    doubleMag > 1 -> 1000
                    else -> 1000
                }
            ),
            repeatMode = RepeatMode.Reverse
        ), label = ""
    )

    Box(
        modifier = Modifier
            .background(lightYellow)
            .padding(20.dp)
            .fillMaxWidth()
    ) {
        Card(
            modifier = if (isLiteMode) {
                Modifier
                    .fillMaxWidth()
                    .height(50.dp)
                    .clip(shape)
            } else {
                Modifier
                    .fillMaxWidth()
                    .clip(shape)
            },
            colors = CardDefaults.cardColors(
                containerColor = green1
            )
        ) {
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.Start,
                modifier = Modifier.padding(10.dp)
            ) {
                Row(modifier = Modifier.padding(top = 4.dp)) {
                    Icon(
                        imageVector = Icons.Filled.LocationOn, contentDescription = "",
                        modifier = Modifier
                            .width(20.dp)
                            .height(20.dp),
                        tint = orange
                    )
                    Text(
                        text = title.toString(),
                        modifier = Modifier.padding(start = 5.dp, top = 3.dp),
                        fontSize = 15.sp,
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                    )
                }
                Text(
                    modifier = Modifier.padding(top = 10.dp),
                    text = detail.toString(),
                    fontSize = 12.sp,
                    color = Color.White,
                )
                Text(
                    modifier = Modifier.padding(top = 10.dp),
                    text = "Occured: " + convertDate(time?.let { Math.round(it) }
                        .toString()),
                    fontSize = 12.sp,
                    color = Color.White,
                )
                Text(
                    modifier = Modifier.padding(top = 10.dp),
                    text = "Last Updated " + convertDateHours(lastUpdated?.let {
                        Math.round(
                            it
                        )
                    }
                        .toString()),
                    fontSize = 12.sp,
                    color = Color.White,
                )
            }
            Row(
                verticalAlignment = Alignment.Bottom,
                horizontalArrangement = Arrangement.Center
            ) {
                Button(
                    onClick = {
                        onMapOpen.invoke()
                    },
                    modifier = Modifier
                        .weight(0.5f)
                        .padding(start = 7.dp, end = 7.dp, bottom = 5.dp)
                        .fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = buttonColor)
                ) {
                    Icon(
                        imageVector = Icons.Default.LocationOn,
                        contentDescription = "Icon",
                        tint = Color.White,
                        modifier = Modifier.padding(end = 5.dp)
                    )
                    Text(text = "Show on map", color = Color.White)
                }
                Button(
                    onClick = {
                        onDetailOpen.invoke(url.toString())
                    },
                    modifier = Modifier
                        .weight(0.5f)
                        .padding(end = 7.dp, bottom = 5.dp)
                        .fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = buttonColor)
                ) {
                    Icon(
                        imageVector = Icons.Default.Send,
                        contentDescription = "Icon",
                        tint = Color.White,
                        modifier = Modifier.padding(end = 5.dp)
                    )
                    Text(text = "Details", color = Color.White)
                }
            }

        }
        Text(
            text = mag.toDouble().round().toString(),
            color = magColor,
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .circleBackground(color = magnitudeColor, padding = 9.dp)
                .align(Alignment.TopEnd)
                .scale(scale)
        )

    }
}

fun Modifier.circleBackground(color: Color, padding: Dp): Modifier {
    val backgroundModifier = drawBehind {
        drawCircle(
            color,
            size.width / 2f,
            center = Offset(size.width / 2f, size.height / 2f)
        )
    }

    val layoutModifier = layout { measurable, constraints ->
        // Adjust the constraints by the padding amount
        val adjustedConstraints = constraints.offset(-padding.roundToPx())

        // Measure the composable with the adjusted constraints
        val placeable = measurable.measure(adjustedConstraints)

        // Get the current max dimension to assign width=height
        val currentHeight = placeable.height
        val currentWidth = placeable.width
        val newDiameter = maxOf(currentHeight, currentWidth) + padding.roundToPx() * 2

        // Assign the dimension and the center position
        layout(newDiameter, newDiameter) {
            // Place the composable at the calculated position
            placeable.placeRelative(
                (newDiameter - currentWidth) / 2,
                (newDiameter - currentHeight) / 2
            )
        }
    }

    return this then backgroundModifier then layoutModifier
}

@Composable
fun CustomAlertDialog(onDismiss: () -> Unit, onApply: () -> Unit) {
    AlertDialog(
        onDismissRequest = {
            onDismiss.invoke()
        },
        title = { Text(text = "Warning!") },
        text = { Text(text = "Please Click Ok to open the web browser.") },
        confirmButton = {
            Button(
                onClick = {
                    onApply.invoke()
                },
                colors = ButtonDefaults.buttonColors(containerColor = buttonColor)
            ) {
                Text(
                    text = "OK",
                    color = Color.White
                )
            }
        },
        dismissButton = {
            Button(
                onClick = {
                    onDismiss.invoke()
                },
                colors = ButtonDefaults.buttonColors(containerColor = buttonColor)
            ) {
                Text(
                    text = "Cancel",
                    color = Color.White
                )
            }
        }
    )
}