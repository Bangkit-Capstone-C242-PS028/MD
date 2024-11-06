package com.bangkit.dermascan.ui.login

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
import androidx.compose.ui.graphics.Color
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.navigation.NavController
import com.bangkit.dermascan.ui.navigation.AppNavHost
import com.bangkit.dermascan.ui.register.RegisterScreen

@Composable
fun LoginScreen(navController: NavController ,onLoginSuccess: () -> Unit) {
    // UI untuk LoginScreen di sini
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ){
        Column (
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ){
            Button(
                onClick = {
                    onLoginSuccess()
                    navController.navigate("main"){
                        popUpTo("login"){
                            inclusive = true
                        }
                    }
                }, colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.onPrimaryContainer, // Warna latar belakang tombol
                    contentColor = Color.White // Warna teks di dalam tombol
                )
            ) {
                Text("Login")
            }

            Button(
                onClick = {

                    navController.navigate("register"){
                        popUpTo("login"){
                            inclusive = true
                        }
                    }
                }, colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.onPrimaryContainer, // Warna latar belakang tombol
                    contentColor = Color.White // Warna teks di dalam tombol
                )
            ) {
                Text("Pergi ke register")
            }
        }

    }

}