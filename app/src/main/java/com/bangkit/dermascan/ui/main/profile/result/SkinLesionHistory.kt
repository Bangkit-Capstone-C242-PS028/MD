package com.bangkit.dermascan.ui.main.profile.result

import android.util.Log
import android.widget.ImageView
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil3.compose.AsyncImage
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition
import com.bangkit.dermascan.R
import com.bangkit.dermascan.data.model.response.SkinLesionItem
import com.bangkit.dermascan.ui.theme.LightBlue
import com.bangkit.dermascan.ui.main.scan.upload.SkinLesionViewModel
import com.bangkit.dermascan.util.Result
import com.bangkit.dermascan.util.formatTimestamp
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SkinLesionHistoryScreen(navController: NavController) {
    val skinLesionViewModel: SkinLesionViewModel = hiltViewModel()

    // Observasi hasil dari StateFlow
    val skinLesionsResult by skinLesionViewModel.skinLesions.collectAsState()

    // Menunggu data untuk dimuat
    LaunchedEffect(Unit) {
        skinLesionViewModel.fetchSkinLesions()
    }

    // Menangani status berdasarkan Result
    when (val result = skinLesionsResult) {
        is Result.Loading -> {
            // Tampilkan loading indicator jika data sedang dimuat
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
        is Result.Success -> {
            val lesions = result.data
            // Jika data berhasil dimuat, tampilkan daftar SkinLesion
            Scaffold(
                topBar = {
                    TopAppBar(
                        title = { Text("Skin Lesion History") },
                        navigationIcon = {
                            IconButton(onClick = { navController.navigateUp() }) {
                                Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                            }
                        }
                    )
                }
            ) { paddingValues ->
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                        .padding(16.dp)
                ) {
                    items(lesions) { index ->
                       SkinLesionItem(navController, index)
                    }
                }
            }
        }
        is Result.Error -> {
            // Jika terjadi error, tampilkan pesan error
            Scaffold(
                topBar = {
                    TopAppBar(
                        title = { Text("Skin Lesion History") },
                        navigationIcon = {
                            IconButton(onClick = { navController.navigateUp() }) {
                                Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                            }
                        }
                    )
                }
            ) { paddingValues ->
                Log.e("SkinLesionHistoryScreen", "Error: ${result.message}")
                Text(
                    text = result.message,
                    modifier = Modifier.padding(paddingValues).fillMaxSize()
                )
            }
        }

        Result.Idle -> {}
    }
}

@Composable
fun SkinLesionItem(
    navController: NavController,
    skinLesion: SkinLesionItem,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp)
            .background(
                color = LightBlue,
                shape = RoundedCornerShape(12.dp)
            )
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Display processed image if available, otherwise original image
        val imageUrl = skinLesion.processedImageUrl

        // Image using Glide with AndroidView
        AsyncImage(
            model = imageUrl,
            contentDescription = null,
            modifier = Modifier
                .size(200.dp)
                .clip(RoundedCornerShape(8.dp))
        )

        Spacer(modifier = Modifier.height(12.dp))

        // Classification and Status
        Text(
            text = "Classification: ${skinLesion.classification}",
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .background(
                    color = Color.Blue.copy(alpha = 0.1f),
                    shape = RoundedCornerShape(12.dp)
                )
                .padding(horizontal = 12.dp, vertical = 6.dp)
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Status: ${skinLesion.status}",
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier
                .background(
                    color = Color.Blue.copy(alpha = 0.2f),
                    shape = RoundedCornerShape(8.dp)
                )
                .padding(horizontal = 12.dp, vertical = 6.dp)
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Dates
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(horizontal = 8.dp)
        ) {
            Text(
                text = "Created At: ${formatTimestamp(skinLesion.createdAt)}",
                style = MaterialTheme.typography.bodySmall,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = "Processed At: ${formatTimestamp(skinLesion.processedAt)}",
                style = MaterialTheme.typography.bodySmall,
                textAlign = TextAlign.Center
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Additional Information
        Text(
            text = "Patient ID: ${skinLesion.patientUid}",
            style = MaterialTheme.typography.bodySmall,
            color = Color.Gray,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Button for Consultation
        Button(
            onClick = {
                // Handle consultation navigation or action here
                // For example, navigate to a consultation screen
                 navController.navigate("consultation")
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
        ) {
            Text(text = "Consultation")
        }
    }
}



