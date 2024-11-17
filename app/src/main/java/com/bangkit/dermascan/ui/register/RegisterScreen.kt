package com.bangkit.dermascan.ui.register

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.input.VisualTransformation
import com.bangkit.dermascan.ui.theme.*
import com.bangkit.dermascan.ui.theme.White
import java.text.SimpleDateFormat
import androidx.compose.material.icons.filled.*
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import com.bangkit.dermascan.ui.login.LoginScreen
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterScreen(onRegisterSuccess: () -> Unit) {
    val fullName = remember { mutableStateOf("") }
    val email = remember { mutableStateOf("") }
    val password = remember { mutableStateOf("") }
    val mobileNumber = remember { mutableStateOf("") }
    val birthday = remember { mutableStateOf("") }
    var passwordVisibility by remember { mutableStateOf(false) }
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.TopCenter
    ) {
        Text(
            text = "Hello!",  // Nama aplikasi
            style = MaterialTheme.typography.titleLarge,
            fontSize = 34.sp,  // Ukuran teks
            color = Blue,
            modifier = Modifier.padding(top = 72.dp)  // Memberikan jarak dari bagian atas
        )
    }

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .padding(24.dp)  // Menambahkan padding di sekeliling kolom
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Full Name Input with wrapper
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),  // Padding antara field
                shape = MaterialTheme.shapes.medium,
                color = TextInp,
                shadowElevation = 1.dp
            ) {
                TextField(
                    value = fullName.value,
                    onValueChange = { fullName.value = it },
                    label = {
                        Text("Full Name",
                        style = Typography.labelLarge.copy(color = Blue)
//                        color = Blue
                    )},
                    modifier = Modifier.fillMaxWidth(),
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = TextInp,
                        unfocusedContainerColor = TextInp,
                        disabledContainerColor = TextInp,
                        focusedIndicatorColor = Blue,
                        focusedLabelColor = Blue,
                        cursorColor = Blue, // Warna kursor
                    ),
                    textStyle = MaterialTheme.typography.bodyLarge.copy(
                        color = if (fullName.value.isNotEmpty()) Black else Black, // Ganti warna teks tergantung kondisi
                        fontWeight = FontWeight.Medium // Mengatur ketebalan font
                    )
                )
            }

            // Email Input with wrapper
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
                    onValueChange = { email.value = it },
                    label = { Text("Email",
                        style = Typography.labelLarge.copy(color = Blue)
                    )},
                    keyboardOptions = KeyboardOptions.Default.copy(
                        keyboardType = KeyboardType.Email
                    ),
                    modifier = Modifier.fillMaxWidth(),
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = TextInp,
                        unfocusedContainerColor = TextInp,
                        disabledContainerColor = TextInp,
                        focusedIndicatorColor = Blue,
                        focusedLabelColor = Blue,
                        cursorColor = Blue, // Warna kursor
                    ),
                    textStyle = MaterialTheme.typography.bodyLarge.copy(
                        color = if (email.value.isNotEmpty()) Black else Black, // Ganti warna teks tergantung kondisi
                        fontWeight = FontWeight.Medium // Mengatur ketebalan font
                    )
                )
            }

            // Password Input with wrapper
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                shape = MaterialTheme.shapes.medium,
                color = TextInp,
                shadowElevation = 1.dp
            ) {
                Row(modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ){
                    TextField(
                        value = password.value,
                        onValueChange = { password.value = it },
                        label = { Text("Password",
                            style = Typography.labelLarge,
                            color = Blue
                        )},
                        keyboardOptions = KeyboardOptions.Default.copy(
                            keyboardType = KeyboardType.Password
                        ),
                        modifier = Modifier.fillMaxWidth(),
                        colors = TextFieldDefaults.colors(
                            focusedContainerColor = TextInp,
                            unfocusedContainerColor = TextInp,
                            disabledContainerColor = TextInp,
                            focusedIndicatorColor = Blue,
                            focusedLabelColor = Blue,
                            cursorColor = Blue, // Warna kursor
                        ),
                        textStyle = MaterialTheme.typography.bodyLarge.copy(
                            color = if (password.value.isNotEmpty()) Black else Black, // Ganti warna teks tergantung kondisi
                            fontWeight = FontWeight.Medium // Mengatur ketebalan font
                        ),
                        visualTransformation = if (passwordVisibility) VisualTransformation.None else PasswordVisualTransformation()
                    )
                    IconButton(
                        onClick = { passwordVisibility = !passwordVisibility },
                        modifier = Modifier.padding(vertical = 8.dp)
                    ) {
                        Icon(
                            imageVector = if (passwordVisibility) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                            contentDescription = "Toggle password visibility"
                        )
                    }
                }

            }



            // Mobile Number Input with wrapper
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                shape = MaterialTheme.shapes.medium,
                color = Blue,
                shadowElevation = 1.dp
            ) {
                TextField(
                    value = mobileNumber.value,
                    onValueChange = { mobileNumber.value = it },
                    label = { Text("Mobile Number",
                        style = Typography.labelLarge,
                        color = Blue
                    )},
                    keyboardOptions = KeyboardOptions.Default.copy(
                        keyboardType = KeyboardType.Phone
                    ),
                    modifier = Modifier.fillMaxWidth(),
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = TextInp,
                        unfocusedContainerColor = TextInp,
                        disabledContainerColor = TextInp,
                        focusedIndicatorColor = Blue,
                        focusedLabelColor = Blue,
                        cursorColor = Blue, // Warna kursor
                    ),
                    textStyle = MaterialTheme.typography.bodyLarge.copy(
                        color = if (mobileNumber.value.isNotEmpty()) Black else Black, // Ganti warna teks tergantung kondisi
                        fontWeight = FontWeight.Medium // Mengatur ketebalan font
                    )
                )
            }

            // Birthday Input with wrapper
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                shape = MaterialTheme.shapes.medium,
                color = TextInp,
                shadowElevation = 1.dp
            ) {
                TextField(
                    value = birthday.value,
                    onValueChange = { birthday.value = it },
                    label = { Text("Birthday (dd/MM/yyyy)",
                        style = Typography.labelLarge,
                        color = Blue
                    )},
                    keyboardOptions = KeyboardOptions.Default.copy(
                        keyboardType = KeyboardType.Phone
                    ),
                    modifier = Modifier.fillMaxWidth(),
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = TextInp,
                        unfocusedContainerColor = TextInp,
                        disabledContainerColor = TextInp,
                        focusedIndicatorColor = Blue,
                        focusedLabelColor = Blue,
                        cursorColor = Blue, // Warna kursor
                    ),
                    textStyle = MaterialTheme.typography.bodyLarge.copy(
                        color = if (birthday.value.isNotEmpty()) Black else Black, // Ganti warna teks tergantung kondisi
                        fontWeight = FontWeight.Medium // Mengatur ketebalan font
                    )
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Sign Up Button
            Button(
                onClick = {
                    // Handle registration logic here
                    onRegisterSuccess()
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Blue,
                    contentColor = White
                ),
                modifier = Modifier
                    .width(207.dp)  // Mengatur lebar tombol
                    .height(45.dp)
            ) {
                Text("Sign Up", fontSize = 16.sp)
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun RegisterScreenPreview() {
    DermaScanTheme {
        RegisterScreen(onRegisterSuccess = { /* action setelah login */ })
    }
}

