package com.bangkit.dermascan.ui.login

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.bangkit.dermascan.ui.register.CustomTextField
import com.bangkit.dermascan.ui.register.isValidePassword
import com.bangkit.dermascan.ui.theme.*

@Composable
fun LoginScreen(context: Context, onLoginSuccess: () -> Unit) {
    val viewModel : AuthViewModel = hiltViewModel()

    val email = rememberSaveable { mutableStateOf("") }
    val password = rememberSaveable { mutableStateOf("") }
    var passwordVisibility by rememberSaveable { mutableStateOf(false) }
    var emailError by rememberSaveable { mutableStateOf("") }
    var passwordError by rememberSaveable { mutableStateOf("") }

    // Validasi email secara real-time
    fun validateEmail(input: String) {
        email.value = input
        emailError = if (android.util.Patterns.EMAIL_ADDRESS.matcher(input).matches()) {
            ""
        } else if(input.isBlank()){
            ""
        } else {
            "Invalid email address"
        }
    }

    // Validasi password secara real-time
    fun validatePassword(input: String) {
        password.value = input
        passwordError = if (input.length >= 8 && input.isNotEmpty()) {
            ""
        }else if(input.isBlank()){
            ""
        }else {
            "Password must be at least 8 characters"
        }
    }

    fun validateInputs(): Boolean {

        if ( email.value.isBlank() ||
            password.value.isBlank() ) {
            Toast.makeText(context, "Please fill in all fields", Toast.LENGTH_SHORT).show()
            return false
        }

        if (!isValidePassword(password.value)) {
            Toast.makeText(context, "Password must be at least 8 characters long and contain a mix of upper and lower case letters, numbers, and special characters", Toast.LENGTH_LONG).show()
            return false
        }

        return true
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp) // Padding di sekitar layar
    ) {
        // Teks "Hello!" di bagian atas
        Text(
            text = "Hello!",
            style = Typography.titleLarge,
            fontSize = 34.sp,
            color = Blue,
            modifier = Modifier
                .align(Alignment.TopCenter) // Posisikan teks di atas tengah
                .padding(top = 72.dp) // Beri jarak dari atas
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally, // Elemen rata tengah secara horizontal
        verticalArrangement = Arrangement.Center // Mengatur jarak antar elemen
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            CustomTextField(value = email, label = "Email",emailError = emailError, onValueChange = { validateEmail(it) })
            CustomTextField(
                value = password,
                label = "Password",
                passwordError = passwordError,
                passwordVisibility = passwordVisibility,
                onPasswordVisibilityToggle = { passwordVisibility = !passwordVisibility },
                onValueChange = { validatePassword(it) },
            )


            Spacer(modifier = Modifier.height(16.dp))

            // Login Button
            Button(
                onClick = {
                    if (validateInputs()) {
                        viewModel.signIn(email.value, password.value, callback = { idToken ->
                            if (idToken != null) {
//                                viewModel.saveSession(UserModel(email.value, idToken))
                                Toast.makeText(context, "Login berhasil", Toast.LENGTH_SHORT).show()
                                onLoginSuccess()
                            } else {
                                Toast.makeText(context, "Login gagal", Toast.LENGTH_SHORT).show()
                            }
                        })
                    }else{
                        Toast.makeText(context, "Input belum valid", Toast.LENGTH_SHORT).show()
                    }

                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Blue,
                    contentColor = White
                ),
                modifier = Modifier
                    .width(207.dp)
                    .height(45.dp)
            ) {
                Text("Login", fontSize = 16.sp)
            }
        }
    }
}



@Preview(showBackground = true)
@Composable
fun LoginScreenPreview() {
    DermaScanTheme {
        LoginScreen(context = LocalContext.current,onLoginSuccess = { /* action setelah login */ })
    }
}