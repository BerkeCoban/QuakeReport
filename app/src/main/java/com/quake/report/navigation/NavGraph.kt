package com.quake.report.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.quake.report.screens.SplashPage

@Composable
fun SetupNavGraph(
    navController: NavHostController,
    navControllerBottom: NavHostController,
    finish: () -> Unit
) {
    NavHost(
        navController = navController,
        startDestination = Screen.Splash.route
    ) {
        composable(
            route = Screen.Splash.route
        ) {
            SplashPage {
                finish.invoke()
            }
        }
        composable(
            route = Screen.Home.route,
        ) {
            BottomBarNavigation(navController = navControllerBottom)
        }
    }
}