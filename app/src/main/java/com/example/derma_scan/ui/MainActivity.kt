package com.example.derma_scan.ui

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
import com.example.derma_scan.ui.theme.DermaScanTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.ui.graphics.vector.ImageVector
import com.example.derma_scan.ui.home.HomeScreen
import com.example.derma_scan.ui.profile.ProfileScreen
import com.example.derma_scan.ui.settings.SettingsScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            DermaScanTheme {
                MainScreen()
            }
        }
    }
}

// Data class untuk item navigasi
data class BottomNavItem(
    val title: String,
    val icon: ImageVector
)

@Composable
fun MainScreen() {
    // Daftar item navigasi
    val items = listOf(
        BottomNavItem("Home", Icons.Default.Home),
        BottomNavItem("Profile", Icons.Default.Person),
        BottomNavItem("Settings", Icons.Default.Settings)
    )

    var selectedItem by remember { mutableIntStateOf(0) }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = {
            NavigationBar {
                items.forEachIndexed { index, item ->
                    NavigationBarItem(
                        icon = { Icon(item.icon, contentDescription = item.title) },
                        label = { Text(item.title) },
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
                1 -> ProfileScreen()
                2 -> SettingsScreen()
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