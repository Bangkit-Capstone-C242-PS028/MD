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
import androidx.lifecycle.viewmodel.compose.viewModel
import com.bangkit.dermascan.ui.MainScreen
import com.bangkit.dermascan.ui.MainViewModel
import com.bangkit.dermascan.ui.ViewModelFactory
import com.bangkit.dermascan.ui.theme.*
import com.example.storyapp.data.pref.UserModel

@Composable
fun LoginScreen(context: Context, onLoginSuccess: () -> Unit) {
    val viewModel : MainViewModel = hiltViewModel()

    val email = remember { mutableStateOf("") }
    val password = remember { mutableStateOf("") }
    var passwordVisibility by remember { mutableStateOf(false) }
    var emailError by remember { mutableStateOf("") }
    var passwordError by remember { mutableStateOf("") }

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

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp) // Padding di sekitar layar
    ) {
        // Teks "Hello!" di bagian atas
        Text(
            text = "Hello!",
            style = MaterialTheme.typography.titleLarge,
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
            // Email Input
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                shape = MaterialTheme.shapes.medium,
                color = TextInp,
                shadowElevation = 1.dp
            ) {
                TextField(
                    value = email.value,
                    onValueChange = { validateEmail(it) },  // Validasi email saat mengetik
                    label = { Text("Email", style = Typography.labelLarge.copy(color = Blue)) },
                    keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Email),
                    isError = emailError.isNotEmpty(),
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = TextInp,
                        unfocusedContainerColor = TextInp,
                        focusedIndicatorColor = Blue,
                        cursorColor = Blue
                    ),
                    textStyle = MaterialTheme.typography.bodyLarge.copy(
                        color = Black,
                        fontWeight = FontWeight.Medium
                    ),
                    supportingText = {
                        if (emailError.isNotEmpty()) {
                            Text(text = emailError, color = Color.Red, style = MaterialTheme.typography.bodySmall)
                        }
                    }
                )
            }

            // Password Input
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                shape = MaterialTheme.shapes.medium,
                color = TextInp,
                shadowElevation = 1.dp
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    TextField(
                        value = password.value,
                        onValueChange = { validatePassword(it) },  // Validasi password saat mengetik
                        label = { Text("Password", style = Typography.labelLarge.copy(color = Blue)) },
                        keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Password),
                        visualTransformation = if (passwordVisibility) VisualTransformation.None else PasswordVisualTransformation(),
                        isError = passwordError.isNotEmpty(),
                        colors = TextFieldDefaults.colors(
                            focusedContainerColor = TextInp,
                            unfocusedContainerColor = TextInp,
                            focusedIndicatorColor = Blue,
                            cursorColor = Blue
                        ),
                        textStyle = MaterialTheme.typography.bodyLarge.copy(
                            color = Black,
                            fontWeight = FontWeight.Medium
                        ),
                        supportingText = {
                            if (passwordError.isNotEmpty()) {
                                Text(text = passwordError, color = Color.Red, style = MaterialTheme.typography.bodySmall)
                            }
                        },
                        modifier = Modifier.weight(1f) // Agar row tidak memotong TextField
                    )
                    IconButton(onClick = { passwordVisibility = !passwordVisibility }) {
                        Icon(
                            imageVector = if (passwordVisibility) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                            contentDescription = "Toggle password visibility"
                        )
                    }
                }
            }

//            // Pastikan pesan kesalahan password ditampilkan di bawah input
//            if (passwordError.isNotEmpty()) {
//                Text(text = passwordError, color = Color.Red, style = MaterialTheme.typography.bodySmall)
//            }

            Spacer(modifier = Modifier.height(16.dp))

            // Login Button
            Button(
                onClick = {
                    // Hanya lanjutkan login jika tidak ada error
                    if ((emailError.isEmpty() && passwordError.isEmpty()) && (email.value.isNotBlank() && password.value.isNotBlank())) {

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
                        Toast.makeText(context, "Email dan pasword masih blank", Toast.LENGTH_SHORT).show()
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