package com.bangkit.dermascan.ui.onBoard

import androidx.activity.viewModels
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.bangkit.dermascan.R
import com.bangkit.dermascan.ui.MainViewModel
import com.bangkit.dermascan.ui.ViewModelFactory
import com.bangkit.dermascan.ui.theme.*


//viewModel.getSession().observe(this){user ->
//    if (user.isLogin){
//
//    }
//}
//private val viewModel by viewModels<MainViewModel> {
//    ViewModelFactory.getInstance(this)
//}


@Composable
fun OnBoardScreen(navController: NavController, onLoginSuccess: () -> Unit) {


    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.TopCenter  // Menempatkan teks di bagian atas tengah
    ) {
        Text(
            text = "Hello!",  // Nama aplikasi
            style = MaterialTheme.typography.titleLarge,
            fontSize = 34.sp,  // Ukuran teks
            color = Blue,
            modifier = Modifier.padding(top = 72.dp)  // Memberikan jarak dari bagian atas
        )
    }
    // UI untuk LoginScreen di sini
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.padding(16.dp)  // Menambahkan padding agar konten tidak terlalu rapat dengan tepi layar
        ) {

            // Nama aplikasi dan deskripsi
            Text(
                text = stringResource(id = R.string.app_name),  // Mengambil string dari resource"DermaScan",  // Nama aplikasi
                style = MaterialTheme.typography.titleLarge,
                fontSize = 58.sp,// Gaya teks untuk nama aplikasi
                color = Blue,
                modifier = Modifier.padding(bottom = 50.dp)  // Padding bawah agar tidak terlalu dekat dengan deskripsi
            )
            Text(
                text = "Scan your skin health with ease.",  // Deskripsi aplikasi
                style = Typography.bodyLarge,  // Gaya teks untuk deskripsi
                color = Black,
                fontSize = 16.sp,
                modifier = Modifier.padding(bottom = 32.dp)  // Padding bawah agar ada jarak antara deskripsi dan tombol
            )

            // Tombol Log In
            Button(
                onClick = {
                    onLoginSuccess()
                    navController.navigate("login") {
                        popUpTo("onBoard") {
                            inclusive = false
                        }
                    }
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Blue, // Warna latar belakang tombol
                    contentColor = White // Warna teks di dalam tombol
                ),
                modifier = Modifier
                    .width(207.dp)  // Mengatur lebar tombol agar memenuhi lebar layar
                    .height(45.dp)  // Mengatur tinggi tombol
            ) {
                Text("Log In", fontSize = 24.sp)
            }

            Spacer(modifier = Modifier.height(24.dp))  // Spacer untuk memberi jarak antar tombol

            // Tombol Sign Up
            Button(
                onClick = {
                    navController.navigate("register") {
                        popUpTo("onBoard") {
                            inclusive = false
                        }
                    }
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = LightBlue, // Warna latar belakang tombol
                    contentColor = Blue // Warna teks di dalam tombol
                ),
                modifier = Modifier
                    .width(207.dp)  // Mengatur lebar tombol agar memenuhi lebar layar
                    .height(45.dp)  // Mengatur tinggi tombol
            ) {
                Text("Sign Up", fontSize = 24.sp)
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun OnBoardScreenPreview() {
    val navController = rememberNavController()
    OnBoardScreen(navController = navController, onLoginSuccess = {})
}


