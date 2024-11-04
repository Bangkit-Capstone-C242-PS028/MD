package com.example.derma_scan.ui.home

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.derma_scan.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Preview(showBackground = true)
@Composable
fun HomeScreen() {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Derma Scan",
            style = MaterialTheme.typography.headlineMedium
        )
        Spacer(modifier = Modifier.height(16.dp))

        Image(
            painter = painterResource(id = R.drawable.cover),
            contentDescription = "Dermatology Image",
            modifier = Modifier
                .size(200.dp)
                .padding(bottom = 16.dp)
        )

        Text(
            text = "Scan your skin with our advanced technology and get insights about your skin health.",
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier
                .padding(
                    bottom = 16.dp,
                    start = 30.dp,
                    end = 30.dp
                )

        )
        val context = LocalContext.current
        val coroutine = rememberCoroutineScope()
        Button(onClick = {
            coroutine.launch {
                Toast.makeText(context, "Hello from Compose!", Toast.LENGTH_SHORT).show()
            }
        }) {
            Text(text = "Show Toast")
        }
    }
}