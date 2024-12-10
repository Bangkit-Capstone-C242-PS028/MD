package com.bangkit.dermascan.ui.main.profile.settings

import android.annotation.SuppressLint

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.bangkit.dermascan.ui.main.feeds.AddTopBar
import com.bangkit.dermascan.ui.theme.*

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun SettingsScreen(navController: NavController, viewModel: SettingsViewModel = hiltViewModel() ){
    val isDarkTheme = viewModel.isDarkTheme.collectAsState(initial = false)
    Scaffold(
        topBar = {
            AddTopBar("Settings", navController = navController)
        },
        content = {innerPadding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(16.dp),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.Start
            ) {
                Text(text = "Appearance", style = MaterialTheme.typography.titleMedium)
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = if (isDarkTheme.value) "Dark Mode" else "Light Mode",
                        style = Typography.titleLarge,
                        modifier = Modifier.weight(1f)
                    )
                    Switch(
                        checked = isDarkTheme.value,
                        onCheckedChange = {
                            viewModel.toggleTheme() // Toggle theme on switch change
                        },
                        colors = SwitchDefaults.colors(
                            checkedThumbColor =  Blue,
                            uncheckedThumbColor = LightBlue,
                            checkedTrackColor = LightBlue,
                            uncheckedTrackColor = Color.DarkGray,
                        )
                    )
                }
            }
        }


    )
}