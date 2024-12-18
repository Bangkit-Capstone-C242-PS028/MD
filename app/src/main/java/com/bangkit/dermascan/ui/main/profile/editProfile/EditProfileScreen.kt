package com.bangkit.dermascan.ui.main.profile.editProfile

import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil3.compose.AsyncImage
import com.bangkit.dermascan.data.pref.UserModel
import com.bangkit.dermascan.ui.authentication.AuthViewModel
import com.bangkit.dermascan.ui.authentication.register.CustomTextField
import com.bangkit.dermascan.ui.main.feeds.AddTopBar
import com.bangkit.dermascan.ui.main.profile.settings.SettingsViewModel
import com.bangkit.dermascan.ui.theme.Blue
import com.bangkit.dermascan.ui.theme.White
import com.bangkit.dermascan.util.uriToFile
import com.bangkit.dermascan.util.Result
import com.bangkit.dermascan.util.getMimeType
import com.bangkit.dermascan.util.reduceFileImage

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditProfileScreen(navController: NavController){
    val context = LocalContext.current
    val authViewModel: AuthViewModel = hiltViewModel()
    val editProfileViewModel : EditProfileViewModel = hiltViewModel()
    val scrollState = rememberScrollState()
    val currentUser by authViewModel.currentUser.observeAsState()
    val firstName = rememberSaveable { mutableStateOf("") }
    val lastName = rememberSaveable { mutableStateOf("") }
    val address = rememberSaveable { mutableStateOf("") }
    val imageUri = rememberSaveable{ mutableStateOf<Uri?>(null) }
    var compressedImageBytes by remember { mutableStateOf<ByteArray?>(null) }
    val settingsViewModel: SettingsViewModel = hiltViewModel()

    // Observe dark mode state
    val isDarkMode by settingsViewModel.isDarkTheme.collectAsState(initial = false)
    val updateResult by editProfileViewModel.updateUserResult.observeAsState()
    LaunchedEffect(currentUser) {
        firstName.value = currentUser?.firstName ?: ""
        lastName.value = currentUser?.lastName ?: ""
        address.value = currentUser?.address ?: ""
    }

    Surface(
        color = if (isDarkMode) MaterialTheme.colorScheme.background
        else MaterialTheme.colorScheme.surface,
        contentColor = if (isDarkMode) MaterialTheme.colorScheme.onBackground
        else MaterialTheme.colorScheme.onSurface
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            // TopAppBar
            AddTopBar("Edit Profile",navController)

            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    modifier = Modifier
                        .verticalScroll(scrollState)
                        .padding(24.dp)  // Menambahkan padding di sekeliling kolom
                        .fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    //ambil link dari viewmodel / userPref
                    val userSession by authViewModel.getSession().observeAsState()
                    val profileImageUrl: String? = userSession?.profileImageUrl  // Ambil URL gambar profil dari session
                    val launcher = rememberLauncherForActivityResult(
                        contract = ActivityResultContracts.GetContent()
                    ) { uri: Uri? ->
                        uri?.let {
                            imageUri.value = it
                        }
                    }
                    if (!profileImageUrl.isNullOrBlank()) {
                        val displayedImageUri = imageUri.value?.toString() ?: profileImageUrl
                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier.size(250.dp) // Ukuran gambar
                        ) {
                            // Gambar profil
                            AsyncImage(
                                model = displayedImageUri,
                                contentDescription = "Profile Picture",
                                modifier = Modifier
                                    .size(250.dp)
                                    .clip(CircleShape)
                                    .clickable {
                                        launcher.launch("image/*")
                                    },
                                contentScale = ContentScale.Crop
                            )

                            // Ikon pensil di kanan bawah
                            Icon(
                                imageVector = Icons.Default.Edit,
                                contentDescription = "Edit Profile Picture",
                                modifier = Modifier
                                    .align(Alignment.BottomEnd)
                                    .size(42.dp)
                                    .background(
                                        color = Color.White,
                                        shape = CircleShape
                                    )
                                    .clickable {
                                        launcher.launch("image/*")
                                    }
                                    .padding(4.dp),
                                tint = Color.Gray
                            )
                        }
                    } else {
                        // Jika URL gambar null atau kosong, tampilkan ikon default
                        Icon(
                            imageVector = Icons.Default.AccountCircle,
                            contentDescription = "Profile Picture",
                            modifier = Modifier.size(250.dp).clickable {
                                launcher.launch("image/*")
                            },
                            tint = Color.Gray
                        )
                    }
                    Spacer(modifier = Modifier.height(34.dp))

                    CustomTextField(value = firstName , label = "First Name")

                    Spacer(modifier = Modifier.height(16.dp))

                    CustomTextField(value = lastName, label = "Last Name")

                    Spacer(modifier = Modifier.height(16.dp))

                    CustomTextField(value = address, label = "Address")

                    Spacer(modifier = Modifier.height(32.dp))

                    when (val result = updateResult) {
                        is Result.Loading -> {
                            CircularProgressIndicator()
                        }
                        is Result.Success -> {
                            Toast.makeText(context, result.data.message, Toast.LENGTH_SHORT).show()
                            val updateData = UserModel(
                                firstName = result.data.data?.firstName,
                                lastName = result.data.data?.lastName,
                                address = result.data.data?.address,
                                profileImageUrl = result.data.data?.photoUrl
                            )
                            authViewModel.saveUpdateData(updateData)
//                        navController.navigate("main") {
//                            popUpTo("editProfile") { inclusive = true }
//                        }
                        }

                        Result.Idle -> {}
                        else -> {}
                    }
                    if(updateResult is Result.Error){
                        Text(text = "Error: ${(updateResult as Result.Error).message}")
                    }

                    var isButtonEnabled by remember { mutableStateOf(true) }
                    Button(
                        onClick = {
                            isButtonEnabled = false
                            val imageFile = imageUri.value?.let {
                                val mimeType = context.getMimeType(it)
                                if (mimeType?.startsWith("image/") == true) {
                                    uriToFile(it, context).reduceFileImage()
                                } else {
                                    Toast.makeText(context, "Please select a valid image file", Toast.LENGTH_SHORT).show()
                                    return@Button
                                }
                            }

                            Log.d("File Check", "File path: ${imageFile?.path}, Exists: ${imageFile?.exists()}")

                            // Handle update logic
                            editProfileViewModel.updateUser(
                                firstname = firstName.value,
                                lastname = lastName.value,
                                address = address.value,
                                imgFile = imageFile // Bisa null jika tidak ada gambar
                            )
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Blue,
                            contentColor = White
                        ),
                        modifier = Modifier
                            .width(207.dp)
                            .height(45.dp),
                        enabled = isButtonEnabled
                    ) {
                        Text("Update Profile", fontSize = 16.sp)
                    }
                }
            }
        }
    }
}
