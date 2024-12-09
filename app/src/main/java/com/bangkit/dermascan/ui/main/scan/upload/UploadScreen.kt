package com.bangkit.dermascan.ui.main.scan.upload

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.bangkit.dermascan.util.Result
import coil3.compose.AsyncImage
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition
import com.bangkit.dermascan.R
import com.bangkit.dermascan.ui.main.MainActivity
import com.bangkit.dermascan.ui.authentication.AuthViewModel
import com.bangkit.dermascan.ui.theme.*
import com.bangkit.dermascan.util.byteArrayToUri
import com.bangkit.dermascan.util.compressImageFromUri
import com.bangkit.dermascan.util.reduceFileImage
import com.bangkit.dermascan.util.uriToFile
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.messaging.FirebaseMessaging

@Composable
fun UploadScreen(
    navController: NavHostController,
    viewModel: SharedViewModel,
    onSuccessClick: () -> Unit
) {
    val skinLesionViewModel : SkinLesionViewModel = hiltViewModel()
    val viewModelAuth: AuthViewModel = hiltViewModel()
    val context = LocalContext.current
    val imageUri by viewModel.imageUri.collectAsState()
    val savedImageUri = rememberSaveable(imageUri) { imageUri!! }
    var compressedImageBytes by remember { mutableStateOf<ByteArray?>(null) }
    val uploadResult = skinLesionViewModel.uploadResult.value

    val currentUser = FirebaseAuth.getInstance().currentUser
    val uid = currentUser?.uid

    if (uid != null) {
        // UID berhasil diambil
        FirebaseMessaging.getInstance().subscribeToTopic(uid)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.d("FCM", "Successfully subscribed to topic: $uid")
                } else {
                    Log.e("FCM", "Failed to subscribe to topic", task.exception)
                }
            }
    } else {
        // Tidak ada pengguna yang sedang login
        println("No user is logged in.")
    }

//    val compressedImageBytes = compressImageFromUri(context, savedImageUri, 5)
    LaunchedEffect(savedImageUri) {
        compressedImageBytes = compressImageFromUri(context, savedImageUri, 5)
    }

    LaunchedEffect(uploadResult) {

        if (uploadResult is Result.Success) {
            Toast.makeText(context, "Upload berhasil ", Toast.LENGTH_SHORT).show()
            onSuccessClick() // Callback untuk pindah halaman setelah upload sukses
        }
    }
    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Tampilan Gambar
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(360.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .border(1.dp, Blue),
                contentAlignment = Alignment.Center
            ) {
                AsyncImage(
                    model = compressedImageBytes,
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
                Log.d("UploadScreen.kt", "onCreate called uri : $compressedImageBytes")
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Tombol Upload
            Button(
                onClick = {
                    // Logika upload gambar
                    if (compressedImageBytes != null) {
                        val uri = byteArrayToUri(context, compressedImageBytes!!, "compressed_image.jpg")
                        val imageFile = uriToFile(uri!!, context).reduceFileImage()
                        skinLesionViewModel.uploadImage(imageFile)
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp), // Menambahkan padding horizontal untuk tombol
                colors = ButtonDefaults.buttonColors(Blue),
                enabled = compressedImageBytes != null
            ) {
                Text("Upload Image",
                    color = MaterialTheme.colorScheme.onPrimary
                    )
            }

            Spacer(modifier = Modifier.height(16.dp)) // Memberikan jarak sebelum pesan atau status

            // Status upload result
            when (uploadResult) {
                is Result.Idle -> {
                    Text("Silahkan Tekan Tombol Upload, untuk mengunggah gambar 😁",
                        color = MaterialTheme.colorScheme.onBackground,
                        textAlign = TextAlign.Center,
                        fontSize = Typography.bodyMedium.fontSize)
                }

                is Result.Error -> {
                    Toast.makeText(context, "${uploadResult.message}, Please re Sign In!!", Toast.LENGTH_SHORT).show()
                    // Menangani error dengan logika keluar pengguna jika token kadaluarsa
                    if (uploadResult.message == "Token has expired") {
                        viewModelAuth.signOut(context)
                        viewModelAuth.getSession().observe(context as MainActivity) { user ->
                            if (!user.isLogin) {
                                navController.navigate("onBoard") {
                                    popUpTo("scan_full") { inclusive = true }
                                }
                            }
                        }
                    }
                }
                else -> {
                    /* Idle state, tidak ada tindakan */
                }
            }
        }

        // Loading state
        if (uploadResult is Result.Loading) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.7f)) // Latar belakang gelap transparan
                    .clickable(enabled = false) { } // Mengabaikan klik di background
            ) {
                val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.animation_1732712667451))
                LottieAnimation(
                    composition = composition,
                    iterations = LottieConstants.IterateForever,
                    modifier = Modifier
                        .fillMaxSize()
                        .align(Alignment.Center) // Posisi animasi di tengah
                )
            }
        }
    }

}

@Preview(showBackground = true)
@Composable
fun PreviewScreen(){
    UploadScreen(navController = rememberNavController(), viewModel = SharedViewModel(), onSuccessClick = {})
}