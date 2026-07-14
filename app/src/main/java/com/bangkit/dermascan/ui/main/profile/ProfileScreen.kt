package com.bangkit.dermascan.ui.main.profile

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.unit.sp
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale

import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil3.compose.AsyncImage
import com.bangkit.dermascan.data.pref.UserModel
import com.bangkit.dermascan.ui.authentication.AuthViewModel
import com.bangkit.dermascan.ui.theme.Typography
import com.bangkit.dermascan.ui.main.profile.settings.SettingsViewModel
import com.bangkit.dermascan.util.Result

import androidx.compose.foundation.border
import androidx.compose.material3.CardDefaults
import com.bangkit.dermascan.ui.theme.*

@Composable
fun ProfileScreen(navController: NavController) {
    val context = LocalContext.current
    val viewModel: AuthViewModel = hiltViewModel()
    val roles by viewModel.roles.observeAsState("PATIENT")
    val settingsViewModel: SettingsViewModel = hiltViewModel()
    val isDark by settingsViewModel.isDarkTheme.collectAsState(initial = false)

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = MaterialTheme.colorScheme.background
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(vertical = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Profile Header
            item {
                ProfileHeader(viewModel, isDark)
                Spacer(modifier = Modifier.height(32.dp))
            }

            // Menu Items Container
            item {
                androidx.compose.material3.Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = if (isDark) CardBgDark else White
                    ),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                    border = androidx.compose.foundation.BorderStroke(1.dp, if (isDark) Color.DarkGray else BorderGray)
                ) {
                    Column(
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                    ) {
                        ProfileMenuItem(
                            icon = Icons.Default.Person,
                            title = "Edit Profile",
                            onClick = {
                                navController.navigate("editProfile")
                            }
                        )
                        
                        if (roles != "DOCTOR") {
                            ProfileMenuItem(
                                icon = Icons.Default.History,
                                title = "Skin Lesion History",
                                onClick = {
                                    navController.navigate("skinLesionHistory")
                                }
                            )
                        }

                        ProfileMenuItem(
                            icon = Icons.Default.Settings,
                            title = "Settings",
                            onClick = {
                                navController.navigate("settings")
                            }
                        )

                        ProfileMenuItem(
                            icon = Icons.AutoMirrored.Filled.Logout,
                            title = "Logout",
                            onClick = {
                                viewModel.signOut(context)
                                navController.navigate("onBoard") {
                                    popUpTo("main") { inclusive = true }
                                }
                            },
                            isLogout = true
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun ProfileHeader(viewModel: AuthViewModel, isDark: Boolean = false) {
    var errorMessage by remember { mutableStateOf<String?>(null) }
    var isLoading by remember { mutableStateOf(true) }
    var userData by remember { mutableStateOf<UserModel?>(null) }
    var imgUrl by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(Unit) {
        viewModel.fetchUserDetail { result ->
            when (result) {
                is Result.Success -> {
                     userData= UserModel(
                         uid = result.data.uid ?: "",
                         firstName = result.data.firstName ?: "",
                         lastName = result.data.lastName ?: "",
                         role = result.data.role ?: "",
                         dob = result.data.dob ?: "",
                         point = result.data.points ?: 0,
                         profileImageUrl = result.data.photoUrl,
                         address = result.data.address ?: "",
                         email = result.data.email ?: "",
                         specialization = result.data.doctor?.specialization ?: "Not Doctor",
                         workplace = result.data.doctor?.workplace ?: "Not Doctor"
                    )
                    imgUrl = result.data.photoUrl
                    Log.e("FetchUserDetail", "Success user details: $result")
                }
                is Result.Error -> {
                    Log.e("FetchUserDetail", "Error fetching user details: ${result.message}")
                }
                Result.Idle -> {}
                Result.Loading -> {}
            }
        }
    }
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxWidth()
    ) {
        var showFullImage by remember { mutableStateOf(false) }
        val userSession by viewModel.getSession().observeAsState()
        val profileImageUrl: String? = userSession?.profileImageUrl

        if (!profileImageUrl.isNullOrBlank()) {
            AsyncImage(
                model = profileImageUrl,
                contentDescription = "Profile Picture",
                modifier = Modifier
                    .size(110.dp)
                    .clip(CircleShape)
                    .border(3.dp, Blue, CircleShape)
                    .clickable { showFullImage = true },
                contentScale = ContentScale.Crop
            )
        } else {
            Icon(
                imageVector = Icons.Default.AccountCircle,
                contentDescription = "Profile Picture",
                modifier = Modifier
                    .size(110.dp)
                    .border(3.dp, Blue, CircleShape),
                tint = Color.Gray
            )
        }
        if (showFullImage) {
            Dialog(onDismissRequest = { showFullImage = false }) {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .width(300.dp)
                        .height(300.dp)
                        .clickable { showFullImage = false }
                ) {
                    AsyncImage(
                        model = profileImageUrl,
                        contentDescription = "Full Profile Picture",
                        modifier = Modifier
                            .fillMaxWidth()
                            .aspectRatio(1f),
                        contentScale = ContentScale.Fit
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        if (userSession?.firstName != null && userSession?.lastName != null) {
            Text(
                text = " ${userSession!!.firstName} ${userSession!!.lastName}",
                style = Typography.titleLarge,
                fontSize = 24.sp,
                color = if (isDark) White else Black,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(4.dp))
            userSession!!.email?.let {
                Text(
                    text = it,
                    style = Typography.bodyMedium,
                    color = if (isDark) White.copy(alpha = 0.6f) else Black.copy(alpha = 0.6f)
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            Surface(
                color = Blue.copy(alpha = 0.1f),
                contentColor = Blue,
                shape = RoundedCornerShape(24.dp),
                modifier = Modifier.padding(top = 4.dp)
            ) {
                Text(
                    text = "Point: ${userData?.point ?: 0}",
                    style = Typography.bodyMedium,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp)
                )
            }
        }
    }
}

@Composable
fun ProfileMenuItem(
    icon: ImageVector,
    title: String,
    onClick: () -> Unit,
    isLogout: Boolean = false
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = title,
            modifier = Modifier.size(24.dp),
            tint = if (isLogout) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.onSurface
        )
        Spacer(modifier = Modifier.width(16.dp))
        Text(
            text = title,
            style = Typography.bodyMedium,
            color = if (isLogout) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.weight(1f)
        )
        Icon(
            imageVector = Icons.Default.ChevronRight,
            contentDescription = "Navigate",
            modifier = Modifier.size(24.dp),
            tint = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
    Divider(color = MaterialTheme.colorScheme.surfaceVariant)
}
@Preview(showBackground = true)
@Composable
fun ProfileScreenPreview(){
    ProfileScreen(navController = NavController(LocalContext.current))

}