
package com.bangkit.dermascan.ui.main

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import com.bangkit.dermascan.R
import com.bangkit.dermascan.ui.main.chat.ChatScreen
import com.bangkit.dermascan.ui.main.feeds.FeedsScreen
import com.bangkit.dermascan.ui.main.home.HomeScreen
import com.bangkit.dermascan.ui.main.profile.ProfileScreen
import com.bangkit.dermascan.ui.main.scan.ScanScreen
//import com.bangkit.dermascan.ui.settings.SettingsScreen
import com.bangkit.dermascan.ui.theme.DermaScanTheme
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.platform.LocalContext

import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.bangkit.dermascan.ui.article.ArticleActivity
import com.bangkit.dermascan.ui.authentication.AuthViewModel

import com.bangkit.dermascan.ui.navigation.AppNavHost
//import com.bangkit.dermascan.ui.scan.PhotoActivity
import com.bangkit.dermascan.ui.theme.*
import com.bangkit.dermascan.ui.main.scan.upload.SharedViewModel
import dagger.hilt.android.AndroidEntryPoint

//import androidx.compose.ui.Alignment
@AndroidEntryPoint
class MainActivity : ComponentActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setTheme(R.style.Theme_DermaScan)
        enableEdgeToEdge()
        setContent {
            DermaScanTheme {
                val navController = rememberNavController()
                AppNavHost(navController = navController)

            }
        }
    }
}

// DataArticles class untuk item navigasi
data class BottomNavItem(
    val title: String,
    val icon: Any // Mendukung baik ImageVector maupun drawable resource ID
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(context: Context, navController: NavHostController) {
    val authViewModel: AuthViewModel = hiltViewModel()
    val roles by authViewModel.roles.observeAsState("PATIENT")

    Toast.makeText(context, "Welcome Back $roles", Toast.LENGTH_SHORT).show()


    val items = mutableListOf(

        BottomNavItem("Feeds", R.drawable.ic_feeds),
        BottomNavItem("Chat Bot", R.drawable.ic_chatt),
        BottomNavItem("Profile", R.drawable.ic_account)
    )

    if (roles != "DOCTOR") {
        items.add(0, BottomNavItem("Home", R.drawable.ic_home))
        items.add(2, BottomNavItem("Scan", R.drawable.ic_camera))
    }

    var selectedItem by rememberSaveable { mutableIntStateOf(0) }
//    val navController = rememberNavController()
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = {
                    Text(text = items[selectedItem].title)
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Blue,
                    titleContentColor = LightBlue,
                    navigationIconContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        },
        bottomBar = {

                // Tampilkan cekungan jika roles bukan "DOCTOR"
                Box(
                    Modifier
                        .fillMaxWidth()
                ) {
                    if (roles != "DOCTOR") {
                        Canvas(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(100.dp) // Tinggi disesuaikan agar cukup untuk lengkungan
                        ) {
                            val width = size.width
                            val height = size.height
                            val cornerRadius = 24.dp.toPx() // Radius untuk lengkungan di kiri dan kanan atas
                            val concaveDepth = 78.dp.toPx() // Kedalaman cekungan

                            drawPath(
                                path = Path().apply {
                                    moveTo(0f, height) // Mulai dari kiri bawah
                                    lineTo(0f, cornerRadius) // Garis vertikal ke atas
                                    quadraticTo(0f, 0f, cornerRadius, 0f) // Lengkungan di kiri atas
                                    lineTo(width * 0.4f, 0f) // Garis ke awal cekungan tengah
                                    quadraticTo(
                                        width * 0.5f, concaveDepth, // Titik kontrol di bawah (kedalaman cekungan)
                                        width * 0.6f, 0f // Akhir cekungan di kanan
                                    )
                                    lineTo(width - cornerRadius, 0f) // Garis horizontal ke kanan
                                    quadraticTo(width, 0f, width, cornerRadius) // Lengkungan di kanan atas
                                    lineTo(width, height) // Garis vertikal ke bawah
                                    close()
                                },
                                color = Blue // Warna NavigationBar
                            )
                        }

                        NavigationBar(
                            containerColor = Color.Transparent, // Set warna transparan karena sudah menggunakan Canvas
                            modifier = Modifier.align(Alignment.BottomCenter)
                        ) {
                            items.forEachIndexed { index, item ->
                                if (index == 2) { // Index untuk FAB (Scan)
                                    Spacer(modifier = Modifier.weight(0.5f))
                                } else {
                                    NavigationBarItem(
                                        icon = {
                                            val offsetY = if (selectedItem == index) (-12).dp else 0.dp // Naikkan ikon jika dipilih
                                            when (item.icon) {
                                                is ImageVector -> Icon(
                                                    item.icon,
                                                    contentDescription = item.title,
                                                    modifier = Modifier.offset(y = offsetY) // Menambahkan offset ke ikon
                                                )
                                                is Int -> Icon(
                                                    painterResource(id = item.icon),
                                                    contentDescription = item.title,
                                                    modifier = Modifier.offset(y = offsetY) // Menambahkan offset ke ikon
                                                )
                                                else -> throw IllegalArgumentException("Icon type not supported")
                                            }
                                        },
                                        label = {
                                            Text(item.title, modifier = Modifier.offset(y = (0).dp))
                                        },
                                        colors = NavigationBarItemDefaults.colors(
                                            selectedIconColor = LightBlue,
                                            unselectedIconColor = LightBlue,
                                            selectedTextColor = White,
                                            unselectedTextColor = LightBlue,
                                            indicatorColor = Blue
                                        ),
                                        selected = selectedItem == index,
                                        onClick = { selectedItem = index },
                                        interactionSource = MutableInteractionSource()
                                    )
                                }
                            }
                        }

                    } else {
                        // Jika roles adalah "DOCTOR", tidak tampilkan cekungan
                        NavigationBar(
                            containerColor = Blue,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            items.forEachIndexed { index, item ->
                                NavigationBarItem(
                                    icon = {
                                        val offsetY = if (selectedItem == index) (-12).dp else 0.dp // Naikkan ikon jika dipilih
                                        when (item.icon) {
                                            is ImageVector -> Icon(
                                                item.icon,
                                                contentDescription = item.title,
                                                modifier = Modifier.offset(y = offsetY)
                                            )
                                            is Int -> Icon(
                                                painterResource(id = item.icon),
                                                contentDescription = item.title,
                                                modifier = Modifier.offset(y = offsetY)
                                            )
                                            else -> throw IllegalArgumentException("Icon type not supported")
                                        }
                                    },
                                    label = {
                                        Text(item.title, modifier = Modifier.offset(y = (0).dp))
                                    },
                                    colors = NavigationBarItemDefaults.colors(
                                        selectedIconColor = LightBlue,
                                        unselectedIconColor = LightBlue,
                                        selectedTextColor = White,
                                        unselectedTextColor = LightBlue,
                                        indicatorColor = Blue
                                    ),
                                    selected = selectedItem == index,
                                    onClick = { selectedItem = index },
                                    interactionSource = MutableInteractionSource()
                                )
                            }
                        }
                    }
                    // FloatingActionButton tetap akan muncul jika roles bukan "DOCTOR"
                    if (roles != "DOCTOR") {
                        FloatingActionButton(
                            onClick = {
                                navController.navigate("scan_full")
                            },
                            modifier = Modifier
                        .align(Alignment.TopCenter)
                                .offset(y = (-34).dp)
                                .padding(10.dp),
                            containerColor = Blue,
                            shape = CircleShape,
                            elevation = FloatingActionButtonDefaults.elevation(0.dp)
                        ) {
                            Icon(
                                painterResource(id = R.drawable.ic_camera),
                                contentDescription = "Scan",
                                Modifier.size(40.dp),
                                tint = LightBlue
                            )
                        }
                    }
                }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            if (roles != "DOCTOR") {
                when (selectedItem) {
                    0 -> HomeScreen(navController)
//                    1 -> {
//                        // Navigasi ke ArticleActivity menggunakan Context
//                        val context = LocalContext.current
//                        LaunchedEffect(Unit) {
//                            val intent = Intent(context, ArticleActivity::class.java)
//                            context.startActivity(intent)
//                        }
//                    }
                    1 ->    FeedsScreen()
                    2 -> ScanScreen(
                        viewModel = SharedViewModel(),
                        onBackClick = { navController.navigateUp() },
                        onUploadClick = { }
                    )
                    3 -> ChatScreen()
                    4 -> ProfileScreen(navController)
                }
            }else{
                when (selectedItem) {
//                    0 -> HomeScreen(navController)
//                    0 -> FeedsScreen()
                    0 -> {
                        // Navigasi ke ArticleActivity menggunakan Context
                        val context = LocalContext.current
                        LaunchedEffect(Unit) {
                            val intent = Intent(context, ArticleActivity::class.java)
                            context.startActivity(intent)
                        }
                    }
                    1 -> ChatScreen()
                    2 -> ProfileScreen(navController)
                }
            }

        }
    }

}

@Preview(showBackground = true)
@Composable
fun MainScreenPreview() {
    DermaScanTheme {
//        MainScreen(navController = rememberNavController())
    }

}




