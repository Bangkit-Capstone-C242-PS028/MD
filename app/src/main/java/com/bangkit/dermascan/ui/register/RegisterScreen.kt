package com.bangkit.dermascan.ui.register

import android.annotation.SuppressLint
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
<<<<<<< HEAD
import androidx.compose.foundation.layout.*
=======
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
>>>>>>> master
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
<<<<<<< HEAD
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
=======
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
>>>>>>> master
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
<<<<<<< HEAD
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.input.VisualTransformation
import com.bangkit.dermascan.ui.theme.*
import com.bangkit.dermascan.ui.theme.White
import java.text.SimpleDateFormat
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
=======
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
>>>>>>> master
import androidx.hilt.navigation.compose.hiltViewModel
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition
import com.bangkit.dermascan.R
import com.bangkit.dermascan.data.model.requestBody.AuthRequest
import com.bangkit.dermascan.data.model.requestBody.DoctorSignupRequest
import com.bangkit.dermascan.ui.login.AuthViewModel
<<<<<<< HEAD
import com.bangkit.dermascan.util.Result
import com.bangkit.dermascan.util.parseErrorMessage
import com.bangkit.dermascan.util.uriToFile
import java.io.File
import java.util.*
=======
import com.bangkit.dermascan.ui.theme.Black
import com.bangkit.dermascan.ui.theme.Blue
import com.bangkit.dermascan.ui.theme.DermaScanTheme
import com.bangkit.dermascan.ui.theme.Purple40
import com.bangkit.dermascan.ui.theme.TextInp
import com.bangkit.dermascan.ui.theme.Typography
import com.bangkit.dermascan.ui.theme.White
import com.bangkit.dermascan.ui.theme.docInp
import com.bangkit.dermascan.util.Result
import com.bangkit.dermascan.util.uriToFile
import java.text.SimpleDateFormat
import java.util.Locale
>>>>>>> master

//@OptIn(ExperimentalMaterial3Api::class)
//@Preview(showSystemUi = true)
@Composable
fun RegisterScreen(onRegisterSuccess: () -> Unit) {
    val context = LocalContext.current
    val registerViewModel: AuthViewModel = hiltViewModel()
    val scrollState = rememberScrollState()
    val firstName = rememberSaveable { mutableStateOf("") }
    val lastName = rememberSaveable { mutableStateOf("") }
    val email = rememberSaveable { mutableStateOf("") }
    val password = rememberSaveable { mutableStateOf("") }
    val confirmPassword = rememberSaveable { mutableStateOf("") }
    val mobileNumber = rememberSaveable { mutableStateOf("") }
    val address = rememberSaveable { mutableStateOf("") }
    val workPlace = rememberSaveable { mutableStateOf("") }
    val birthday = rememberSaveable { mutableStateOf("") }
    val waUrl = rememberSaveable { mutableStateOf("") }
    var passwordError by rememberSaveable { mutableStateOf("") }
    var emailError by rememberSaveable { mutableStateOf("") }
    var passwordVisibility by rememberSaveable { mutableStateOf(false) }
//    var specializations by remember { mutableStateOf("") }
    val specializations = rememberSaveable { mutableStateOf("") }
    val roles = listOf("", "PATIENT","DOCTOR")
    var selectedFileUri by rememberSaveable { mutableStateOf<Uri?>(null) }
//    var selectedRole by remember { mutableStateOf(roles[0]) }
//    var expanded by remember { mutableStateOf(false) }

    var expanded by rememberSaveable { mutableStateOf(false) }
    var selectedRole by rememberSaveable { mutableStateOf<String?>(null) }  // Inisialisasi null
    var roleError by rememberSaveable { mutableStateOf<String?>(null) }
    var selectedFileError by rememberSaveable { mutableStateOf<Uri?>(null) }
    val signupState by registerViewModel.signupState.collectAsState()
    val signupResult by registerViewModel.signupResult.observeAsState()

    // Handle signup state


    fun validateEmail(input: String) {
        email.value = input
        emailError = if (android.util.Patterns.EMAIL_ADDRESS.matcher(input).matches()) {
            ""
        } else if(input.isBlank()){
            ""
        } else {
            "Invalid email address"
        }
    }

    fun validatePassword(input: String) {
        password.value = input
        passwordError = if (input.length >= 8 && input.isNotEmpty()) {
            ""
        }else if(input.isBlank()){
            ""
        }else {
            "Password must be at least 8 characters"
        }
    }



    fun validateInputs(): Boolean {

        if (firstName.value.isBlank() || lastName.value.isBlank() || email.value.isBlank() ||
            password.value.isBlank() || confirmPassword.value.isBlank()) {
            Toast.makeText(context, "Please fill in all fields", Toast.LENGTH_SHORT).show()
            return false
        }
        if (password.value != confirmPassword.value) {
            Toast.makeText(context, "Passwords do not match", Toast.LENGTH_SHORT).show()
            return false
        }

        if (!isValidePassword(password.value)) {
            Toast.makeText(context, "Password must be at least 8 characters long and contain a mix of upper and lower case letters, numbers, and special characters", Toast.LENGTH_LONG).show()
            return false
        }

        if (roleError != null) {
            Toast.makeText(context, "Please select a role", Toast.LENGTH_SHORT).show()
            return false
        }
        return true
    }

    fun formatDateToISO(input: String): String? {
        return try {
            // Memastikan input tidak kosong
            if (input.isBlank()) return null

            val inputFormat = SimpleDateFormat("dd-MM-yyyy", Locale.US)  // Tentukan Locale
            val outputFormat = SimpleDateFormat("yyyy-MM-dd", Locale.US)
            val date = inputFormat.parse(input)
            date?.let {
                outputFormat.format(it)
            }  // Jika parsing gagal, kembalikan null
        } catch (e: Exception) {
            null  // Tanggal tidak valid atau format tidak sesuai
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp) // Padding di sekitar layar
    ) {
        // Teks "Hello!" di bagian atas
        Text(
            text = "Hello!",
            style = Typography.titleLarge,
            fontSize = 34.sp,
            color = Blue,
            modifier = Modifier
                .align(Alignment.TopCenter) // Posisikan teks di atas tengah
                .padding(top = 72.dp) // Beri jarak dari atas
        )
    }

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
            Spacer(modifier = Modifier.height(16.dp))

            CustomTextField(value = firstName, label = "First Name")
            CustomTextField(value = lastName, label = "Last Name")
            CustomTextField(value = email, label = "Email",emailError = emailError, onValueChange = { validateEmail(it) })
            CustomTextField(
                value = password,
                label = "Password",
                passwordError = passwordError,
                passwordVisibility = passwordVisibility,
                onPasswordVisibilityToggle = { passwordVisibility = !passwordVisibility },
                onValueChange = { validatePassword(it) }
            )
            CustomTextField(
                value = confirmPassword,
                label = "Confirm Password",
                passwordValue = password.value,
                passwordError = passwordError,
                passwordVisibility = passwordVisibility,
                onPasswordVisibilityToggle = { passwordVisibility = !passwordVisibility }
            )
            CustomTextField(value = address, label = "Address")
            CustomTextField(value = mobileNumber, label = "Mobile Number")
            CustomTextField(value = birthday, label = "Birthday (dd-MM-yyyy)")

            RoleSpinner(
                roles = roles,
                expanded = expanded,
                selectedRole = selectedRole,
                onExpandedChange = { expanded = it },
                onRoleSelected = { role ->
                    selectedRole = role
                    roleError = null  // Hapus error setelah memilih role
                },
                roleError = roleError
            )

            if (selectedRole == "DOCTOR") {
                CustomTextField(
                    value = workPlace,
                    label = "Work Place"  // Input tambahan untuk dokter
                )

                CustomTextField(
                    value = specializations,
                    label = "Specializations"  // Input tambahan untuk dokter
                )

                CustomTextField(
                    value = waUrl,
                    label = "Whatsapp Url"
                )// Input tambahan untuk dokter

                PdfFilePicker(
                    label = "Upload Document (PDF)",
                    selectedFileName = selectedFileUri?.lastPathSegment,
                    onFileSelected = { uri -> selectedFileUri = uri }
                )

            }

//            if (signupState is Result.Loading) {
//                Box(
//                    modifier = Modifier
//                        .fillMaxSize()
//                        .background(Color.Black.copy(alpha = 0.7f)) // Latar belakang gelap transparan
//                        .clickable(enabled = false) { } // Mengabaikan klik di background
//                ) {
//                    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.loading_drug))
//                    LottieAnimation(
//                        composition = composition,
//                        iterations = LottieConstants.IterateForever,
//                        modifier = Modifier
//                            .fillMaxSize()
//                            .align(Alignment.Center) // Posisi animasi di tengah
//                    )
//                }
//            }
            Spacer(modifier = Modifier.height(16.dp))

            // Sign Up Button
            Button(
                onClick = {
                    // Handle registration logic here
                    // Validasi pilihan role sebelum mengirim request


                    if (validateInputs()) {
                        if (selectedRole.isNullOrBlank() || (selectedRole != "PATIENT" && selectedRole != "DOCTOR")) {
                            roleError = "Please select a valid role"
                            return@Button
                        }


                        val formattedBirthday = formatDateToISO(birthday.value)
                        if (formattedBirthday == null) {
                            Toast.makeText(context, "Invalid date format. Please use dd-MM-yyyy.", Toast.LENGTH_SHORT).show()
                            return@Button
                        }
                        if(selectedRole == "PATIENT"){
                            val reqBody = AuthRequest(
                                firstName = firstName.value,
                                lastName = lastName.value,
                                email = email.value,
                                password = password.value,
                                confirmPassword = confirmPassword.value,
                                address = address.value,
                                dob = formattedBirthday,
                                role = selectedRole ?: "PATIENT",
                                workplace = workPlace.value,
                                specialization = specializations.value
                            )
                            registerViewModel.signup(reqBody)
                        }else{
                            val formattedBirthday = formatDateToISO(birthday.value)
                            if (formattedBirthday == null) {
                                Toast.makeText(context, "Invalid date format. Please use dd-MM-yyyy.", Toast.LENGTH_SHORT).show()
                                return@Button
                            }

                            if(selectedFileUri == null){
                                Toast.makeText(context, "Please select a file", Toast.LENGTH_SHORT).show()

                                return@Button
                            }


                            val request = DoctorSignupRequest(
                                role = selectedRole ?: "DOCTOR",
                                email = email.value,
                                password = password.value,
                                confirmPassword = confirmPassword.value,
                                firstName = firstName.value,
                                lastName = lastName.value,
                                dob = formattedBirthday,
                                address = address.value,
                                workplace = workPlace.value,
                                specialization = specializations.value,
                                whatsappUrl = waUrl.value,
                            )
                            val documentFile = selectedFileUri?.let { uriToFile(selectedFileUri!!,context) }
//                            val documentFile = File(context.filesDir, "document.pdf")
                            if (documentFile != null) {
                                registerViewModel.doctorSignup(request, documentFile)
                            }
                        }


                    }
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Blue,
                    contentColor = White
                ),
                modifier = Modifier
                    .width(207.dp)  // Mengatur lebar tombol
                    .height(45.dp)
            ) {
                Text("Sign Up", fontSize = 16.sp)
            }

        }


    }
    if (signupState is Result.Loading || signupResult is Result.Loading) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.7f)) // Latar belakang gelap transparan
                .clickable(enabled = false) { } // Mengabaikan klik di background
        ) {
            val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.animation_1732712667451))
            LottieAnimation(
                composition = composition,
                iterations = LottieConstants.IterateForever,
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.Center) // Posisi animasi di tengah
            )
        }
    }

    LaunchedEffect(signupState, signupResult) {
        if(selectedRole == "PATIENT"){
            signupState?.let { result ->
                when (result) {
                    is Result.Success -> {
                        // Handle success result
                        Toast.makeText(context, result.data, Toast.LENGTH_SHORT).show()
                        onRegisterSuccess()  // Navigasi jika berhasil
                    }
                    is Result.Error -> {
                        // Handle error result
                        Toast.makeText(context, result.message, Toast.LENGTH_SHORT).show() // Tampilkan error
                    }
                    else -> {}
                }
            }
        }else{
            signupResult?.let { result ->
                when (result) {
//                    is Result.Loading -> {
//                        CircularProgressIndicator() // Indikator loading
//                    }
                    is Result.Success -> {
                        Toast.makeText(context, result.data.message, Toast.LENGTH_SHORT).show()
                        onRegisterSuccess()
                    }
                    is Result.Error -> {
                        Toast.makeText(context, result.message , Toast.LENGTH_SHORT).show()
                    }
                    else -> {}
                }
            }
        }



    }

}



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RoleSpinner(
    roles: List<String>,
    expanded: Boolean,
    selectedRole: String?,
    onExpandedChange: (Boolean) -> Unit,
    onRoleSelected: (String) -> Unit,
    roleError: String?
) {
    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { onExpandedChange(it) }
    ) {
        Surface(
            modifier = Modifier
                .menuAnchor()
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            shape = MaterialTheme.shapes.medium,
            color = TextInp,
            shadowElevation = 1.dp
        ) {
            TextField(
                value = selectedRole ?: "",  // Tampilkan kosong jika null
                onValueChange = {},
                readOnly = true,
                label = {
                    Text(
                        "Role",
                        style = Typography.labelLarge,
                        color = Blue
                    )
                },
                placeholder = { Text("Select Role", color = Color.Gray, style = Typography.bodyMedium) },  // Placeholder
                trailingIcon = {
                    Icon(
                        imageVector = if (expanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                        contentDescription = null,
                        tint = Blue
                    )
                },
                isError = roleError != null,  // Tampilkan error jika ada
                supportingText = {
                    roleError?.let {
                        Text(it, color = MaterialTheme.colorScheme.error)
                        Text(it, color = MaterialTheme.colorScheme.error)
                    }
                },
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = TextInp,
                    unfocusedContainerColor = TextInp,
                    focusedIndicatorColor = Blue,
                    cursorColor = Blue
                ),
                modifier = Modifier.fillMaxWidth(),
                textStyle = Typography.bodyMedium.copy(
                    color = Black,
                    fontWeight = FontWeight.Medium
                )
            )

        }

        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { onExpandedChange(false) }
        ) {
            roles.filter { it.isNotBlank() }.forEach { role ->  // Pastikan tidak ada yang kosong
                DropdownMenuItem(
                    text = { Text(role) },
                    onClick = {
                        onRoleSelected(role)
                        onExpandedChange(false)
                    }
                )
            }
        }
    }
}

fun isValidePassword(password: String): Boolean {
    if(password.isEmpty()) return true
<<<<<<< HEAD
    val passwordPattern = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%_^&+=])(?=\\S+$).{8,}$"
=======
    val passwordPattern = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&_-])[A-Za-z\\d@$!%*?&_-]{8,}$"
>>>>>>> master
    return password.matches(passwordPattern.toRegex())
}

@SuppressLint("ModifierParameter")
@Composable
fun CustomTextField(
    value: MutableState<String>,
    label: String,
    keyboardType: KeyboardType = KeyboardType.Text,
    modifier: Modifier = Modifier,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    trailingIcon: @Composable (() -> Unit)? = null,
    emailError: String = "",
    passwordError: String = "",
    passwordValue: String = "",
    passwordVisibility: Boolean = false,
    onPasswordVisibilityToggle: () -> Unit = {},
    onValueChange: (String) -> Unit = {}
) {
//    val passTemp = remember { mutableStateOf("") }
    when (label) {
        "Mobile Number" -> {
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                shape = MaterialTheme.shapes.medium,
                color = Blue,
                shadowElevation = 1.dp
            ) {
                TextField(
                    value = value.value,
                    onValueChange = { value.value = it },
                    label = {
                        Text(label,
                            style = Typography.labelLarge,
                            color = Blue
                        )
                    },
                    keyboardOptions = KeyboardOptions.Default.copy(
                        keyboardType = KeyboardType.Phone
                    ),
                    modifier = Modifier.fillMaxWidth(),
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = TextInp,
                        unfocusedContainerColor = TextInp,
                        disabledContainerColor = TextInp,
                        focusedIndicatorColor = Blue,
                        focusedLabelColor = Blue,
                        cursorColor = Blue
                    ),
                    textStyle = Typography.bodyMedium.copy(
                        color = Black,
                        fontWeight = FontWeight.Medium
                    )
                )
            }
        }
        "Email" -> {
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                shape = MaterialTheme.shapes.medium,
                color = TextInp,
                shadowElevation = 1.dp
            ) {
                TextField(
                    value = value.value,
                    onValueChange = {
                        value.value = it
                        onValueChange(it)
                    },
                    label = {
                        Text(label,
                            style = Typography.labelLarge.copy(color = Blue)
                        )
                    },
                    keyboardOptions = KeyboardOptions.Default.copy(
                        keyboardType = KeyboardType.Email
                    ),
                    isError = emailError.isNotEmpty(),
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = TextInp,
                        unfocusedContainerColor = TextInp,
                        focusedIndicatorColor = Blue,
                        cursorColor = Blue
                    ),
                    textStyle = Typography.bodyMedium.copy(
                        color = Black,
                        fontWeight = FontWeight.Medium
                    ),
                    supportingText = {
                        if (emailError.isNotEmpty()) {
                            Text(
                                text = emailError,
                                color = Color.Red,
                                style = MaterialTheme.typography.bodySmall
                            )
                        }
                    }
                )
            }
        }

        "Password" ->{
//            passTemp.value = value.value
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                shape = MaterialTheme.shapes.medium,
                color = TextInp,
                shadowElevation = 1.dp
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    TextField(
                        value = value.value,
                        onValueChange = {
                            value.value = it
                            onValueChange(it)
                        },
                        label = {
                            Text(label,
                                style = Typography.labelLarge.copy(color = Blue)
                            )
                        },
                        keyboardOptions = KeyboardOptions.Default.copy(
                            keyboardType = KeyboardType.Password
                        ),
                        visualTransformation = if (passwordVisibility)
                            VisualTransformation.None
                        else
                            PasswordVisualTransformation(),
                        isError = passwordError.isNotEmpty(),
                        colors = TextFieldDefaults.colors(
                            focusedContainerColor = TextInp,
                            unfocusedContainerColor = TextInp,
                            focusedIndicatorColor = Blue,
                            cursorColor = Blue
                        ),
                        textStyle = Typography.bodyMedium.copy(
                            color = Black,
                            fontWeight = FontWeight.Medium
                        ),
                        supportingText = {
                            if (passwordError.isNotEmpty()) {
                                Text(
                                    text = passwordError,
                                    color = Color.Red,
                                    style = MaterialTheme.typography.bodySmall
                                )
                            }else if(!isValidePassword(value.value)){
                                Text(
                                    text = "Password must be contain a mix of upper and lower case letters, numbers, and special characters",
                                    color = Color.Red,
                                    style = MaterialTheme.typography.bodySmall
                                )
                            }
                        },
                        modifier = Modifier.weight(1f)
                    )
                    IconButton(onClick = onPasswordVisibilityToggle) {
                        Icon(
                            imageVector = if (passwordVisibility)
                                Icons.Default.Visibility
                            else
                                Icons.Default.VisibilityOff,
                            contentDescription = "Toggle password visibility"
                        )
                    }
                }
            }
        }
       "Confirm Password" ->{

            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                shape = MaterialTheme.shapes.medium,
                color = TextInp,
                shadowElevation = 1.dp
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    TextField(
                        value = value.value,
                        onValueChange = {
                            value.value = it
                            onValueChange(it)
                        },
                        label = {
                            Text(label,
                                style = Typography.labelLarge.copy(color = Blue)
                            )
                        },
                        keyboardOptions = KeyboardOptions.Default.copy(
                            keyboardType = KeyboardType.Password
                        ),
                        visualTransformation = if (passwordVisibility)
                            VisualTransformation.None
                        else
                            PasswordVisualTransformation(),
                        isError = passwordError.isNotEmpty(),
                        colors = TextFieldDefaults.colors(
                            focusedContainerColor = TextInp,
                            unfocusedContainerColor = TextInp,
                            focusedIndicatorColor = Blue,
                            cursorColor = Blue
                        ),
                        textStyle = Typography.bodyMedium.copy(
                            color = Black,
                            fontWeight = FontWeight.Medium
                        ),
                        supportingText = {
                            if (passwordError.isNotEmpty()) {
                                Text(
                                    text = passwordError,
                                    color = Color.Red,
                                    style = MaterialTheme.typography.bodySmall
                                )
                            }else if (passwordValue != value.value) {
                                Text(
                                    text = "Password and Confirm Password must be the same",
                                    color = Color.Red,
                                    style = MaterialTheme.typography.bodySmall
                                )
                            }
                        },
                        modifier = Modifier.weight(1f)
                    )
                    IconButton(onClick = onPasswordVisibilityToggle) {
                        Icon(
                            imageVector = if (passwordVisibility)
                                Icons.Default.Visibility
                            else
                                Icons.Default.VisibilityOff,
                            contentDescription = "Toggle password visibility"
                        )
                    }
                }
            }
        }
        "Birthday (dd-MM-yyyy)" -> {
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                shape = MaterialTheme.shapes.medium,
                color = TextInp,
                shadowElevation = 1.dp
            ) {
                TextField(
                    value = value.value,
                    onValueChange = { value.value = it },
                    label = {
                        Text(label,
                            style = Typography.labelLarge,
                            color = Blue
                        )
                    },
                    keyboardOptions = KeyboardOptions.Default.copy(
                        keyboardType = KeyboardType.Phone
                    ),
                    modifier = Modifier.fillMaxWidth(),
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = TextInp,
                        unfocusedContainerColor = TextInp,
                        disabledContainerColor = TextInp,
                        focusedIndicatorColor = Blue,
                        focusedLabelColor = Blue,
                        cursorColor = Blue
                    ),
                    textStyle = Typography.bodyMedium.copy(
                        color = Black,
                        fontWeight = FontWeight.Medium
                    )
                )
            }
        }

        else -> {
            Surface(
                modifier = modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                shape = MaterialTheme.shapes.medium,
                color = TextInp,
                shadowElevation = 1.dp
            ) {
                TextField(
                    value = value.value,
                    onValueChange = { value.value = it },
                    label = {
                        Text(
                            label,
                            style = Typography.labelLarge.copy(color = Blue)
                        )
                    },
                    keyboardOptions = KeyboardOptions.Default.copy(
                        keyboardType = keyboardType
                    ),
                    modifier = modifier.fillMaxWidth(),
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = TextInp,
                        unfocusedContainerColor = TextInp,
                        disabledContainerColor = TextInp,
                        focusedIndicatorColor = Blue,
                        focusedLabelColor = Blue,
                        cursorColor = Blue
                    ),
                    textStyle = Typography.bodyMedium.copy(
                        color = Black,
                        fontWeight = FontWeight.Medium
                    ),
                    visualTransformation = visualTransformation,
                    trailingIcon = trailingIcon
                )
            }
        }
//        "Email" -> CustomEmailInput(value = value, onValueChange = { validateEmail(it) })
    }
}


@Composable
fun PdfFilePicker(

    label: String,
    selectedFileName: String?,
    onFileSelected: (Uri) -> Unit
) {
    var selectedFileNameState by remember { mutableStateOf(selectedFileName ?: "STR ( Surat Tanda Registrasi) iirc") }

    // Launcher for picking a PDF file
    val filePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            onFileSelected(it)
            selectedFileNameState = uri.lastPathSegment ?: "Selected PDF"
        }
    }

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .clickable { filePickerLauncher.launch("application/pdf") },  // Launch file picker on click
        shape = MaterialTheme.shapes.medium,
        color = docInp,  // Blue color
//        shadowElevation = 1.dp
    ) {
        Column(
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp)
        ) {
            Text(
                text = label,
                style = MaterialTheme.typography.labelLarge.copy(
                    color = Blue
                ),
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = selectedFileNameState,
                style = Typography.bodyMedium.copy(
                    color = Purple40
                ),
                maxLines = 1
            )
        }
    }
}


//
//@Composable
//fun CustomfirstNameInput(value: MutableState<String>) {
//    Surface(
//        modifier = Modifier
//            .fillMaxWidth()
//            .padding(vertical = 8.dp),
//        shape = MaterialTheme.shapes.medium,
//        color = TextInp,
//        shadowElevation = 1.dp
//    ) {
//        TextField(
//            value = value.value,
//            onValueChange = { value.value = it },
//            label = {
//                Text("Full Name",
//                    style = Typography.labelLarge.copy(color = Blue)
//                )
//            },
//            modifier = Modifier.fillMaxWidth(),
//            colors = TextFieldDefaults.colors(
//                focusedContainerColor = TextInp,
//                unfocusedContainerColor = TextInp,
//                disabledContainerColor = TextInp,
//                focusedIndicatorColor = Blue,
//                focusedLabelColor = Blue,
//                cursorColor = Blue
//            ),
//            textStyle = Typography.bodyMedium.copy(
//                color = Black,
//                fontWeight = FontWeight.Medium
//            )
//        )
//    }
//}
//
//@Composable
//fun CustomEmailInput(
//    value: MutableState<String>,
//    emailError: String = "",
//    onValueChange: (String) -> Unit
//) {
//    Surface(
//        modifier = Modifier
//            .fillMaxWidth()
//            .padding(vertical = 8.dp),
//        shape = MaterialTheme.shapes.medium,
//        color = TextInp,
//        shadowElevation = 1.dp
//    ) {
//        TextField(
//            value = value.value,
//            onValueChange = {
//                value.value = it
//                onValueChange(it)
//            },
//            label = {
//                Text("Email",
//                    style = Typography.labelLarge.copy(color = Blue)
//                )
//            },
//            keyboardOptions = KeyboardOptions.Default.copy(
//                keyboardType = KeyboardType.Email
//            ),
//            isError = emailError.isNotEmpty(),
//            colors = TextFieldDefaults.colors(
//                focusedContainerColor = TextInp,
//                unfocusedContainerColor = TextInp,
//                focusedIndicatorColor = Blue,
//                cursorColor = Blue
//            ),
//            textStyle = Typography.bodyMedium.copy(
//                color = Black,
//                fontWeight = FontWeight.Medium
//            ),
//            supportingText = {
//                if (emailError.isNotEmpty()) {
//                    Text(
//                        text = emailError,
//                        color = Color.Red,
//                        style = MaterialTheme.typography.bodySmall
//                    )
//                }
//            }
//        )
//    }
//}
//
//@Composable
//fun CustomPasswordInput(
//    value: MutableState<String>,
//    passwordError: String = "",
//    passwordVisibility: Boolean,
//    onPasswordVisibilityToggle: () -> Unit,
//    onValueChange: (String) -> Unit
//) {
//    Surface(
//        modifier = Modifier
//            .fillMaxWidth()
//            .padding(vertical = 8.dp),
//        shape = MaterialTheme.shapes.medium,
//        color = TextInp,
//        shadowElevation = 1.dp
//    ) {
//        Row(
//            modifier = Modifier.fillMaxWidth(),
//            verticalAlignment = Alignment.CenterVertically
//        ) {
//            TextField(
//                value = value.value,
//                onValueChange = {
//                    value.value = it
//                    onValueChange(it)
//                },
//                label = {
//                    Text("Password",
//                        style = Typography.labelLarge.copy(color = Blue)
//                    )
//                },
//                keyboardOptions = KeyboardOptions.Default.copy(
//                    keyboardType = KeyboardType.Password
//                ),
//                visualTransformation = if (passwordVisibility)
//                    VisualTransformation.None
//                else
//                    PasswordVisualTransformation(),
//                isError = passwordError.isNotEmpty(),
//                colors = TextFieldDefaults.colors(
//                    focusedContainerColor = TextInp,
//                    unfocusedContainerColor = TextInp,
//                    focusedIndicatorColor = Blue,
//                    cursorColor = Blue
//                ),
//                textStyle = Typography.bodyMedium.copy(
//                    color = Black,
//                    fontWeight = FontWeight.Medium
//                ),
//                supportingText = {
//                    if (passwordError.isNotEmpty()) {
//                        Text(
//                            text = passwordError,
//                            color = Color.Red,
//                            style = MaterialTheme.typography.bodySmall
//                        )
//                    }
//                },
//                modifier = Modifier.weight(1f)
//            )
//            IconButton(onClick = onPasswordVisibilityToggle) {
//                Icon(
//                    imageVector = if (passwordVisibility)
//                        Icons.Default.Visibility
//                    else
//                        Icons.Default.VisibilityOff,
//                    contentDescription = "Toggle password visibility"
//                )
//            }
//        }
//    }
//}
//
//@Composable
//fun CustomMobileNumberInput(value: MutableState<String>) {
//    Surface(
//        modifier = Modifier
//            .fillMaxWidth()
//            .padding(vertical = 8.dp),
//        shape = MaterialTheme.shapes.medium,
//        color = Blue,
//        shadowElevation = 1.dp
//    ) {
//        TextField(
//            value = value.value,
//            onValueChange = { value.value = it },
//            label = {
//                Text("Mobile Number",
//                    style = Typography.labelLarge,
//                    color = Blue
//                )
//            },
//            keyboardOptions = KeyboardOptions.Default.copy(
//                keyboardType = KeyboardType.Phone
//            ),
//            modifier = Modifier.fillMaxWidth(),
//            colors = TextFieldDefaults.colors(
//                focusedContainerColor = TextInp,
//                unfocusedContainerColor = TextInp,
//                disabledContainerColor = TextInp,
//                focusedIndicatorColor = Blue,
//                focusedLabelColor = Blue,
//                cursorColor = Blue
//            ),
//            textStyle = Typography.bodyMedium.copy(
//                color = Black,
//                fontWeight = FontWeight.Medium
//            )
//        )
//    }
//}
//
//@Composable
//fun CustomBirthdayInput(value: MutableState<String>) {
//    Surface(
//        modifier = Modifier
//            .fillMaxWidth()
//            .padding(vertical = 8.dp),
//        shape = MaterialTheme.shapes.medium,
//        color = TextInp,
//        shadowElevation = 1.dp
//    ) {
//        TextField(
//            value = value.value,
//            onValueChange = { value.value = it },
//            label = {
//                Text("Birthday (dd/MM/yyyy)",
//                    style = Typography.labelLarge,
//                    color = Blue
//                )
//            },
//            keyboardOptions = KeyboardOptions.Default.copy(
//                keyboardType = KeyboardType.Phone
//            ),
//            modifier = Modifier.fillMaxWidth(),
//            colors = TextFieldDefaults.colors(
//                focusedContainerColor = TextInp,
//                unfocusedContainerColor = TextInp,
//                disabledContainerColor = TextInp,
//                focusedIndicatorColor = Blue,
//                focusedLabelColor = Blue,
//                cursorColor = Blue
//            ),
//            textStyle = Typography.bodyMedium.copy(
//                color = Black,
//                fontWeight = FontWeight.Medium
//            )
//        )
//    }
//}



@Composable
fun RegisterScreenPreview() {
    DermaScanTheme {
        RegisterScreen(onRegisterSuccess = { /* action setelah login */ })
    }
}

