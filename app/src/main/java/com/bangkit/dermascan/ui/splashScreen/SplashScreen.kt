package com.bangkit.dermascan.ui.splashScreen

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil3.compose.AsyncImage
import com.bangkit.dermascan.R
import com.bangkit.dermascan.ui.authentication.AuthViewModel
import com.bangkit.dermascan.ui.theme.Blue
import com.bangkit.dermascan.ui.theme.SplashBg
import com.bangkit.dermascan.ui.theme.White
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(navController: NavController) {
    val context = LocalContext.current
    val viewModel : AuthViewModel = hiltViewModel()
    val userSession by viewModel.getSession().observeAsState()
    userSession?.let{ user ->
        LaunchedEffect(Unit) {

            Log.d("Session", user.toString())
            if(user.isLogin){
//                viewModel.login(user.email, "P@ssword1")
//                viewModel.token.observeForever { token ->
//                    Log.d("Token", token)
//                }
//                Toast.makeText(context, "Welcome Back ${user.firstName} ${user.lastName}", Toast.LENGTH_SHORT).show()
                //panggil endpoint customToken
                viewModel.refreshToken()
                delay(2500)
//            LaunchedEffect(Unit) {
//                onSplashFinished()
                navController.navigate("main") {
                    popUpTo("splash") { inclusive = true }
                }
//            }
            }else{
                delay(2500)
                navController.navigate("onBoard") {
                    popUpTo("splash") { inclusive = true }
                }

            }
        }

    }


    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(SplashBg),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            AsyncImage(
                model = R.drawable.derma_scan_logo,
                contentDescription = "Logo",
                modifier = Modifier.size(300.dp),
                placeholder = painterResource(id = R.drawable.ic_launcher_foreground),
            )

//            Spacer(modifier = Modifier.height(16.dp))
//
//            Text(
//                text = stringResource(R.string.app_name),
//                color = White,
//                fontSize = 34.sp,
//                fontWeight = FontWeight.Bold
//            )
        }
    }
}

