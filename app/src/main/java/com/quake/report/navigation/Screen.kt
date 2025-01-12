package com.quake.report.navigation

import com.quake.report.R

sealed class Screen(val route: String, var icon:Int ) {
    data object Splash: Screen(route = "splash_screen", R.drawable.pin)
    data object Home: Screen(route = "home_screen", R.drawable.pin)
    data object BottomNavHome: Screen(route = "bottom_home", R.drawable.home_black)
    data object BottomNavGraph: Screen(route = "bottom_graph", R.drawable.home_black)
    data object BottomNavList: Screen(route = "bottom_list",R.drawable.home_black)
}