package com.bangkit.dermascan.data.model

data class ApiResponse(
    val success: Boolean,
    val message: String,
    val data: Any? = null // Tergantung pada data yang diterima dari server, bisa diganti sesuai kebutuhan
)
