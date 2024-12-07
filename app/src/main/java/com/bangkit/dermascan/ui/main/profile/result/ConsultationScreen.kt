package com.bangkit.dermascan.ui.main.profile.result

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat.startActivity
import androidx.hilt.navigation.compose.hiltViewModel
import com.bangkit.dermascan.data.model.response.UserData
import com.bangkit.dermascan.ui.theme.Typography
import com.bangkit.dermascan.util.Result

@Composable
fun ConsultationsScreen() {
    val context = LocalContext.current
    val viewModel: ConsulViewModel = hiltViewModel()
    val doctorsResult by viewModel.doctors.observeAsState(Result.Idle)

    // Mengambil data dokter saat composable pertama kali dipanggil
    LaunchedEffect(Unit) {
        viewModel.getDoctors()
    }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text(
            text = "Consultations",
            style = MaterialTheme.typography.labelLarge,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        when (doctorsResult) {
            is Result.Loading -> {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
            }
            is Result.Error -> {
                Text(text = (doctorsResult as Result.Error).message, color = Color.Red)
            }
            is Result.Success -> {
                val doctors = (doctorsResult as Result.Success<List<UserData>>).data
                LazyColumn {
                    items(doctors) { doctor ->
                        DoctorItem(context = context, doctor = doctor)
                    }
                }
            }
            else -> { /* Idle State, Nothing to display */ }
        }
    }
//    // Simulasi data dokter
//    val doctors = listOf(
//        Doctor("Dr. John Doe", "+6281271624137"),
//        Doctor("Dr. Jane Smith", "+0987654321"),
//        Doctor("Dr. Richard Roe", "+1122334455")
//    )
//
//    // Tampilan daftar dokter
//    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
//        Text(
//            text = "Consultations",
//            style = MaterialTheme.typography.bodyMedium,
//            modifier = Modifier.padding(bottom = 16.dp)
//        )
//
//        LazyColumn {
//            items(doctors) { doctor ->
//                DoctorItem(context = context, doctor = doctor)
//            }
//        }
//    }
}

@Composable
fun DoctorItem(context: Context, doctor: UserData) {
    Card(
        modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
//        elevation = 4.dp,
        shape = RoundedCornerShape(8.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = doctor.firstName!! + " " + doctor.lastName!!,
                style = Typography.bodyMedium
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Tombol untuk menghubungi lewat WhatsApp
            Button(
                onClick = { doctor.doctor?.let { openWhatsApp(context, it.whatsappUrl) } },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = "Chat with DR.${doctor.firstName + doctor.lastName}")
            }
        }
    }
}

//data class Doctor(val name: String, val phoneNumber: String)

fun openWhatsApp(context : Context, phoneNumber: String) {
    val url = "https://wa.me/$phoneNumber"
    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
    startActivity(context, intent, null)
}