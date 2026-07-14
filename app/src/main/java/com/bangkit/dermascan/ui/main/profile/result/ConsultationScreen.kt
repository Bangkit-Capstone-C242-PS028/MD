package com.bangkit.dermascan.ui.main.profile.result

import android.content.Context
import android.content.Intent
import android.graphics.drawable.Icon
import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.background
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
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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
import com.bangkit.dermascan.data.model.response.DoctorData
import com.bangkit.dermascan.data.model.response.UserData
import com.bangkit.dermascan.ui.main.feeds.AddTopBar
import com.bangkit.dermascan.ui.main.home.SkinLesionItemWithShimmer
import com.bangkit.dermascan.ui.main.profile.settings.SettingsViewModel
import com.bangkit.dermascan.ui.theme.Black
import com.bangkit.dermascan.ui.theme.Blue
import com.bangkit.dermascan.ui.theme.GreenWA
import com.bangkit.dermascan.ui.theme.LightBlue
import com.bangkit.dermascan.ui.theme.Typography
import com.bangkit.dermascan.ui.theme.White
import com.bangkit.dermascan.ui.theme.CardBgDark
import com.bangkit.dermascan.ui.theme.BorderGray
import androidx.compose.foundation.border
import com.bangkit.dermascan.util.Result
import com.bangkit.dermascan.util.formatDate

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ConsultationsScreen(navController: NavController) {
    val context = LocalContext.current
    val viewModel: ConsulViewModel = hiltViewModel()
    val settingsViewModel: SettingsViewModel = hiltViewModel()

    // Observe dark mode state
    val isDarkMode by settingsViewModel.isDarkTheme.collectAsState(initial = false)

    // Mengambil data dokter saat composable pertama kali dipanggil
//    val doctorsResult by viewModel.doctors.observeAsState(Result.Idle)
    val doctorPaging : LazyPagingItems<UserData> = viewModel.consul.collectAsLazyPagingItems()
    // LaunchedEffect untuk mengambil dokter
//    LaunchedEffect(Unit) {
//        viewModel.getDoctors()
//    }
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Consultations") },
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
            items(doctorPaging.itemCount) { index ->
                doctorPaging[index]?.let { doctorData ->
//                    SkinLesionItem(
//                        navController = navController,
//                        skinLesion = lesion
//                    )
                    DoctorItem(
                        context = context,
                        doctor = doctorData,
                        isDarkMode = isDarkMode
                    )
                }
            }

            // Handle loading and error states
            doctorPaging.apply {
                when {
                    loadState.refresh is LoadState.Loading -> {
                        items(5) {

                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                SkinLesionItemWithShimmer(
                                    isLoading = true,
                                    modifier = Modifier.padding(end = 8.dp)
                                )
                            }
                        }
                    }

                    loadState.refresh is LoadState.Error -> {
                        val error = doctorPaging.loadState.refresh as LoadState.Error
                        item {
                            ErrorStateComponent(
                                message = error.error.localizedMessage
                                    ?: "Failed to load skin lesion history",
                                onRetry = { doctorPaging.retry() }
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
                        val error = doctorPaging.loadState.append as LoadState.Error
                        item {
                            ErrorStateComponent(
                                message = error.error.localizedMessage
                                    ?: "Failed to load more skin lesions",
                                onRetry = { doctorPaging.retry() }
                            )
                        }
                    }
                }
            }

            // Empty state handling
            if (doctorPaging.itemCount == 0 && doctorPaging.loadState.refresh !is LoadState.Loading) {
                item {
                    EmptyStateComponent()
                }
            }
        }
    }
    // Gunakan Surface untuk background tema
//    Surface(
//        color = if (isDarkMode) MaterialTheme.colorScheme.background
//        else MaterialTheme.colorScheme.surface,
//        contentColor = if (isDarkMode) MaterialTheme.colorScheme.onBackground
//        else MaterialTheme.colorScheme.onSurface
//    ) {
//        Column(modifier = Modifier.fillMaxSize()) {
//            // TopAppBar
//            AddTopBar("Consultations", navController)
//
//            // Konten utama
//            Column(
//                modifier = Modifier
//                    .padding(top = 16.dp, start = 16.dp, end = 16.dp),
//                // Tambahkan warna background sesuai mode
////                backgroundColor = if (isDarkMode)
////                    MaterialTheme.colorScheme.background
////                else MaterialTheme.colorScheme.surface
//            ) {
//                when (doctorsResult) {
//                    is Result.Loading -> {
//                        CircularProgressIndicator(
//                            modifier = Modifier.align(Alignment.CenterHorizontally),
//                            color = if (isDarkMode)
//                                MaterialTheme.colorScheme.primary
//                            else MaterialTheme.colorScheme.primary
//                        )
//                    }
//                    is Result.Error -> {
//                        Text(
//                            text = (doctorsResult as Result.Error).message,
//                            color = if (isDarkMode)
//                                MaterialTheme.colorScheme.error
//                            else Color.Red
//                        )
//                    }
//                    is Result.Success -> {
//                        val doctors = (doctorsResult as Result.Success<List<UserData>>).data
//                        LazyColumn {
//                            items(doctors) { doctor ->
//                                // Tambahkan parameter isDarkMode jika perlu
//                                DoctorItem(
//                                    context = context,
//                                    doctor = doctor,
//                                    isDarkMode = isDarkMode
//                                )
//                            }
//                        }
//                    }
//                    else -> { /* Idle State, Nothing to display */ }
//                }
//            }
//        }
//    }
}


@Composable
fun DoctorItem(context: Context, doctor: UserData, isDarkMode: Boolean) {
    Card(
        modifier = Modifier
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
                // Doctor Photo with circular border
                AsyncImage(
                    model = doctor.photoUrl ?: R.drawable.ic_account,
                    contentDescription = "Doctor's Photo",
                    modifier = Modifier
                        .size(80.dp)
                        .clip(CircleShape)
                        .background(if (isDarkMode) Color.Gray.copy(alpha = 0.2f) else White.copy(alpha = 0.5f))
                        .border(2.dp, Blue, CircleShape),
                    contentScale = ContentScale.Crop
                )

                Spacer(modifier = Modifier.width(16.dp))

                Column(
                    verticalArrangement = Arrangement.spacedBy(4.dp),
                    modifier = Modifier.weight(1f)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Text(
                            text = "dr. ${doctor.firstName} ${doctor.lastName}",
                            style = Typography.titleLarge,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (isDarkMode) White else Blue
                        )
                        
                        // Verification Checkmark
                        if (doctor.doctor?.isVerified == true) {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_doc_check),
                                contentDescription = "Verified",
                                tint = Color(0xFF16A34A),
                                modifier = Modifier.size(18.dp)
                            )
                        } else {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_un_checked),
                                contentDescription = "Not Verified",
                                tint = Color(0xFFDC2626),
                                modifier = Modifier.size(18.dp)
                            )
                        }
                    }

                    doctor.doctor?.specialization?.let {
                        Text(
                            text = it,
                            style = Typography.bodyMedium,
                            fontWeight = FontWeight.Medium,
                            color = if (isDarkMode) Color.LightGray else Color.DarkGray
                        )
                    }

                    doctor.doctor?.workplace?.let {
                        Text(
                            text = "Workplace: $it",
                            style = Typography.bodySmall,
                            color = if (isDarkMode) Color.LightGray else Color.Gray
                        )
                    }

                    Text(
                        text = "Address: ${doctor.address ?: "-"}",
                        style = Typography.bodySmall,
                        color = if (isDarkMode) Color.LightGray else Color.Gray,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            WhatsAppButton(
                context = context,
                doctor = doctor,
                modifier = Modifier.fillMaxWidth(),
                isDarkMode = isDarkMode
            )
        }
    }
}

@Composable
fun WhatsAppButton(
    context: Context,
    doctor: UserData,
    modifier: Modifier,
    isDarkMode: Boolean
) {
    val buttonColor = if (isDarkMode) Color(0xFF2E7D32) else Color(0xFF40C351)
    Button(
        onClick = {
            if (doctor.doctor?.phoneNumber.isNullOrBlank()) {
                Toast.makeText(context, "No phone number available", Toast.LENGTH_SHORT).show()
            } else {
                openWhatsApp(context, doctor.doctor?.phoneNumber!!)
            }
        },
        modifier = modifier,
        colors = androidx.compose.material3.ButtonDefaults.buttonColors(
            containerColor = buttonColor,
            contentColor = White
        ),
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Start Consultation",
                style = Typography.labelLarge,
                fontWeight = FontWeight.Bold,
                color = White
            )
            Spacer(modifier = Modifier.size(8.dp))
            val rawRes = if (isDarkMode) R.raw.icons_whatsapp_blck else R.raw.icons_whatsapp
            val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(rawRes))
            LottieAnimation(
                composition = composition,
                iterations = LottieConstants.IterateForever,
                modifier = Modifier.size(24.dp)
            )
        }
    }
}
//data class Doctor(val name: String, val phoneNumber: String)

fun openWhatsApp(context: Context, noHp: String) {
    val waUrl = "https://wa.me/$noHp"
    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(waUrl))
    context.startActivity(intent)  // Gunakan context.startActivity() untuk memulai intent
}