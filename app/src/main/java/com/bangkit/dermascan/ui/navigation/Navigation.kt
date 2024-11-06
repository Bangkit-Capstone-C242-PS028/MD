package com.bangkit.dermascan.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import com.bangkit.dermascan.ui.MainScreen
import com.bangkit.dermascan.ui.login.LoginScreen
import com.bangkit.dermascan.ui.register.RegisterScreen
import androidx.navigation.compose.composable

@Composable
fun AppNavHost(navController: NavHostController) {
    NavHost(navController, startDestination = "login") {
        composable("login") {
            LoginScreen(
                navController = navController,
                onLoginSuccess = { navController.navigate("main")
                }
            )
        }
        composable("register") {
            RegisterScreen(onRegisterSuccess = { navController.navigate("login"){
                popUpTo("register"){
                    inclusive = true
                }
            } })
        }
        composable("main") {
            MainScreen()

        }
    }
}

