package com.bangkit.dermascan.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.MailOutline
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import com.bangkit.dermascan.R
import com.bangkit.dermascan.ui.chat.ChatScreen
import com.bangkit.dermascan.ui.feeds.FeedsScreen
import com.bangkit.dermascan.ui.home.HomeScreen
import com.bangkit.dermascan.ui.profile.ProfileScreen
import com.bangkit.dermascan.ui.scan.ScanScreen
//import com.bangkit.dermascan.ui.settings.SettingsScreen
import com.bangkit.dermascan.ui.theme.DermaScanTheme
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.navigation.compose.rememberNavController
import com.bangkit.dermascan.ui.navigation.AppNavHost

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            DermaScanTheme {
                val navController = rememberNavController()
                AppNavHost(navController)
//                MainScreen()
            }
        }
    }
}

// Data class untuk item navigasi
data class BottomNavItem(
    val title: String,
    val icon: Any // Mendukung baik ImageVector maupun drawable resource ID
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen() {
    // Daftar item navigasi, beberapa menggunakan ikon drawable dan lainnya menggunakan ikon bawaan
    val items = listOf(
        BottomNavItem("Home", Icons.Filled.Home),       // Ikon dari drawable
        BottomNavItem("Feeds", R.drawable.ic_feed), // Ikon bawaan Jetpack Compose
        BottomNavItem("Scan", R.drawable.ic_camera), // Ikon bawaan Jetpack Compose
        BottomNavItem("Chat", R.drawable.ic_chat),  // Ikon dari drawable
        BottomNavItem("Profile", Icons.Default.Person)    // Ikon bawaan Jetpack Compose
    )

    var selectedItem by remember { mutableIntStateOf(0) }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = {
                    Text(text = items[selectedItem].title)
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.onPrimaryContainer,      // Warna latar belakang
                    titleContentColor = MaterialTheme.colorScheme.onPrimary, // Warna teks judul
                    navigationIconContentColor = MaterialTheme.colorScheme.onPrimary // Warna ikon navigasi
                )
            )
        },
        bottomBar = {
            NavigationBar(containerColor = MaterialTheme.colorScheme.onPrimaryContainer) {
                items.forEachIndexed { index, item ->
                    NavigationBarItem(
                        icon = {
                            when (item.icon) {
                                is ImageVector -> Icon(item.icon as ImageVector, contentDescription = item.title)
                                is Int -> Icon(painterResource(id = item.icon as Int), contentDescription = item.title)
                                else -> throw IllegalArgumentException("Icon type not supported")
                            }
                        },
                        label = { Text(item.title) },
                        colors = NavigationBarItemDefaults.colors( // Ganti dengan NavigationBarItemDefaults.colors

                            selectedIconColor = Color.Black, // Warna ikon saat dipilih
                            unselectedIconColor = Color.Gray, // Warna ikon saat tidak dipilih
                            selectedTextColor = Color.White, // Warna teks saat dipilih
                            unselectedTextColor = Color.Gray // Warna teks saat tidak dipilih
                        ),
                        selected = selectedItem == index,
                        onClick = { selectedItem = index }
                    )
                }
            }
        }
    ) { innerPadding ->
        // Konten sesuai dengan tab yang dipilih
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .padding(start = 15.dp, bottom = 15.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            when (selectedItem) {
                0 -> HomeScreen()
                1 -> FeedsScreen()
                2 -> ScanScreen()
                3 -> ChatScreen()
                4 -> ProfileScreen()
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MainScreenPreview() {
    DermaScanTheme {
        MainScreen()
    }


}




