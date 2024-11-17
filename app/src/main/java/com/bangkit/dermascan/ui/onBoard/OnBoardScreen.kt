package com.bangkit.dermascan.ui.onBoard

import android.app.Activity
import android.content.ContentValues.TAG
import android.content.Intent
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
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
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.Icon
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
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
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.auth

@Composable
fun OnBoardScreen(navController: NavController, onLoginSuccess: () -> Unit) {
    val context = LocalContext.current
    val viewModel : MainViewModel = viewModel(
        factory = ViewModelFactory.getInstance(context)
    )

    val signInResult = rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val data: Intent? = result.data
            viewModel.handleSignInResult(data)
        }
    }

    val signInStatus by viewModel.signInStatus.observeAsState()



//    val auth : FirebaseAuth = FirebaseAuth.getInstance()
//    val firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()
    val auth: FirebaseAuth = Firebase.auth
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

            Spacer(modifier = Modifier.height(24.dp))  // Spacer untuk memberi jarak antar tombol

            Button(
                onClick = {
                    // Tambahkan fungsi untuk Sign In with Google di sini
                    val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                        .requestIdToken(context.getString(R.string.default_web_client_id)) // Ambil dari Firebase Console
                        .requestEmail()
                        .build()

                    val googleSignInClient = GoogleSignIn.getClient(context, gso)

                    // Memulai proses sign-in
                    val signInIntent = googleSignInClient.signInIntent
                    signInResult.launch(signInIntent) // Meluncurkan intent sign-in
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Blue, // Warna latar belakang tombol
                    contentColor = LightBlue // Warna teks di dalam tombol
                ),
                modifier = Modifier
                    .wrapContentHeight()
                    .width(207.dp)  // Lebar tombol
//                    .height(50.dp)  // Tinggi tombol
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    // Tambahkan ikon Google
                    Icon(
                        painter = painterResource(id = R.drawable.ic_google), // Pastikan kamu menambahkan ikon Google ke drawable
                        contentDescription = "Google Icon",
                        modifier = Modifier.width(50.dp), // Ukuran ikon
                        tint = Color.Unspecified // Membuat warna ikon mengikuti warna aslinya dari file drawable
                    )
                    Spacer(modifier = Modifier.width(8.dp)) // Spasi antara ikon dan teks
                    Text("Sign In with Google", fontSize = 16.sp)
                }
            }
        }
    }

    // Menavigasi ke mainScreen setelah login berhasil
    LaunchedEffect(signInStatus) {
        if (signInStatus == true) {
            navController.navigate("main") {
                popUpTo("onBoard") { inclusive = true }
            }
        }
    }
}


private fun signInWithEmailPassword(auth: FirebaseAuth, email: String, password: String, navController: NavController) {
    auth.signInWithEmailAndPassword(email, password)
        .addOnCompleteListener { task ->
            if (task.isSuccessful) {
                // Navigate to MainActivity after successful login
                navController.navigate("main") {
                    popUpTo("onBoard") { inclusive = true }
                }
            } else {
                Log.w(TAG, "signInWithEmailPassword:failure", task.exception)
                // Handle failure (e.g., show a toast)
            }
        }
}


@Preview(showBackground = true)
@Composable
fun OnBoardScreenPreview() {
    val navController = rememberNavController()
    OnBoardScreen(navController = navController, onLoginSuccess = {})
}


