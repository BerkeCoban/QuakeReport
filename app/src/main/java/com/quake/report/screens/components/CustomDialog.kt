package com.quake.report.screens.components

import android.graphics.drawable.Drawable
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.quake.report.R
import com.quake.report.osm.MarkerLabeled
import com.quake.report.osm.OpenStreetMap
import com.quake.report.osm.rememberCameraState
import com.quake.report.osm.rememberMarkerState
import com.quake.report.osm.rememberOverlayManagerState
import org.osmdroid.util.GeoPoint

@Composable
fun CustomDialog(
    coordinates: GeoPoint,
    onDismissRequest: () -> Unit
) {

    val context = LocalContext.current
    val cameraState = rememberCameraState {
        geoPoint = coordinates
        zoom = 0.0
    }
    val overlayManagerState = rememberOverlayManagerState()
    val pinIcon: Drawable? by remember {
        mutableStateOf(context.getDrawable(R.drawable.pin))
    }


    Dialog(
        onDismissRequest = {
            onDismissRequest()
        }
    ) {
        Card(
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier
                .fillMaxWidth(1f)
                .fillMaxHeight(0.6f),
        ) {
            Surface(
                modifier = Modifier.fillMaxSize()
            ) {
                OpenStreetMap(
                    cameraState = cameraState,
                    overlayManagerState = overlayManagerState,
                    onFirstLoadListener = {}
                ) {
                    MarkerLabeled(
                        state = rememberMarkerState(
                            geoPoint = coordinates,
                            rotation = 0f
                        ),
                        icon = pinIcon,
                        //title = markerData.title,
                        //snippet = markerData.place,
                        // label = markerData.magnitude.toString(),
                        // labelProperties = labelProperties.value
                    )
                }
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(end = 5.dp),
                    verticalArrangement = Arrangement.Top,
                    horizontalAlignment = Alignment.End,
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.ic_close),
                        contentDescription = "Logo",
                        modifier = Modifier.clickable {
                            onDismissRequest()
                        }
                    )
                }
            }
        }
    }
}