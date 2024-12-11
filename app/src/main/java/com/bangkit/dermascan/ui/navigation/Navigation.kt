package com.bangkit.dermascan.ui.navigation

import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.slideOutVertically
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import com.bangkit.dermascan.ui.main.MainScreen
import com.bangkit.dermascan.ui.onBoard.OnBoardScreen
import com.bangkit.dermascan.ui.authentication.register.RegisterScreen
import androidx.navigation.compose.composable
import com.bangkit.dermascan.ui.articleFavorite.FavoriteArticleScreen
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
import com.bangkit.dermascan.ui.main.profile.settings.SettingsScreen
import com.bangkit.dermascan.ui.main.scan.ScanScreen
import com.bangkit.dermascan.ui.splashScreen.SplashScreen
import com.bangkit.dermascan.ui.main.scan.upload.SharedViewModel
import com.bangkit.dermascan.ui.main.scan.upload.UploadScreen

@Composable
fun AppNavHost(navController: NavHostController) {
    val sharedViewModel = SharedViewModel()
    val viewModel: AuthViewModel = hiltViewModel()
    val roles by viewModel.roles.observeAsState("PATIENT")

    NavHost(navController, startDestination = "splash") {
        composable(
            "splash",
            enterTransition = {
                // Zoom In Effect
                scaleIn(
                    initialScale = 0.0f, // Mulai dari 20% dari ukuran aslinya
                    animationSpec = tween(1000)
                ) + fadeIn(animationSpec = tween(1000)) // Menambahkan fade-in
            },
            exitTransition = {
                // Zoom Out Effect
                scaleOut(
                    targetScale = 2.2f, // Berakhir dengan ukuran 220% dari ukuran aslinya
                    animationSpec = tween(1000)
                ) + fadeOut(animationSpec = tween(1000)) // Menambahkan fade-out
            }
        ) {
            SplashScreen(navController)
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
            MainScreen(navController)
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
                fadeIn(animationSpec = tween(1000))
            },
            exitTransition = {
                fadeOut(animationSpec = tween(1000))
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
                fadeIn(animationSpec = tween(1000))
            },
            exitTransition = {
                fadeOut(animationSpec = tween(1000))
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
                slideInHorizontally(initialOffsetX = { 1000 }) + fadeIn(animationSpec = tween(500))
            },
            exitTransition = {
                slideOutHorizontally(targetOffsetX = { -1000 }) + fadeOut(animationSpec = tween(500))
            }
        ) {
            EditProfileScreen(navController)
        }

        composable("skinLesionHistory",
            enterTransition = {
                slideInVertically(initialOffsetY = { 1000 }) + fadeIn(animationSpec = tween(500))
            },
            exitTransition = {
                slideOutVertically(targetOffsetY = { -1000 }) + fadeOut(animationSpec = tween(500))
            }
        ) {
            SkinLesionHistoryScreen(navController)
        }

        composable("article",
            enterTransition = {
                fadeIn(animationSpec = tween(1000))
            },
            exitTransition = {
                fadeOut(animationSpec = tween(1000))
            }
        ) {
            FeedsScreen(navController, roles = roles)
        }

        composable("articleAdd",
            enterTransition = {
                fadeIn(animationSpec = tween(300))
            },
            exitTransition = {
                fadeOut(animationSpec = tween(300))
            }
        ) {
            ArticleAddScreen(navController)
        }

        composable("consultation",
            enterTransition = {
                slideInHorizontally(initialOffsetX = { 1000 }) + fadeIn(animationSpec = tween(500))
            },
            exitTransition = {
                slideOutHorizontally(targetOffsetX = { -1000 }) + fadeOut(animationSpec = tween(500))
            }
        ) {
            ConsultationsScreen(navController)
        }

        composable("block",
            enterTransition = {
                fadeIn(animationSpec = tween(1000))
            },
            exitTransition = {
                fadeOut(animationSpec = tween(1000))
            }
        ) {
            BlockedAccessScreen()
        }

        composable("favoriteArticle",
            enterTransition = {
                fadeIn(animationSpec = tween(1000))
            },
            exitTransition = {
                fadeOut(animationSpec = tween(1000))
            }
        ) {
            FavoriteArticleScreen(navController)
        }
        composable("settings",
            enterTransition = {
                fadeIn(animationSpec = tween(1000))
            },
            exitTransition = {
                fadeOut(animationSpec = tween(1000))
            }
        ) {
            SettingsScreen(navController)
        }
    }
}





