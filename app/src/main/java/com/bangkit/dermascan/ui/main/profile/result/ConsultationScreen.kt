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
        modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isDarkMode) LightBlue else Blue,
            contentColor = if (isDarkMode) Black else White
        )
    ) {
        Row(Modifier.padding(bottom = 4.dp, top = 16.dp)
            .align(Alignment.CenterHorizontally),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // Menampilkan foto dokter jika tersedia
            AsyncImage(
                model = doctor.photoUrl ?: R.drawable.ic_account,
//                model = "https://storage.googleapis.com/dermascan-cloud-storage/articles%2FzSyL1AsuhkVejKQG6UjsfdcLFYr2%2FyT_X30U21f0HE1jMO9uPJ",
                contentDescription = "Doctor's Photo",
                modifier = Modifier
                    .size(100.dp)
                    .clip(CircleShape)
                    .background(Black)
            )
            Spacer(modifier = Modifier.width(30.dp))
            Column(modifier = Modifier.padding(bottom = 10.dp, top = 10.dp)
                .background(
                    shape = RoundedCornerShape(8.dp),
                    color = if (isDarkMode) Color.Gray else Color.DarkGray
                ).padding(16.dp)
            ) {

//                Spacer(modifier = Modifier.height(8.dp))

                // Menampilkan nama dokter
                Text(
                    text = "dr. ${doctor.firstName} ${doctor.lastName}",
                    style = Typography.titleLarge,
                    fontSize = 18.sp,
                    color = if (isDarkMode) Black else White
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Menampilkan spesialisasi dan tempat kerja
                doctor.doctor?.specialization?.let {
                    Text(text = "Specialization: $it", style = Typography.bodyMedium,
                        color = if (isDarkMode) Black else White
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))
                doctor.doctor?.workplace?.let {
                    Text(text = "Workplace: $it", style = Typography.bodyMedium,
                        color = if (isDarkMode) Black else White
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Menampilkan tanggal lahir (dob) jika ada
                Text(text = "Date of Birth: ${doctor.dob?.let { formatDate(it) }}", style = Typography.bodyMedium,
                    color = if (isDarkMode) Black else White
                )

                Spacer(modifier = Modifier.height(8.dp))
                // Menampilkan alamat jika ada
                doctor.address?.let {
                    Text(text = "Address: $it", style = Typography.bodyMedium,
                        color = if (isDarkMode) Black else White
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center,
                    modifier =
                    Modifier.align(
                        Alignment.CenterHorizontally
                    )
                ) {
                    // Menampilkan status verifikasi jika sudah diverifikasi
                    if (doctor.doctor?.isVerified == true) {
                        Text(text = "Verified", color = Color.Green, style = Typography.bodyLarge)
                        Icon(
                            painter = painterResource(id = R.drawable.ic_doc_check),
                            contentDescription = "Verified",
                            tint = Color.Green,
                            modifier = Modifier.size(40.dp)
                        )
                    }else{
                        Text(text = "Not Verified", color = Color.Red, style = Typography.bodyLarge)
                        Icon(
                            painter = painterResource(id = R.drawable.ic_un_checked),
                            contentDescription = "Verified",
                            tint = Color.Red,
                            modifier = Modifier.size(40.dp)

                        )
                    }
                }
            }
        }
        WhatsAppButton(context,
            doctor,
            Modifier
            .wrapContentSize()
            .align(Alignment.CenterHorizontally)
            .padding(bottom = 12.dp),
            isDarkMode)
        // Tombol untuk menghubungi lewat WhatsApp
//        Button(
//            onClick = {
//                if (doctor.doctor?.phoneNumber.isNullOrBlank()) {
//                    Toast.makeText(context, "No phone number available", Toast.LENGTH_SHORT).show()
//                    return@Button
//                }else{
//                    openWhatsApp(context, doctor.doctor?.phoneNumber!!)
//                }
////                openWhatsApp(context, "6287840012446")
////                doctor.doctor?.phoneNumber?.let {  }
//                      },
//            modifier = Modifier.wrapContentSize().align(Alignment.CenterHorizontally).padding(bottom = 12.dp),
//            colors = androidx.compose.material3.ButtonDefaults.buttonColors(
//                containerColor = Blue,
//                contentColor = LightBlue
//            )
//        ) {
////                Text(text = "Chat with Dr. ${doctor.firstName} ${doctor.lastName}")
//            Text(text = "Start Chat with Dr.${doctor.firstName + " " + doctor.lastName}"
//                , style = Typography.bodyMedium,
//            )
//        }

    }
}

@Composable
fun WhatsAppButton(
    context: Context,
    doctor: UserData,
    modifier: Modifier,
    isDarkMode: Boolean
) {
    if (!isDarkMode) {
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
                containerColor = GreenWA,
                contentColor = LightBlue
            )
        ) {
            Row(verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier.align(alignment = Alignment.CenterVertically)) {
                Text(
                    text = "Start Consultation",
                    style = Typography.bodyMedium,
                    color = White
                )
                Spacer(modifier = Modifier.size(8.dp))
                val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.icons_whatsapp))
                LottieAnimation(
                    composition = composition,
                    iterations = LottieConstants.IterateForever,
                    modifier = Modifier
                        .size(30.dp)
                        .align(Alignment.CenterVertically)
//                    .align(Alignment.) // Posisi animasi di tengah
                )


            }
        }
    }else{
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
                containerColor = Color.Gray,
                contentColor = LightBlue
            )
        ) {
            Row(verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier.align(alignment = Alignment.CenterVertically)) {
                Text(
                    text = "Start Consultation",
                    style = Typography.bodyMedium,
                    color = White
                )
                Spacer(modifier = Modifier.size(8.dp))
                val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.icons_whatsapp_blck))
                LottieAnimation(
                    composition = composition,
                    iterations = LottieConstants.IterateForever,
                    modifier = Modifier
                        .size(30.dp)
                        .align(Alignment.CenterVertically)
//                    .align(Alignment.) // Posisi animasi di tengah
                )


            }
        }
    }

}
//data class Doctor(val name: String, val phoneNumber: String)

fun openWhatsApp(context: Context, noHp: String) {
    val waUrl = "https://wa.me/$noHp"
    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(waUrl))
    context.startActivity(intent)  // Gunakan context.startActivity() untuk memulai intent
}