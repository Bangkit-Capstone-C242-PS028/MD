package com.bangkit.dermascan.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.bangkit.dermascan.R

// Deklarasi FontFamily dengan tiga varian font
val myFontFamily = FontFamily(
    Font(R.font.league_spartan_bold, FontWeight.Bold),
    Font(R.font.league_spartan_light, FontWeight.Light),
    Font(R.font.league_spartan_medium, FontWeight.Medium)
)

// Menyusun Typography dengan fontFamily yang sudah dideklarasikan
val Typography = Typography(
    bodyLarge = TextStyle(
        fontFamily = myFontFamily,  // Menggunakan myFontFamily untuk bodyLarge
        fontWeight = FontWeight.Light, // Bisa diubah sesuai kebutuhan
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.5.sp
    ),
    // Contoh pengaturan untuk tipe teks lainnya
    titleLarge = TextStyle(
        fontFamily = myFontFamily,  // Menggunakan myFontFamily
        fontWeight = FontWeight.Bold,  // Menambahkan Bold pada titleLarge
        fontSize = 22.sp,
        lineHeight = 28.sp,
        letterSpacing = 0.sp
    ),
    labelSmall = TextStyle(
        fontFamily = myFontFamily,  // Menggunakan myFontFamily
        fontWeight = FontWeight.Medium,  // Menambahkan Medium pada labelSmall
        fontSize = 11.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.5.sp
    )
)
