package com.bangkit.dermascan.ui.main.chat

import android.annotation.SuppressLint
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material3.IconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.bangkit.dermascan.ui.authentication.register.CustomTextField
import com.bangkit.dermascan.ui.theme.Black
import com.bangkit.dermascan.ui.theme.Blue
import com.bangkit.dermascan.ui.theme.LightBlue
import com.bangkit.dermascan.ui.theme.Typography
import com.bangkit.dermascan.ui.theme.White

@Composable
fun ChatScreen() {
    val viewModel: ChatViewModel = hiltViewModel()

    val userMessage = remember { mutableStateOf("") } // MutableState untuk input pengguna

    // Mengamati LiveData atau StateFlow dari ViewModel untuk daftar pesan
    val chatMessages by viewModel.chatMessages.collectAsState()

    val chatReply by viewModel.chatReply.collectAsState()

    LaunchedEffect(chatReply) {
        // Cek apakah balasan sudah ada dalam daftar
        chatReply.takeIf { it.isNotEmpty() }?.let { reply ->
            viewModel.addBotReplyIfNotExist(reply) // Menambahkan balasan bot jika belum ada
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(8.dp) // Menambahkan padding yang konsisten di seluruh layar
    ) {
        // Menampilkan pesan chat
        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = 0.dp), // Padding horizontal agar pesan tidak terlalu mepet ke sisi layar
            reverseLayout = true
        ) {
            if (chatMessages.isEmpty()) {
                item {
                    Text(
                        text = "Start new chat",
                        style = Typography.titleLarge,
                        color = MaterialTheme.colorScheme.onBackground,
                        modifier = Modifier.fillMaxWidth().wrapContentWidth(Alignment.CenterHorizontally).padding(16.dp)
                    )
                }
            } else {
                items(chatMessages.reversed()) { message ->
                    ChatBubble(message.first, message.second)
                    Spacer(modifier = Modifier.height(8.dp)) // Memberikan jarak antar bubble chat
                }
            }

        }

        // Row untuk input pesan dan tombol kirim
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp, vertical = 8.dp)
                .imePadding(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Input field untuk pesan pengguna
            OutlinedTextField(
                value = userMessage.value,
                onValueChange = { userMessage.value = it },
                placeholder = { Text("Tulis pesan...", color = Color.Gray, fontSize = 13.sp) },
                modifier = Modifier
                    .weight(1f)
                    .padding(end = 8.dp),
                shape = RoundedCornerShape(24.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Blue,
                    unfocusedBorderColor = LightBlue,
                    focusedContainerColor = MaterialTheme.colorScheme.surface,
                    unfocusedContainerColor = MaterialTheme.colorScheme.surface
                ),
                maxLines = 4
            )

            // Tombol Kirim bulat dengan ikon
            IconButton(
                onClick = {
                    if (userMessage.value.isNotBlank()) {
                        viewModel.sendMessage(userMessage.value)
                        userMessage.value = ""
                    }
                },
                modifier = Modifier
                    .size(48.dp)
                    .background(color = Blue, shape = CircleShape)
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.Send,
                    contentDescription = "Send",
                    tint = White,
                    modifier = Modifier.size(24.dp)
                )
            }
        }
    }
}

@SuppressLint("UseOfNonLambdaOffsetOverload")
@Composable
fun ChatBubble(message: String, isUser: Boolean) {
    val annotatedMessage = buildAnnotatedString {
        // Menambahkan format Markdown yang sederhana
        var remainingMessage = message
        while (remainingMessage.contains("**")) {
            val start = remainingMessage.indexOf("**")
            val end = remainingMessage.indexOf("**", start + 2)

            // Menambahkan teks sebelum bold
            append(remainingMessage.substring(0, start))

            // Menambahkan teks bold
            pushStyle(SpanStyle(fontWeight = FontWeight.Bold))
            append(remainingMessage.substring(start + 2, end))
            pop()

            remainingMessage = remainingMessage.substring(end + 2)
        }
        // Menambahkan sisa pesan setelah memproses bold
        append(remainingMessage)
    }

    // User chat bubble (right side not rounded)
    val userBubbleShape = RoundedCornerShape(
        topStart = 16.dp,
        topEnd = 0.dp,
        bottomStart = 16.dp,
        bottomEnd = 16.dp
    )

    // AI/Bot chat bubble (left side not rounded)
    val botBubbleShape = RoundedCornerShape(
        topStart = 0.dp,
        topEnd = 16.dp,
        bottomStart = 16.dp,
        bottomEnd = 16.dp
    )

    // State untuk animasi
    var isVisible by remember { mutableStateOf(false) }

    // Memicu perubahan animasi ketika pesan baru muncul
    LaunchedEffect(message) {
        isVisible = true
    }

    // Animasi untuk munculnya chat bubble
    val scale by animateFloatAsState(
        targetValue = if (isVisible) 1f else 0f,
        animationSpec = tween(durationMillis = 500, easing = FastOutSlowInEasing)
    )

    val alpha by animateFloatAsState(
        targetValue = if (isVisible) 1f else 0f,
        animationSpec = tween(durationMillis = 500, easing = FastOutSlowInEasing)
    )

    // Menggunakan LocalDensity untuk mendapatkan ukuran layar
    val density = LocalDensity.current.density
    val screenWidth = LocalConfiguration.current.screenWidthDp * density // Lebar layar dalam piksel

    // Menghitung offset untuk animasi masuk (slide dari samping)
    val offsetX by animateFloatAsState(
        targetValue = if (isVisible) 0f else (if (isUser) 50f else -50f),
        animationSpec = tween(durationMillis = 400, easing = FastOutSlowInEasing)
    )

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 4.dp) // Padding horizontal dan vertikal
            .offset(x = offsetX.dp) // Animasi pergeseran
            .graphicsLayer(
                scaleX = scale,
                scaleY = scale,
                alpha = alpha
            ),
        contentAlignment = if (isUser) Alignment.CenterEnd else Alignment.CenterStart
    ) {
        Text(
            text = annotatedMessage,
            style = Typography.bodyMedium,
            color = if (isUser) White else Black,
            modifier = Modifier
                .widthIn(max = 280.dp)
                .background(
                    color = if (isUser) Blue else LightBlue,
                    shape = if (isUser) userBubbleShape else botBubbleShape
                )
                .padding(12.dp) // Padding di dalam bubble chat
        )
    }
}





