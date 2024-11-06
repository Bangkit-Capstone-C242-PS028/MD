package com.bangkit.dermascan.ui.register

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

@Composable
fun RegisterScreen(onRegisterSuccess: () -> Unit) {

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ){
//        // UI untuk RegisterScreen di sini
//        Button(onClick = { onRegisterSuccess() }) {
//            Text("Register")
//        }

        Button(
            onClick = {
                onRegisterSuccess()
            }, colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.onPrimaryContainer, // Warna latar belakang tombol
                contentColor = Color.White // Warna teks di dalam tombol
            )
        ) {
            Text("Register")
        }
    }

}