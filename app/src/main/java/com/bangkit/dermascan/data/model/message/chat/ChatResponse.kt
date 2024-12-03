package com.bangkit.dermascan.data.model.message.chat

data class ChatResponse(
    val statusCode: Int,
    val message: String,
    val data: ChatData
)

data class ChatData(
    val reply: String
)

data class ChatRequest(
    val message: String
)