package com.bangkit.dermascan.ui.splashScreen

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.bangkit.dermascan.R
import com.bangkit.dermascan.ui.MainViewModel
import com.bangkit.dermascan.ui.ViewModelFactory
import com.bangkit.dermascan.ui.login.AuthViewModel
import kotlinx.coroutines.delay
import com.bangkit.dermascan.ui.theme.*

@Composable
fun SplashScreen(navController: NavController) {
    val viewModel : AuthViewModel = hiltViewModel()
    val userSession by viewModel.getSession().observeAsState()
    userSession?.let{ user ->
        LaunchedEffect(Unit) {
            delay(2500)
            Log.d("Session", user.toString())
            if(user.isLogin){
                //panggil endpoint customToken
                viewModel.refreshToken()
//            LaunchedEffect(Unit) {
                navController.navigate("main") {
                    popUpTo("splash") { inclusive = true }
//
                }
//            }
            }else{
                navController.navigate("onBoard") {
                    popUpTo("splash") { inclusive = true }
                }

            }
        }

    }


    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Blue),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic),
                contentDescription = "Search Icon",
                modifier = Modifier.size(200.dp),
                tint = White
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = stringResource(R.string.app_name),
                color = White,
                fontSize = 34.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

