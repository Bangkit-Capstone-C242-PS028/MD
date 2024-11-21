package com.bangkit.dermascan.ui.home

import android.annotation.SuppressLint
import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.bangkit.dermascan.R
//import com.bangkit.dermascan.data.AccesToken
//import com.bangkit.dermascan.data.signIn
import com.bangkit.dermascan.ui.MainViewModel
import com.bangkit.dermascan.ui.ViewModelFactory
import kotlinx.coroutines.launch

//@Preview(showBackground = true)
@Composable
fun HomeScreen(navController: NavController) {

    val context = LocalContext.current
    // Menambahkan scroll state untuk memungkinkan scrolling pada Column
    val scrollState = rememberScrollState()

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .verticalScroll(scrollState) // Menambahkan scroll
            .padding(16.dp) // Tambahkan padding jika diinginkan
    ) {
        Text(
            text = "Derma Scan",
            style = MaterialTheme.typography.headlineMedium
        )
        Spacer(modifier = Modifier.height(16.dp))

        Image(
            painter = painterResource(id = R.drawable.cover),
            contentDescription = "Dermatology Image",
            modifier = Modifier
                .size(200.dp)
                .padding(bottom = 16.dp)
        )

        Text(
            text = "Scan your skin with our advanced technology and get insights about your skin health.",
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier
                .padding(
                    bottom = 16.dp,
                    start = 30.dp,
                    end = 30.dp
                )
        )
        ButtonWithCustomColor(navController = navController,context)
    }
}

@SuppressLint("RememberReturnType")
@Composable
fun ButtonWithCustomColor(navController: NavController, context: Context) {
//    val context = LocalContext.current
//    val coroutineScope = rememberCoroutineScope()
//
//    // State untuk menyimpan token
//    var token by remember { mutableStateOf("") }
    val viewModel : MainViewModel = hiltViewModel()
    Button(
        onClick = {
            viewModel.signOut(context)
//            onLogOut()
            navController.navigate("onBoard") {
                popUpTo("main") { inclusive = true }
            }
        },
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.onPrimaryContainer, // Warna latar belakang tombol
            contentColor = Color.White // Warna teks di dalam tombol
        )
    ) {
        Text(text = "Log Out")
    }

    // Menampilkan token di UI
    Spacer(modifier = Modifier.height(16.dp))
//    Text(text = "Access Token: $token")
}