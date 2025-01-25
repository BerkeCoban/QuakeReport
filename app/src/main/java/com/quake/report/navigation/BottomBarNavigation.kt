package com.quake.report.navigation

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import com.quake.report.R
import com.quake.report.screens.BottomNavGraphPage
import com.quake.report.screens.BottomNavListPage
import com.quake.report.screens.MarkerPage


@Composable
fun BottomBarNavigation(navController: NavHostController) {

    Scaffold(
        bottomBar = {
            BottomNavigation(
                modifier = Modifier
                    .height(70.dp).clip(RoundedCornerShape(15.dp, 15.dp, 0.dp, 0.dp)),
                backgroundColor = Color.White,
                contentColor = Color.Black
            ) {
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentRoute = navBackStackEntry?.destination?.route


                BottomNavigationItem(
                    selected = currentRoute == Screen.BottomNavHome.route,
                    onClick = {
                        navController.navigate(route = Screen.BottomNavHome.route)

                    },
                    icon = {
                        if (currentRoute == Screen.BottomNavHome.route) {
                            Image(
                                painter = painterResource(id = R.drawable.home_black),
                                contentDescription = "Logo",
                            )
                        } else {
                            Image(
                                painter = painterResource(id = R.drawable.home_gray),
                                contentDescription = "Logo",
                            )
                        }

                    },
                    label = { Text(text = "Home") }
                )

                BottomNavigationItem(
                    selected = currentRoute == Screen.BottomNavGraph.route,
                    onClick = {
                        navController.navigate(route = Screen.BottomNavGraph.route)
                    },
                    icon = {
                        if (currentRoute == Screen.BottomNavGraph.route) {
                            Image(
                                painter = painterResource(id = R.drawable.graph_black),
                                contentDescription = "Logo",
                            )
                        } else {
                            Image(
                                painter = painterResource(id = R.drawable.graph_gray),
                                contentDescription = "Logo",
                            )
                        }
                    },
                    label = { Text(text = "Graph") }
                )

                BottomNavigationItem(
                    selected = currentRoute == Screen.BottomNavList.route,
                    onClick = {
                        navController.navigate(route = Screen.BottomNavList.route)
                    },
                    icon = {
                        if (currentRoute == Screen.BottomNavList.route) {
                            Image(
                                painter = painterResource(id = R.drawable.list_black),
                                contentDescription = "Logo",
                            )
                        } else {
                            Image(
                                painter = painterResource(id = R.drawable.list_gray),
                                contentDescription = "Logo",
                            )
                        }
                    },
                    label = { Text(text = "Detail") }
                )

            }
        }
    ) { innerPadding ->
        val modifier = Modifier.padding(innerPadding)
        NavHost(navController, startDestination = Screen.BottomNavHome.route, modifier = modifier) {
            composable(Screen.BottomNavHome.route) { MarkerPage(navController) }

            composable(route = Screen.BottomNavGraph.route) { BottomNavGraphPage {
                Log.d("test", "test")
            }
            }
            composable(route = Screen.BottomNavList.route) { BottomNavListPage {
                Log.d("", "")
            }
            }
        }
    }
}