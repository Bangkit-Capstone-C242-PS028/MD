
package com.bangkit.dermascan.ui.main

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Error
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import com.bangkit.dermascan.R
import com.bangkit.dermascan.ui.main.chat.ChatScreen
//import com.bangkit.dermascan.ui.main.feeds.FeedsScreen
import com.bangkit.dermascan.ui.main.home.HomeScreen
import com.bangkit.dermascan.ui.main.profile.ProfileScreen
import com.bangkit.dermascan.ui.main.scan.ScanScreen
//import com.bangkit.dermascan.ui.settings.SettingsScreen
import com.bangkit.dermascan.ui.theme.DermaScanTheme
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.bangkit.dermascan.ui.article.ArticleActivity
import com.bangkit.dermascan.ui.authentication.AuthViewModel
import com.bangkit.dermascan.ui.forum.ForumActivity
import com.bangkit.dermascan.ui.main.feeds.FeedsScreen
import com.bangkit.dermascan.ui.main.forum.ForumScreen

import com.bangkit.dermascan.ui.navigation.AppNavHost
//import com.bangkit.dermascan.ui.scan.PhotoActivity
import com.bangkit.dermascan.ui.theme.*
import com.bangkit.dermascan.ui.main.scan.upload.SharedViewModel
import com.google.firebase.messaging.FirebaseMessaging
import dagger.hilt.android.AndroidEntryPoint
import com.bangkit.dermascan.util.Result
import com.google.firebase.auth.FirebaseAuth

//import androidx.compose.ui.Alignment
@AndroidEntryPoint
class MainActivity : ComponentActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setTheme(R.style.Theme_DermaScan)
        enableEdgeToEdge()
        FirebaseMessaging.getInstance().subscribeToTopic("articles")
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.d("FCM", "Successfully subscribed to topic: articles")
                } else {
                    Log.e("FCM", "Failed to subscribe to topic", task.exception)
                }
            }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(
                    this,
                    android.Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(android.Manifest.permission.POST_NOTIFICATIONS),
                    NOTIFICATION_PERMISSION_REQUEST_CODE
                )
            }
        }

        setContent {
            DermaScanTheme {
                val navController = rememberNavController()
                AppNavHost(navController = navController)

            }
        }
    }

    companion object {
        const val NOTIFICATION_PERMISSION_REQUEST_CODE = 1
    }
}

// DataArticles class untuk item navigasi
data class BottomNavItem(
    val title: String,
    val icon: Any // Mendukung baik ImageVector maupun drawable resource ID
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen( navController: NavHostController) {

    val authViewModel: AuthViewModel = hiltViewModel()
    val roles by authViewModel.roles.observeAsState("PATIENT")
    val result = authViewModel.isVerified.observeAsState(Result.Idle)
    val context = LocalContext.current
    // Panggil ViewModel untuk memuat status verifikasi saat pertama kali memuat


    if(roles == "DOCTOR"){
        LaunchedEffect(Unit) {
            authViewModel.getDoctorVerificationStatus()
        }
        // Tangani status verifikasi
        when (val status = result.value) {
            is Result.Idle -> {
                // Idle state, bisa menunjukkan loading atau lainnya
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
            }
            is Result.Loading -> {
                // Menampilkan indikator loading
            }
            is Result.Success -> {
                // Ambil nilai Boolean dari status.data
                val isVerified = status.data
                Log.d("DoctorVerification", "Is Verified: $isVerified")

                LaunchedEffect(isVerified) {
                    // Pastikan navigasi hanya terjadi sekali
                    if (!isVerified) {
                        navController.navigate("block") {
                            // Hapus halaman sebelumnya agar pengguna tidak bisa kembali ke halaman verifikasi
                            popUpTo("main") { inclusive = true }
                        }
                    }
                }
            }
            is Result.Error -> {
                // Tangani error jika terjadi
                Toast.makeText(LocalContext.current, status.message, Toast.LENGTH_SHORT).show()
            }
        }


    }

    val items = mutableListOf(

        BottomNavItem("Home", R.drawable.ic_home),
        BottomNavItem("Forum", R.drawable.ic_feeds),
        BottomNavItem("Chat Bot", R.drawable.ic_chatt),
        BottomNavItem("Profile", R.drawable.ic_account)
    )

    if (roles != "DOCTOR") {
//        items.add(0, BottomNavItem("Home", R.drawable.ic_home))
        items.add(2, BottomNavItem("Scan", R.drawable.ic_camera))
    }

    var selectedItem by rememberSaveable { mutableIntStateOf(0) }
//    val navController = rememberNavController()
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = {
                    Text(text = items[selectedItem].title)
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Blue,
                    titleContentColor = LightBlue,
                    navigationIconContentColor = MaterialTheme.colorScheme.onPrimary
                ),
                actions = {
                    if(items[selectedItem].title == "Home"){
                        FloatingActionButton(
                            onClick = {
                                // Tindakan ketika FAB ditekan
//                                Toast.makeText(context, "FAB Clicked", Toast.LENGTH_SHORT).show()
                                navController.navigate("favoriteArticle")
                            },
                            containerColor = LightBlue,
                            contentColor = Blue,
                            shape = CircleShape,
                            modifier = Modifier
                                .size(40.dp) // Ukuran FAB lebih kecil agar sesuai di TopAppBar
                                .offset(x = (-8).dp) // Jarak dari tepi kanan
                        ) {
                            Icon(
                                imageVector = Icons.Default.Favorite,
                                contentDescription = "Add Icon"
                            )
                        }
                    }
                }

            )
        }

        ,
        bottomBar = {

                // Tampilkan cekungan jika roles bukan "DOCTOR"
                Box(
                    Modifier
                        .fillMaxWidth()
                ) {
                    if (roles != "DOCTOR") {
                        Canvas(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(100.dp) // Tinggi disesuaikan agar cukup untuk lengkungan
                        ) {
                            val width = size.width
                            val height = size.height
                            val cornerRadius = 24.dp.toPx() // Radius untuk lengkungan di kiri dan kanan atas
                            val concaveDepth = 78.dp.toPx() // Kedalaman cekungan

                            drawPath(
                                path = Path().apply {
                                    moveTo(0f, height) // Mulai dari kiri bawah
                                    lineTo(0f, cornerRadius) // Garis vertikal ke atas
                                    quadraticTo(0f, 0f, cornerRadius, 0f) // Lengkungan di kiri atas
                                    lineTo(width * 0.4f, 0f) // Garis ke awal cekungan tengah
                                    quadraticTo(
                                        width * 0.5f, concaveDepth, // Titik kontrol di bawah (kedalaman cekungan)
                                        width * 0.6f, 0f // Akhir cekungan di kanan
                                    )
                                    lineTo(width - cornerRadius, 0f) // Garis horizontal ke kanan
                                    quadraticTo(width, 0f, width, cornerRadius) // Lengkungan di kanan atas
                                    lineTo(width, height) // Garis vertikal ke bawah
                                    close()
                                },
                                color = Blue // Warna NavigationBar
                            )
                        }

                        NavigationBar(
                            containerColor = Color.Transparent, // Set warna transparan karena sudah menggunakan Canvas
                            modifier = Modifier.align(Alignment.BottomCenter)
                        ) {
                            items.forEachIndexed { index, item ->
                                if (index == 2) { // Index untuk FAB (Scan)
                                    Spacer(modifier = Modifier.weight(0.5f))
                                } else {
                                    NavigationBarItem(
                                        icon = {
                                            val offsetY = if (selectedItem == index) (-12).dp else 0.dp // Naikkan ikon jika dipilih
                                            when (item.icon) {
                                                is ImageVector -> Icon(
                                                    item.icon,
                                                    contentDescription = item.title,
                                                    modifier = Modifier.offset(y = offsetY) // Menambahkan offset ke ikon
                                                )
                                                is Int -> Icon(
                                                    painterResource(id = item.icon),
                                                    contentDescription = item.title,
                                                    modifier = Modifier.offset(y = offsetY) // Menambahkan offset ke ikon
                                                )
                                                else -> throw IllegalArgumentException("Icon type not supported")
                                            }
                                        },
                                        label = {
                                            Text(item.title, modifier = Modifier.offset(y = (0).dp))
                                        },
                                        colors = NavigationBarItemDefaults.colors(
                                            selectedIconColor = LightBlue,
                                            unselectedIconColor = LightBlue,
                                            selectedTextColor = White,
                                            unselectedTextColor = LightBlue,
                                            indicatorColor = Blue
                                        ),
                                        selected = selectedItem == index,
                                        onClick = { selectedItem = index },
                                        interactionSource = MutableInteractionSource()
                                    )
                                }
                            }
                        }

                    } else {
                        // Jika roles adalah "DOCTOR", tidak tampilkan cekungan
                        NavigationBar(
                            containerColor = Blue,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            items.forEachIndexed { index, item ->
                                NavigationBarItem(
                                    icon = {
                                        val offsetY = if (selectedItem == index) (-12).dp else 0.dp // Naikkan ikon jika dipilih
                                        when (item.icon) {
                                            is ImageVector -> Icon(
                                                item.icon,
                                                contentDescription = item.title,
                                                modifier = Modifier.offset(y = offsetY)
                                            )
                                            is Int -> Icon(
                                                painterResource(id = item.icon),
                                                contentDescription = item.title,
                                                modifier = Modifier.offset(y = offsetY)
                                            )
                                            else -> throw IllegalArgumentException("Icon type not supported")
                                        }
                                    },
                                    label = {
                                        Text(item.title, modifier = Modifier.offset(y = (0).dp))
                                    },
                                    colors = NavigationBarItemDefaults.colors(
                                        selectedIconColor = LightBlue,
                                        unselectedIconColor = LightBlue,
                                        selectedTextColor = White,
                                        unselectedTextColor = LightBlue,
                                        indicatorColor = Blue
                                    ),
                                    selected = selectedItem == index,
                                    onClick = { selectedItem = index },
                                    interactionSource = MutableInteractionSource()
                                )
                            }
                        }
                    }
                    // FloatingActionButton tetap akan muncul jika roles bukan "DOCTOR"
                    if (roles != "DOCTOR") {
                        FloatingActionButton(
                            onClick = {
                                navController.navigate("scan_full")
                            },
                            modifier = Modifier
                        .align(Alignment.TopCenter)
                                .offset(y = (-34).dp)
                                .padding(10.dp),
                            containerColor = Blue,
                            shape = CircleShape,
                            elevation = FloatingActionButtonDefaults.elevation(0.dp)
                        ) {
                            Icon(
                                painterResource(id = R.drawable.ic_camera),
                                contentDescription = "Scan",
                                Modifier.size(40.dp),
                                tint = LightBlue
                            )
                        }
                    }
                }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            if (roles != "DOCTOR") {
                when (selectedItem) {
                    0 -> HomeScreen(navController, roles)
//                    1 -> {
//                        // Navigasi ke ArticleActivity menggunakan Context
//                        val context = LocalContext.current
//                        LaunchedEffect(Unit) {
//                            val intent = Intent(context, ArticleActivity::class.java)
//                            context.startActivity(intent)
//                        }
//                    }
                    1 -> ForumScreen(roles = roles)
//                    1 -> {
//                        // Navigasi ke ArticleActivity menggunakan Context
//                        val context = LocalContext.current
//                        LaunchedEffect(Unit) {
//                            val intent = Intent(context, ForumActivity::class.java)
//                            context.startActivity(intent)
//                        }
//                    }
                    2 -> ScanScreen(
                        viewModel = SharedViewModel(),
                        onBackClick = { navController.navigateUp() },
                        onUploadClick = { }
                    )
                    3 -> ChatScreen()
                    4 -> ProfileScreen(navController)
                }
            }else{
                when (selectedItem) {
                    0 -> HomeScreen(navController,roles)
//                    0 -> FeedsScreen(navController, roles)
//                    0 -> {
//                        // Navigasi ke ArticleActivity menggunakan Context
//                        val context = LocalContext.current
//                        LaunchedEffect(Unit) {
//                            val intent = Intent(context, ForumActivity::class.java)
//                            context.startActivity(intent)
//                        }
//                    }
                    1 -> ForumScreen(roles = roles)
                    2 -> ChatScreen()
                    3 -> ProfileScreen(navController)
                }
            }

        }
    }

}

@Composable
fun BlockedAccessScreen() {
    val context = LocalContext.current

    // Tampilan untuk halaman yang membatasi akses
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // Gambar atau ikon
        Icon(
            imageVector = Icons.Default.Error, // Ikon error atau ikon yang sesuai
            contentDescription = "Blocked",
            modifier = Modifier.size(80.dp),
            tint = Color.Red
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Pesan Informasi
        Text(
            "Your account is not verified. You cannot access other features. Please wait, admin will verify your account. Close your app and reopen again for update status.",
            style = MaterialTheme.typography.titleLarge,
            textAlign = TextAlign.Center,
            color = Blue
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Pesan tambahan jika diinginkan
        Text(
            "If you have any questions, please contact support.",
            style = MaterialTheme.typography.bodyMedium,
            textAlign = TextAlign.Center,
            color = Blue.copy(alpha = 0.7f)
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Tombol untuk menghubungi support (jika ada)
        Button(
            onClick = { sendEmailToSupport(context  )},
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(
                containerColor = Blue,
                contentColor = LightBlue
            )
        ) {
            Text("Contact Support")
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Indikator loading jika diperlukan
//        CircularProgressIndicator(modifier = Modifier.padding(8.dp))
    }
}

fun sendEmailToSupport(context: Context) {
    val emailIntent = Intent(Intent.ACTION_SENDTO).apply {
        data = Uri.parse("mailto:khairunsyah8935@gmail.com") // Ganti dengan alamat email tujuan
        putExtra(Intent.EXTRA_SUBJECT, "Account Verification Issue") // Subjek email
        putExtra(Intent.EXTRA_TEXT, "Dear Support,\n\nI am facing an issue with account verification.\n\nRegards,") // Body email
    }

    // Cek apakah ada aplikasi email yang dapat menangani intent ini
    try {
        context.startActivity(Intent.createChooser(emailIntent, "Send Email"))
    } catch (e: Exception) {
        // Tangani error jika tidak ada aplikasi email yang tersedia
        Toast.makeText(context, "No email client found", Toast.LENGTH_SHORT).show()
    }
}

@Preview(showBackground = true)
@Composable
fun MainScreenPreview() {
    DermaScanTheme {
//        MainScreen(navController = rememberNavController())
    }

}




