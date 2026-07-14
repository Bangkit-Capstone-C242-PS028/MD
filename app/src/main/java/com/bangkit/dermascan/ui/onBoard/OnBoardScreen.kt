package com.bangkit.dermascan.ui.onBoard

import android.content.ContentValues.TAG
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil3.compose.AsyncImage
import com.bangkit.dermascan.R
import com.bangkit.dermascan.ui.authentication.AuthViewModel
import com.bangkit.dermascan.ui.theme.*
import com.google.firebase.auth.FirebaseAuth

@Composable
fun OnBoardScreen(navController: NavController, onLoginSuccess: () -> Unit) {
    val context = LocalContext.current
    val viewModel : AuthViewModel = hiltViewModel()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 32.dp, vertical = 48.dp)
        ) {
            // Header Space or Top Hello
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(top = 24.dp)
            ) {
                Text(
                    text = "Hello!",
                    style = Typography.titleLarge,
                    fontSize = 24.sp,
                    color = Blue,
                    fontWeight = FontWeight.Bold
                )
            }

            // Center Illustration/Logo
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                modifier = Modifier.weight(1f)
            ) {
                AsyncImage(
                    model = R.drawable.derma_scan_logo,
                    contentDescription = "Logo",
                    modifier = Modifier.size(240.dp),
                    placeholder = painterResource(id = R.drawable.ic_launcher_foreground),
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = stringResource(id = R.string.app_name),
                    style = Typography.titleLarge,
                    fontSize = 44.sp,
                    color = Blue,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Scan your skin health with ease.",
                    style = Typography.bodyMedium,
                    fontSize = 16.sp,
                    color = if (isSystemInDarkTheme()) White else Black.copy(alpha = 0.7f),
                    textAlign = TextAlign.Center
                )
            }

            // Bottom CTAs
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxWidth()
            ) {
                Button(
                    onClick = {
                        onLoginSuccess()
                        navController.navigate("login") {
                            popUpTo("onBoard") {
                                inclusive = false
                            }
                        }
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Blue,
                        contentColor = White
                    ),
                    shape = androidx.compose.foundation.shape.RoundedCornerShape(12.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp)
                ) {
                    Text("Log In", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                }

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = {
                        navController.navigate("register") {
                            popUpTo("onBoard") {
                                inclusive = false
                            }
                        }
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = LightBlue,
                        contentColor = Blue
                    ),
                    shape = androidx.compose.foundation.shape.RoundedCornerShape(12.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp)
                ) {
                    Text("Sign Up", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}


private fun signInWithEmailPassword(auth: FirebaseAuth, email: String, password: String, navController: NavController) {
    auth.signInWithEmailAndPassword(email, password)
        .addOnCompleteListener { task ->
            if (task.isSuccessful) {
                // Navigate to MainActivity after successful login
                navController.navigate("main") {
                    popUpTo("onBoard") { inclusive = true }
                }
            } else {
                Log.w(TAG, "signInWithEmailPassword:failure", task.exception)
                // Handle failure (e.g., show a toast)
            }
        }
}


@Preview(showBackground = true)
@Composable
fun OnBoardScreenPreview() {
    val navController = rememberNavController()
    OnBoardScreen(navController = navController, onLoginSuccess = {})
}


