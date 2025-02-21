package com.quake.report.screens

import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.GenericShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.offset
import androidx.compose.ui.unit.sp
import com.quake.report.MainActivity
import com.quake.report.data.model.UiResponse
import com.quake.report.screens.components.CustomDialog
import com.quake.report.ui.theme.green1
import com.quake.report.ui.theme.lightYellow
import com.quake.report.ui.theme.buttonColor
import com.quake.report.ui.theme.magColor
import com.quake.report.ui.theme.magnitudeColor
import com.quake.report.util.convertDate
import com.quake.report.util.convertDateHours
import com.quake.report.util.round
import org.osmdroid.util.GeoPoint

@Composable
fun BottomNavListPage(action: () -> Unit) {

    // empty view
    // yukari tarih kutucugu vs yada direk home daki daha gosterilsin filtrelenebilir
    // listede yuksek siddet yukarda olabilir
    // kartin boyutu sabit olabilir cunku yuvalak oturmaz vs. gerek varmi kontrol et
    // card frame
    // arada sigmayan title oluyor lite viewda texti tek satir yap yada text size kucult
    // lite view ortala titlei
    // bir uyari yazilabilir mapteki data burada gozukuyor gibi
    // switch yukarda sabit kalabilir assagi indikce dursun yukarda
    // show on map ve details icon

    val data = MainActivity.splashData
    val context = LocalContext.current
    var showDialog by remember { mutableStateOf(false) }
    var isLiteViews by remember { mutableStateOf(false) }
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
        add {
            Row {
                Button( modifier = Modifier
                    .height(100.dp)
                    .width(100.dp),
                    onClick = {
                        isLiteViews = !isLiteViews
                    }) {
                    Text(text = "switch")
                }
            }
        }
        for (item: UiResponse in data) {
            add {
                val infiniteTransition = rememberInfiniteTransition(label = "")
                val scale by infiniteTransition.animateFloat(
                    initialValue = 1f,
                    targetValue = 1.2f,
                    animationSpec = infiniteRepeatable(
                        animation = tween(
                            durationMillis = when {
                                item.magnitude!! > 6 -> 200
                                item.magnitude!! > 5 -> 300
                                item.magnitude!! > 4 -> 500
                                item.magnitude!! > 3 -> 700
                                item.magnitude!! > 1 -> 1000
                                else -> 1000
                            }
                        ),
                        repeatMode = RepeatMode.Reverse
                    ), label = ""
                )

                CustomCard(
                    title = item.place,
                    detail = item.title,
                    time = item.time,
                    lastUpdated = item.lastUpdated,
                    mag = item.magnitude.toString(),
                    doubleMag = item.magnitude,
                    shape = shape,
                    url = item.detailUrl,
                    isLiteMode = isLiteViews
                ) {
                    if (item.latitude != null && item.longitude != null) {
                        MainActivity.dialogGeoPoint =
                            GeoPoint(item.latitude!!, item.longitude!!)
                        showDialog = true
                    } else {
                        Toast.makeText(context, "Location is missing.", Toast.LENGTH_LONG)
                            .show()
                    }
                }
            }
        }
    })

    when {
        showDialog -> {
            CustomDialog(
                onDismissRequest = { showDialog = false }
            )
        }
    }


}

@Composable
fun CardList(
    items: ArrayList<@Composable () -> Unit>
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
fun CustomCard(
    title: String?, detail: String?,
    time: Double?, lastUpdated: Double?,
    shape: Shape,
    mag: String,
    doubleMag: Double?,
    url: String?,
    isLiteMode: Boolean,
    onDialogOpen: () -> Unit,
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
                Text(
                    text = title.toString(),
                    fontSize = 15.sp,
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                )
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
                        onDialogOpen.invoke()
                    },
                    modifier = Modifier
                        .weight(0.5f)
                        .padding(start = 7.dp, end = 7.dp, bottom = 5.dp)
                        .fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = buttonColor)
                ) {
                    Icon(
                        imageVector = Icons.Default.Favorite,
                        contentDescription = "Icon",
                        tint = Color.White,
                        modifier = Modifier.padding(end = 5.dp)
                    )
                    Text(text = "Show on map", color = Color.White)
                }
                Button(
                    onClick = {
                        context.startActivity(intent)
                    },
                    modifier = Modifier
                        .weight(0.5f)
                        .padding(end = 7.dp, bottom = 5.dp)
                        .fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = buttonColor)
                ) {
                    Icon(
                        imageVector = Icons.Default.Favorite,
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