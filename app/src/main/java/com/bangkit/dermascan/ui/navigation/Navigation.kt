package com.bangkit.dermascan.ui.navigation

import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.slideOutVertically
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import com.bangkit.dermascan.ui.main.MainScreen
import com.bangkit.dermascan.ui.onBoard.OnBoardScreen
import com.bangkit.dermascan.ui.authentication.register.RegisterScreen
import androidx.navigation.compose.composable
import com.bangkit.dermascan.ui.authentication.AuthViewModel
import com.bangkit.dermascan.ui.main.profile.editProfile.EditProfileScreen
//import com.bangkit.dermascan.ui.home.ButtonWithCustomColor
//import com.bangkit.dermascan.ui.home.HomeScreen
import com.bangkit.dermascan.ui.authentication.login.LoginScreen
import com.bangkit.dermascan.ui.main.BlockedAccessScreen
import com.bangkit.dermascan.ui.main.feeds.ArticleAddScreen
import com.bangkit.dermascan.ui.main.feeds.FeedsScreen
import com.bangkit.dermascan.ui.main.profile.result.ConsultationsScreen
//import com.bangkit.dermascan.ui.main.feeds.ArticleAddScreen
//import com.bangkit.dermascan.ui.main.feeds.FeedsScreen
import com.bangkit.dermascan.ui.main.profile.result.SkinLesionHistoryScreen
import com.bangkit.dermascan.ui.main.scan.ScanScreen
import com.bangkit.dermascan.ui.splashScreen.SplashScreen
import com.bangkit.dermascan.ui.main.scan.upload.SharedViewModel
import com.bangkit.dermascan.ui.main.scan.upload.UploadScreen

@Composable
fun AppNavHost(navController: NavHostController) {
    val sharedViewModel = SharedViewModel()
    val viewModel : AuthViewModel = hiltViewModel()
    val roles by viewModel.roles.observeAsState("PATIENT")
    NavHost(navController, startDestination = "splash") {
        composable(
            "splash",
            enterTransition = {
                slideInHorizontally(initialOffsetX = { 1000 }) + fadeIn(animationSpec = tween(500))
            },
            exitTransition = {
                slideOutHorizontally(targetOffsetX = { -1000 }) + fadeOut(animationSpec = tween(500))
            }
        ) {
            SplashScreen(navController)
        }

        composable(
            "onBoard",
            enterTransition = {
                slideInHorizontally(initialOffsetX = { 1000 }) + fadeIn(animationSpec = tween(500))
            },
            exitTransition = {
                slideOutHorizontally(targetOffsetX = { -1000 }) + fadeOut(animationSpec = tween(500))
            }
        ) {
            OnBoardScreen(
                navController = navController,
                onLoginSuccess = { navController.navigate("main") }
            )
        }

        composable(
            "login",
            enterTransition = {
                fadeIn(animationSpec = tween(300))
            },
            exitTransition = {
                fadeOut(animationSpec = tween(300))
            }
        ) {
            LoginScreen(context = navController.context, onLoginSuccess = {
                navController.navigate("main") {
                    popUpTo("login") { inclusive = true }
                    popUpTo("onBoard") { inclusive = true }
                    launchSingleTop = true
                }
            })
        }

        composable(
            "register",
            enterTransition = {
                slideInHorizontally(initialOffsetX = { 1000 }) + fadeIn(animationSpec = tween(500))
            },
            exitTransition = {
                slideOutHorizontally(targetOffsetX = { -1000 }) + fadeOut(animationSpec = tween(500))
            }
        ) {
            RegisterScreen(onRegisterSuccess = {
                navController.navigate("login") {
                    popUpTo("register") { inclusive = true }
                }
            })
        }

        composable(
            "main",
            enterTransition = {
                slideInVertically(initialOffsetY = { 1000 }) + fadeIn(animationSpec = tween(500))
            },
            exitTransition = {
                slideOutVertically(targetOffsetY = { -1000 }) + fadeOut(animationSpec = tween(500))
            }
        ) {
            MainScreen( navController)
        }

        composable(
            "scan_full",
            enterTransition = {
                fadeIn(animationSpec = tween(400))
            },
            exitTransition = {
                fadeOut(animationSpec = tween(400))
            }
        ) {
            ScanScreen(
                viewModel = sharedViewModel,
                onBackClick = { navController.navigateUp() },
                onUploadClick = {
                    navController.navigate("upload") {
                        popUpTo("scan_full") { inclusive = false }
                    }
                }
            )
        }

        composable(
            "upload",
            enterTransition = {
                fadeIn(animationSpec = tween(300))
            },
            exitTransition = {
                fadeOut(animationSpec = tween(300))
            }
        ) {
            UploadScreen(
                navController = navController,
                viewModel = sharedViewModel,
                onSuccessClick = {
                    navController.navigate("main") {
                        popUpTo("upload") { inclusive = true }
                        popUpTo("scan_full") { inclusive = true }
                    }
                }
            )
        }

        composable("editProfile",
            enterTransition = {
                fadeIn(animationSpec = tween(300))
            },
            exitTransition = {
                fadeOut(animationSpec = tween(300))
            }
        ) {
            EditProfileScreen(navController)
        }

        composable("skinLesionHistory") {
            SkinLesionHistoryScreen(navController)
        }

        composable("article") {
            FeedsScreen(navController,roles = roles)
        }

        composable("articleAdd") {
            ArticleAddScreen(navController)
        }

        composable("consultation") {
            ConsultationsScreen()
        }

        composable("block") {
            BlockedAccessScreen()
        }
//
//        composable("articleAdd") {
//            ArticleAddScreen(navController)
//        }

    }
}




