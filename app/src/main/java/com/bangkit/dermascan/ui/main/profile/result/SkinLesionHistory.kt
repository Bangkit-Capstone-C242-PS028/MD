package com.bangkit.dermascan.ui.main.profile.result

import android.util.Log
import android.widget.ImageView
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.material.icons.filled.Error
import androidx.compose.material.icons.filled.ListAlt
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import coil3.compose.AsyncImage
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition
import com.bangkit.dermascan.R
import com.bangkit.dermascan.data.model.response.SkinLesionItem
import com.bangkit.dermascan.ui.main.feeds.AddTopBar
import com.bangkit.dermascan.ui.main.home.SkinLesionItemWithShimmer
import com.bangkit.dermascan.ui.theme.LightBlue
import com.bangkit.dermascan.ui.main.scan.upload.SkinLesionViewModel
import com.bangkit.dermascan.ui.theme.Blue
import com.bangkit.dermascan.ui.theme.Typography
import com.bangkit.dermascan.ui.theme.White
import com.bangkit.dermascan.util.Result
import com.bangkit.dermascan.util.formatTimestampToGMT
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SkinLesionHistoryScreen(navController: NavController) {
    val skinLesionViewModel: SkinLesionViewModel = hiltViewModel()
    val lesionsPaging: LazyPagingItems<SkinLesionItem> = skinLesionViewModel.skinLesion.collectAsLazyPagingItems()

    val context = LocalContext.current
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Skin Lesion History") },
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .padding(paddingValues)
                .padding(horizontal = 16.dp)
        ) {
            // Render items
            items(lesionsPaging.itemCount) { index ->
                lesionsPaging[index]?.let { lesion ->
                    SkinLesionItem(
                        navController = navController,
                        skinLesion = lesion
                    )
                    Toast.makeText(context, "INI PAKE PAGING", Toast.LENGTH_SHORT).show()
//                    LesionHistoryCard(
//                        lesion = lesion,
//                        onClick = {
//                            // Navigate to detail screen if needed
//                            // navController.navigate("lesion_detail/${lesion.id}")
//                        }
//                    )
                }
            }

            // Handle loading and error states
            lesionsPaging.apply {
                when {
                    loadState.refresh is LoadState.Loading -> {
                        item {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                CircularProgressIndicator()
                            }
                        }
                    }

                    loadState.refresh is LoadState.Error -> {
                        val error = lesionsPaging.loadState.refresh as LoadState.Error
                        item {
                            ErrorStateComponent(
                                message = error.error.localizedMessage
                                    ?: "Failed to load skin lesion history",
                                onRetry = { lesionsPaging.retry() }
                            )
                        }
                    }

                    loadState.append is LoadState.Loading -> {
                        item {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                CircularProgressIndicator(
                                    modifier = Modifier.size(24.dp)
                                )
                            }
                        }
                    }

                    loadState.append is LoadState.Error -> {
                        val error = lesionsPaging.loadState.append as LoadState.Error
                        item {
                            ErrorStateComponent(
                                message = error.error.localizedMessage
                                    ?: "Failed to load more skin lesions",
                                onRetry = { lesionsPaging.retry() }
                            )
                        }
                    }
                }
            }

            // Empty state handling
            if (lesionsPaging.itemCount == 0 && lesionsPaging.loadState.refresh !is LoadState.Loading) {
                item {
                    EmptyStateComponent()
                }
            }
        }
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

        val imageUrl = skinLesion.processedImageUrl ?: skinLesion.originalImageUrl


        // Image using Glide with AndroidView
        AsyncImage(
            model = imageUrl,
            contentDescription = "Skin Lesion Image",
            modifier = Modifier
                .size(200.dp)
                .clip(RoundedCornerShape(8.dp))
        )

        Spacer(modifier = Modifier.height(12.dp))

        // Classification and Status
        Text(
            text = "Classification: ${skinLesion?.classification.let { it ?: "Loading..." }}",
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .background(
                    color = White,
                    shape = RoundedCornerShape(12.dp)
                )
                .padding(horizontal = 12.dp, vertical = 6.dp),
            color = Blue
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Description: ${skinLesion?.description.let { it ?: "No Description" } }",
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .background(
                    color = Blue,
                    shape = RoundedCornerShape(12.dp)
                )
                .padding(horizontal = 12.dp, vertical = 6.dp),
            color = White
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Status: ${skinLesion.status}",
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier
                .background(
                    color = White,
                    shape = RoundedCornerShape(8.dp)
                )
                .padding(horizontal = 12.dp, vertical = 6.dp),
            color = Blue
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Dates
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(horizontal = 8.dp)
        ) {
            Text(
                text = "Created At: ${formatTimestampToGMT(skinLesion.createdAt)}",
                style = MaterialTheme.typography.bodySmall,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .background(
                        color = Blue,
                        shape = RoundedCornerShape(8.dp)
                    )
                    .padding(horizontal = 12.dp, vertical = 6.dp),
                color = White
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Processed At: ${skinLesion?.processedAt?.let { formatTimestampToGMT(it) } ?: "Loading..."}",
                style = MaterialTheme.typography.bodyMedium,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .background(
                        color = White,
                        shape = RoundedCornerShape(8.dp)
                    )
                    .padding(horizontal = 12.dp, vertical = 6.dp),
                color = Blue
            )
        }
//
//        Spacer(modifier = Modifier.height(8.dp))

//        // Additional Information
//        Text(
//            text = "Patient ID: ${skinLesion.patientUid}",
//            style = MaterialTheme.typography.bodySmall,
//            color = Color.Gray,
//            textAlign = TextAlign.Center
//        )

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
                .padding(horizontal = 16.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Blue,
                contentColor = LightBlue
            )
        ) {
            Text(text = "Consultation",
                style = Typography.labelLarge,
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp)
        }
    }
}


@Composable
fun ErrorStateComponent(
    message: String,
    onRetry: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = Icons.Default.Error,
            contentDescription = "Error",
            tint = MaterialTheme.colorScheme.error,
            modifier = Modifier.size(48.dp)
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = message,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.error
        )
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = onRetry) {
            Text("Retry")
        }
    }
}

@Composable
fun EmptyStateComponent() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                imageVector = Icons.Default.ListAlt,
                contentDescription = "No Lesions",
                modifier = Modifier.size(48.dp),
                tint = MaterialTheme.colorScheme.secondary
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "No skin lesion history found",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.secondary
            )
        }
    }
}

