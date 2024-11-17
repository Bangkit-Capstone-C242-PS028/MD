package com.bangkit.dermascan.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import com.bangkit.dermascan.ui.MainScreen
import com.bangkit.dermascan.ui.onBoard.OnBoardScreen
import com.bangkit.dermascan.ui.register.RegisterScreen
import androidx.navigation.compose.composable
import com.bangkit.dermascan.ui.home.ButtonWithCustomColor
import com.bangkit.dermascan.ui.home.HomeScreen
import com.bangkit.dermascan.ui.login.LoginScreen
import com.bangkit.dermascan.ui.splashScreen.SplashScreen

@Composable
fun AppNavHost(navController: NavHostController) {
    NavHost(navController, startDestination = "splash") {
        composable("splash") { SplashScreen(navController) }
        composable("onBoard") {
            OnBoardScreen(
                navController = navController,
                onLoginSuccess = { navController.navigate("main") }
            )
        }
        composable("login") {
            LoginScreen(context = navController.context,onLoginSuccess = { navController.navigate("main"){
                popUpTo("login"){
                    inclusive = true
                }
                popUpTo("onBoard"){
                    inclusive = true
                }
                launchSingleTop = true
            } })
        }
        composable("register") {
            RegisterScreen(onRegisterSuccess = { navController.navigate("login"){
                popUpTo("register"){
                    inclusive = true
                }
            } })
        }
        composable("main") {
            MainScreen(navController)

        }

//        composable("home") {
//            HomeScreen(navController = navController, onLogOut = {
//                navController.navigate("onBoard") {
//                    popUpTo("home") { inclusive = true }
//                }
//            })
//        }
//        co
////        composable("onboard_screen") {
////            OnBoardScreen(navController)
//        }
    }
}

