package com.cuentasclaras.app.presentation.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.cuentasclaras.app.presentation.detail.GroupDetailScreen
import com.cuentasclaras.app.presentation.groups.GroupsScreen
import com.cuentasclaras.app.presentation.login.LoginScreen

@Composable
fun AppNavGraph(
    mainViewModel: MainViewModel = hiltViewModel()
) {
    val authState by mainViewModel.authState.collectAsStateWithLifecycle()

    when (authState) {
        AuthNavigationState.Loading -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }
        AuthNavigationState.Unauthenticated -> {
            val navController = rememberNavController()
            NavHost(
                navController = navController,
                startDestination = Screen.Login.route
            ) {
                composable(Screen.Login.route) {
                    LoginScreen()
                }
            }
        }
        AuthNavigationState.Authenticated -> {
            val navController = rememberNavController()
            NavHost(
                navController = navController,
                startDestination = Screen.Groups.route
            ) {
                composable(Screen.Groups.route) {
                    GroupsScreen(
                        onGroupClick = { groupId, groupName ->
                            navController.navigate(
                                Screen.GroupDetail.createRoute(groupId, groupName)
                            )
                        }
                    )
                }
                composable(
                    route = Screen.GroupDetail.route,
                    arguments = listOf(
                        navArgument("groupId") { type = NavType.StringType },
                        navArgument("groupName") { type = NavType.StringType }
                    )
                ) {
                    GroupDetailScreen(
                        onNavigateBack = { navController.popBackStack() }
                    )
                }
            }
        }
    }
}