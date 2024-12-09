package com.bangkit.dermascan.ui.main.profile.result

import android.content.Context
import android.content.Intent
import android.graphics.drawable.Icon
import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil3.compose.AsyncImage
import com.bangkit.dermascan.R
import com.bangkit.dermascan.data.model.response.UserData
import com.bangkit.dermascan.ui.theme.Black
import com.bangkit.dermascan.ui.theme.Blue
import com.bangkit.dermascan.ui.theme.LightBlue
import com.bangkit.dermascan.ui.theme.Typography
import com.bangkit.dermascan.util.Result
import com.bangkit.dermascan.util.formatDate

@Composable
fun ConsultationsScreen() {
    val context = LocalContext.current
    val viewModel: ConsulViewModel = hiltViewModel()
    val doctorsResult by viewModel.doctors.observeAsState(Result.Idle)

    // Mengambil data dokter saat composable pertama kali dipanggil
    LaunchedEffect(Unit) {
        viewModel.getDoctors()
    }

    Column(modifier = Modifier.fillMaxSize().padding(top = 42.dp, start = 16.dp, end = 16.dp    )) {
//        Text(
//            text = "Consultations",
//            style = MaterialTheme.typography.,
//            modifier = Modifier.padding(top = 16.dp)
//        )

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
}

@Composable
fun DoctorItem(context: Context, doctor: UserData) {


    Card(
        modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
        shape = RoundedCornerShape(8.dp),
        colors = androidx.compose.material3.CardDefaults.cardColors(
            containerColor = LightBlue,
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
            Column(modifier = Modifier.padding(16.dp)) {

                Spacer(modifier = Modifier.height(8.dp))

                // Menampilkan nama dokter
                Text(
                    text = "${doctor.firstName} ${doctor.lastName}",
                    style = Typography.titleMedium
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Menampilkan spesialisasi dan tempat kerja
                doctor.doctor?.specialization?.let {
                    Text(text = "Specialization: $it", style = Typography.bodyMedium)
                }

                Spacer(modifier = Modifier.height(8.dp))
                doctor.doctor?.workplace?.let {
                    Text(text = "Workplace: $it", style = Typography.bodyMedium)
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Menampilkan tanggal lahir (dob) jika ada
                Text(text = "Date of Birth:${doctor.dob?.let { formatDate(it) }}", style = Typography.bodyMedium)

                Spacer(modifier = Modifier.height(8.dp))
                // Menampilkan alamat jika ada
                doctor.address?.let {
                    Text(text = "Address: $it", style = Typography.bodyMedium)
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

        // Tombol untuk menghubungi lewat WhatsApp
        Button(
            onClick = {
                if (doctor.doctor?.phoneNumber.isNullOrBlank()) {
                    Toast.makeText(context, "No phone number available", Toast.LENGTH_SHORT).show()
                    return@Button
                }else{
                    openWhatsApp(context, doctor.doctor?.phoneNumber!!)
                }
//                openWhatsApp(context, "6287840012446")
//                doctor.doctor?.phoneNumber?.let {  }
                      },
            modifier = Modifier.wrapContentSize().align(Alignment.CenterHorizontally).padding(bottom = 12.dp),
            colors = androidx.compose.material3.ButtonDefaults.buttonColors(
                containerColor = Blue,
                contentColor = LightBlue
            )
        ) {
//                Text(text = "Chat with Dr. ${doctor.firstName} ${doctor.lastName}")
            Text(text = "Chat with Dr. ${doctor.doctor?.phoneNumber}"
                , style = Typography.bodyMedium,
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