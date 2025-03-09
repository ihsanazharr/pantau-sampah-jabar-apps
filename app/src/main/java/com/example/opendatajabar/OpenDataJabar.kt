package com.example.opendatajabar

import androidx.compose.animation.*
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.*
import androidx.navigation.navArgument
import com.example.opendatajabar.ui.navigation.NavigationItem
import com.example.opendatajabar.ui.navigation.Screen
import com.example.opendatajabar.ui.screen.dataEntry.InputScreen
import com.example.opendatajabar.ui.screen.dataEntry.PieChartScreen
import com.example.opendatajabar.ui.screen.dataList.DataListScreen
import com.example.opendatajabar.ui.screen.dataList.EditScreen
import com.example.opendatajabar.ui.screen.home.HomeScreen
import com.example.opendatajabar.ui.screen.onboarding.OnboardingScreen
import com.example.opendatajabar.ui.screen.profile.ProfileScreen
import com.example.opendatajabar.ui.screen.profile.EditProfileScreen
import com.example.opendatajabar.viewmodel.DataViewModel
import com.example.opendatajabar.viewmodel.ProfileViewModel

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun DataOpenJabarApp(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
    viewModel: DataViewModel = hiltViewModel(),
    profileViewModel: ProfileViewModel = viewModel()
) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route ?: Screen.Home.route
    val shouldExit = remember { mutableStateOf(false) }

    Scaffold(
        bottomBar = {
            if (currentRoute != Screen.DetailReward.route) {
                BottomBar(navController)
            }
        },
        modifier = modifier
    ) { innerPadding ->
        AnimatedContent(
            targetState = currentRoute,
            transitionSpec = {
                slideInHorizontally(
                    initialOffsetX = { fullWidth -> fullWidth },
                    animationSpec = tween(durationMillis = 100, easing = FastOutSlowInEasing)
                ) with slideOutHorizontally(
                    targetOffsetX = { fullWidth -> -fullWidth },
                    animationSpec = tween(durationMillis = 100, easing = FastOutSlowInEasing)
                )
            },
            label = "ScreenTransition"
        ) { targetScreen ->
            NavHost(
                navController = navController,
                startDestination = Screen.Home.route,
                modifier = Modifier.padding(innerPadding)
            ) {
                composable(Screen.Onboarding.route) {
                    if (targetScreen == Screen.Onboarding.route) {
                        OnboardingScreen(navController = navController)
                    }
                }
                composable(Screen.Home.route) {
                    if (targetScreen == Screen.Home.route) {
                        HomeScreen(navController = navController, viewModel = viewModel)
                    }
                }
                composable(Screen.DataInput.route) {
                    if (targetScreen == Screen.DataInput.route) {
                        InputScreen(
                            onClose = { navController.popBackStack() }
                        )
                    }
                }
                composable(Screen.Edit.route,
                    arguments = listOf(navArgument("id") { type = NavType.IntType })
                ) { backStackEntry ->
                    val id = backStackEntry.arguments?.getInt("id") ?: 0
                    if (targetScreen == Screen.Edit.route) {
                        EditScreen(navController = navController, viewModel = viewModel, dataId = id)
                    }
                }
                composable(Screen.DataList.route) {
                    if (targetScreen == Screen.DataList.route) {
                        DataListScreen(navController = navController, viewModel = viewModel)
                    }
                }
                composable(Screen.PieChart.route) {
                    if (targetScreen == Screen.PieChart.route) {
                        val data = viewModel.dataList.value
                        if (data != null) {
                            PieChartScreen(dataList = data)
                        } else {
                            // Tampilkan pesan atau indikator loading
                            Text("Loading data...")
                        }
                    }
                }
                composable(Screen.Profile.route) {
                    if (targetScreen == Screen.Profile.route) {
                        ProfileScreen(
                            navController = navController,
                            viewModel = profileViewModel,
                            onQuit = { shouldExit.value = true } // Set state keluar
                        )
                    }
                }

                if (shouldExit.value) {
                    System.exit(0)
                }
                composable("edit_profile") {
                    if (targetScreen == "edit_profile") {
                        EditProfileScreen(navController = navController, viewModel = profileViewModel)
                    }
                }
            }
        }
    }
}

@Composable
private fun BottomBar(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    NavigationBar(
        modifier = modifier,
        containerColor = MaterialTheme.colorScheme.onTertiary,
        contentColor = MaterialTheme.colorScheme.primary
    ) {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.destination?.route
        val navigationItems = listOf(
            NavigationItem("Home", Icons.Default.Home, Screen.Home),
            NavigationItem("Grafik", Icons.Default.Info, Screen.PieChart),
//            NavigationItem("List", Icons.Default.List, Screen.DataList),
            NavigationItem("Profile", Icons.Default.AccountCircle, Screen.Profile),
        )

        navigationItems.forEach { item ->
            val isSelected = currentRoute == item.screen.route

            NavigationBarItem(
                icon = { Icon(item.icon, contentDescription = item.title) },
                label = { if (isSelected) Text(item.title) },
                selected = isSelected,
                onClick = {
                    navController.navigate(item.screen.route) {
                        popUpTo(navController.graph.findStartDestination().id) { saveState = true }
                        restoreState = true
                        launchSingleTop = true
                    }
                },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = MaterialTheme.colorScheme.onTertiary,
                    indicatorColor = MaterialTheme.colorScheme.primary,
                    unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant
                )
            )
        }
    }
}