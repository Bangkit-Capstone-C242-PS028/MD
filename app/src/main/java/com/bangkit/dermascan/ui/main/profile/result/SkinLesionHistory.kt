package com.bangkit.dermascan.ui.main.profile.result

import android.util.Log
import android.widget.ImageView
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ListAlt
import androidx.compose.material.icons.filled.Error
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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.layout.ContentScale
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Divider
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
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

import com.bangkit.dermascan.ui.main.profile.settings.SettingsViewModel
import com.bangkit.dermascan.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SkinLesionHistoryScreen(navController: NavController) {
    val skinLesionViewModel: SkinLesionViewModel = hiltViewModel()
    val lesionsPaging: LazyPagingItems<SkinLesionItem> = skinLesionViewModel.skinLesion.collectAsLazyPagingItems()
    val settingsViewModel: SettingsViewModel = hiltViewModel()
    val isDarkMode by settingsViewModel.isDarkTheme.collectAsState(initial = false)

    val context = LocalContext.current
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Skin Lesion History") },
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = if (isDarkMode) CardBgDark else Blue,
                    titleContentColor = if (isDarkMode) White else LightBlue,
                    navigationIconContentColor = if (isDarkMode) White else White
                )
            )
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 16.dp)
        ) {
            // Render items
            items(lesionsPaging.itemCount) { index ->
                lesionsPaging[index]?.let { lesion ->
                    SkinLesionItem(
                        navController = navController,
                        skinLesion = lesion,
                        isDarkMode = isDarkMode
                    )
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
    isDarkMode: Boolean,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isDarkMode) CardBgDark else LightBlue,
            contentColor = if (isDarkMode) White else Blue
        ),
        border = androidx.compose.foundation.BorderStroke(1.dp, if (isDarkMode) Color.DarkGray else BorderGray),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                val imageUrl = skinLesion.processedImageUrl ?: skinLesion.originalImageUrl

                AsyncImage(
                    model = imageUrl,
                    contentDescription = "Skin Lesion Image",
                    modifier = Modifier
                        .size(90.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .border(1.dp, if (isDarkMode) Color.DarkGray else BorderGray, RoundedCornerShape(12.dp)),
                    contentScale = ContentScale.Crop
                )

                Spacer(modifier = Modifier.width(12.dp))

                Column(
                    verticalArrangement = Arrangement.spacedBy(6.dp),
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = skinLesion.classification ?: "Classification: Unknown",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = if (isDarkMode) White else Blue,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )

                    // Status Badge
                    Surface(
                        color = when (skinLesion.status?.uppercase()) {
                            "SUCCESS" -> Color(0xFFE8F5E9)
                            "PENDING" -> Color(0xFFFFF3E0)
                            else -> Color(0xFFFFEBEE)
                        },
                        contentColor = when (skinLesion.status?.uppercase()) {
                            "SUCCESS" -> Color(0xFF2E7D32)
                            "PENDING" -> Color(0xFFE65100)
                            else -> Color(0xFFC62828)
                        },
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text(
                            text = skinLesion.status ?: "PENDING",
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.SemiBold,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                        )
                    }
                }
            }

            if (skinLesion.description != null && skinLesion.description.isNotEmpty()) {
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = skinLesion.description,
                    style = MaterialTheme.typography.bodySmall,
                    color = if (isDarkMode) Color.LightGray else Color.DarkGray,
                    maxLines = 3,
                    overflow = TextOverflow.Ellipsis
                )
            }

            Spacer(modifier = Modifier.height(12.dp))
            Divider(color = Blue.copy(alpha = 0.1f))
            Spacer(modifier = Modifier.height(8.dp))

            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Column {
                    Text(
                        text = "Created At",
                        style = MaterialTheme.typography.labelSmall,
                        color = if (isDarkMode) Color.LightGray else Color.Gray
                    )
                    Text(
                        text = formatTimestampToGMT(skinLesion.createdAt),
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.Medium,
                        color = if (isDarkMode) LightBlue else Blue
                    )
                }
                Column(horizontalAlignment = Alignment.End) {
                    Text(
                        text = "Processed At",
                        style = MaterialTheme.typography.labelSmall,
                        color = if (isDarkMode) Color.LightGray else Color.Gray
                    )
                    Text(
                        text = skinLesion.processedAt?.let { formatTimestampToGMT(it) } ?: "-",
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.Medium,
                        color = if (isDarkMode) LightBlue else Blue
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Button for Consultation
            Button(
                onClick = {
                     navController.navigate("consultation")
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Blue,
                    contentColor = White
                )
            ) {
                Text(text = "Consultation",
                    style = Typography.labelLarge,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp)
            }
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
                imageVector = Icons.AutoMirrored.Filled.ListAlt,
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

