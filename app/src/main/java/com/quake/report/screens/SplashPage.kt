package com.quake.report.screens

import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.quake.report.MainViewModel
import com.quake.report.R
import com.quake.report.ui.theme.buttonColor
import com.quake.report.ui.theme.splashYellow

@Composable
fun SplashPage(onCloseApp: () -> Unit) {
    val viewModel = viewModel<MainViewModel>()
    val showDialog = viewModel.emptyData.collectAsState()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(splashYellow),
        contentAlignment = Alignment.Center
    ) {
        Pulsating {
            Image(
                painter = painterResource(id = R.drawable.earthquake_icon_2),
                contentDescription = "Logo",
            )
        }
        if (showDialog.value) {
            AlertDialog(
                onDismissRequest = {
                },
                title = { Text(text = "Error!") },
                text = { Text(text = "Something went wrong, please try again later.") },
                confirmButton = {
                    Button(
                        onClick = {
                            viewModel.updateEmptyData(false)
                            onCloseApp.invoke()
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = buttonColor)
                    ) {
                        Text(
                            text = "OK",
                            color = Color.White
                        )
                    }
                }
            )
        }
    }
}

@Composable
fun Pulsating(pulseFraction: Float = 1.2f, content: @Composable () -> Unit) {
    val infiniteTransition = rememberInfiniteTransition(label = "")

    val scale by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = pulseFraction,
        animationSpec = infiniteRepeatable(
            animation = tween(1000),
            repeatMode = RepeatMode.Reverse
        ), label = ""
    )

    Box(modifier = Modifier.scale(scale)) {
        content()
    }
}