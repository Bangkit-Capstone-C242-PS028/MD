package com.bangkit.dermascan.ui.home

//import com.bangkit.dermascan.dataArticles.AccesToken
//import com.bangkit.dermascan.dataArticles.signIn
//import androidx.compose.foundation.layout.FlowColumnScopeInstance.align
import android.annotation.SuppressLint
import android.util.Log
import android.widget.ImageView
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.bangkit.dermascan.data.model.response.SkinLesionItem
import com.bangkit.dermascan.ui.theme.Blue
import com.bangkit.dermascan.ui.theme.LightBlue
import com.bangkit.dermascan.ui.theme.Typography
import com.bangkit.dermascan.ui.upload.SkinLesionViewModel
import com.bangkit.dermascan.util.Result
import com.bangkit.dermascan.util.formatTimestamp
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions

//@Preview(showBackground = true)
@Composable
fun HomeScreen(navController: NavController) {
    val skinLesionViewModel: SkinLesionViewModel = hiltViewModel()
    val skinLesionsResult by skinLesionViewModel.skinLesions.collectAsState()

    LaunchedEffect(Unit) {
        skinLesionViewModel.fetchSkinLesions()
    }

    when (val result = skinLesionsResult) {
        is Result.Loading -> {
            // Tampilkan shimmer loading untuk daftar skin lesion
            Column(
                horizontalAlignment = Alignment.Start,
                modifier = Modifier.padding(16.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "History:",
                        style = Typography.titleLarge
                    )

                    // Tetap tampilkan "See All" saat loading
                    Text(
                        text = "See All",
                        style = Typography.bodyMedium.copy(color = Blue),
                        modifier = Modifier.clickable {
                            navController.navigate("skinLesionHistory")
                        }
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                LazyRow(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(8.dp)
                ) {
                    // Tampilkan 3-5 item shimmer
                    items(5) {
                        SkinLesionItemWithShimmer(
                            isLoading = true,
                            modifier = Modifier.padding(end = 8.dp)
                        )
                    }
                }
            }
        }
        is Result.Success -> {
            val lesions = result.data
            Column(
                horizontalAlignment = Alignment.Start,
                modifier = Modifier.padding(16.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "History:",
                        style = Typography.titleLarge
                    )

                    Text(
                        text = "See All",
                        style = Typography.bodyMedium.copy(color = Blue),
                        modifier = Modifier.clickable {
                            navController.navigate("skinLesionHistory")
                        }
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                LazyRow(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(8.dp)
                ) {
                    items(lesions) { skinLesion ->
                        SkinLesionItemWithShimmer(
                            isLoading = false,
                            skinLesion = skinLesion,
                            modifier = Modifier.padding(end = 8.dp)
                        )
                    }
                }
            }
        }
        is Result.Error -> {
            Scaffold { paddingValues ->
                Log.e("SkinLesionHistoryScreen", "Error: ${result.message}")
                Text(
                    text = result.message,
                    modifier = Modifier
                        .padding(paddingValues)
                        .fillMaxSize()
                )
            }
        }

        Result.Idle -> {}
    }
}
@Composable
fun SkinLesionItem(
    skinLesion: SkinLesionItem,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .width(400.dp)  // Tetapkan lebar jika diperlukan
            // .height(250.dp)  // Hapus baris ini agar tinggi menyesuaikan konten
            .padding(16.dp)
            .background(
                color = LightBlue,
                shape = RoundedCornerShape(12.dp)
            ).padding(16.dp),
        verticalAlignment = Alignment.CenterVertically // Tetap memusatkan konten secara vertikal
    ){
        // Display processed image if available, otherwise original image
        val imageUrl = skinLesion.processedImageUrl.takeIf { it.isNotEmpty() }
            ?: skinLesion.originalImageUrl

        // Image using Glide with AndroidView
        AndroidView(
            factory = { context ->
                ImageView(context).apply {
                    Glide.with(context)
                        .load(imageUrl)
                        .apply(RequestOptions().centerCrop())
                        .into(this)
                }
            },
            modifier = Modifier
                .size(100.dp) // Ukuran gambar lebih kecil untuk horizontal layout
                .clip(RoundedCornerShape(8.dp))
        )

        Spacer(modifier = Modifier.width(16.dp)) // Spacer horizontal

        // Informasi Skin Lesion
        Column(
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.weight(1f) // Mengisi sisa ruang horizontal
        ) {
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
            Text(
                text = "Created At: ${formatTimestamp(skinLesion.createdAt)}",
                style = MaterialTheme.typography.bodySmall,
                textAlign = TextAlign.Start
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Processed At: ${formatTimestamp(skinLesion.processedAt)}",
                style = MaterialTheme.typography.bodySmall,
                textAlign = TextAlign.Start
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Additional Information
            Text(
                text = "Patient ID: ${skinLesion.patientUid}",
                style = MaterialTheme.typography.bodySmall,
                color = Color.Gray,
                textAlign = TextAlign.Start
            )
        }
    }
}

@Composable
fun SkinLesionItemWithShimmer(
    isLoading: Boolean,
    skinLesion: SkinLesionItem? = null,
    @SuppressLint("ModifierParameter") modifier: Modifier = Modifier
) {
    if (isLoading) {
        // Implementasi Shimmer Effect dengan Modifier
        Row(
            modifier = modifier
                .width(400.dp)
//                .height(150.dp)
                .shimmer() // Gunakan ekstensi shimmer
                .background(
                    color = LightBlue,
                    shape = RoundedCornerShape(12.dp)
                ).padding(16.dp)
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Shimmer untuk gambar
            Box(
                modifier = Modifier
                    .size(100.dp)
                    .background(Color.Gray.copy(alpha = 0.3f), shape = RoundedCornerShape(8.dp))
            )

            Spacer(modifier = Modifier.width(16.dp))

            // Shimmer untuk informasi teks
            Column(
                verticalArrangement = Arrangement.Center,
                modifier = Modifier.weight(1f)
            ) {
                // Garis shimmer untuk klasifikasi
                Box(
                    modifier = Modifier
                        .fillMaxWidth(0.7f)
                        .height(20.dp)
                        .background(Color.Gray.copy(alpha = 0.3f), shape = RoundedCornerShape(4.dp))
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Garis shimmer untuk status
                Box(
                    modifier = Modifier
                        .fillMaxWidth(0.5f)
                        .height(16.dp)
                        .background(Color.Gray.copy(alpha = 0.3f), shape = RoundedCornerShape(4.dp))
                )

                // Tambahkan beberapa garis shimmer lainnya...
            }
        }
    } else {
        // Konten asli setelah loading selesai (sama seperti sebelumnya)
        Row(
            modifier = modifier
                .width(400.dp)
                .padding(16.dp)
                .background(
                    color = LightBlue,
                    shape = RoundedCornerShape(12.dp)
                )
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            val imageUrl = skinLesion?.processedImageUrl.takeIf { it?.isNotEmpty() ?: false }
                ?: skinLesion?.originalImageUrl

            AndroidView(
                factory = { context ->
                    ImageView(context).apply {
                        Glide.with(context)
                            .load(imageUrl)
                            .apply(RequestOptions().centerCrop())
                            .into(this)
                    }
                },
                modifier = Modifier
                    .size(100.dp)
                    .clip(RoundedCornerShape(8.dp))
            )

            Spacer(modifier = Modifier.width(16.dp))

            Column(
                verticalArrangement = Arrangement.Center,
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = "Classification: ${skinLesion?.classification ?: "Loading..."}",
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "Status: ${skinLesion?.status ?: "Loading..."}",
                    style = MaterialTheme.typography.bodyMedium
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "Created At: ${skinLesion?.createdAt?.let { formatTimestamp(it) } ?: "Loading..."}",
                    style = MaterialTheme.typography.bodySmall
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "Processed At: ${skinLesion?.processedAt?.let { formatTimestamp(it) } ?: "Loading..."}",
                    style = MaterialTheme.typography.bodySmall
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "Patient ID: ${skinLesion?.patientUid ?: "Loading..."}",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.Gray
                )
            }
        }
    }
}

// Ekstensi shimmer untuk Modifier
fun Modifier.shimmer(): Modifier = composed {
    val transition = rememberInfiniteTransition(label = "")
    val translateAnimation by transition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(3000),
            repeatMode = RepeatMode.Restart
        ), label = "Bla"
    )

    background(
        brush = Brush.linearGradient(
            colors = listOf(
                Color.LightGray.copy(alpha = 0.3f),
                Color.LightGray.copy(alpha = 0.5f),
                Color.LightGray.copy(alpha = 0.3f)
            ),
            start = Offset(translateAnimation , 0f),
            end = Offset(translateAnimation, 0f)
        )
    )
}
