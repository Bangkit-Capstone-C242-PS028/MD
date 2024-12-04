package com.bangkit.dermascan.ui.main.chat

import androidx.lifecycle.ViewModel
import com.bangkit.dermascan.data.repository.ApiRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class ChatViewModel @Inject constructor(private val repository: ApiRepository) : ViewModel() {

    // StateFlow untuk riwayat pesan
    private val _chatMessages = MutableStateFlow<List<Pair<String, Boolean>>>(emptyList())
    val chatMessages: StateFlow<List<Pair<String, Boolean>>> = _chatMessages

    // StateFlow untuk balasan chatbot
    private val _chatReply = MutableStateFlow("")
    val chatReply: StateFlow<String> get() = _chatReply

    // Fungsi untuk mengirim pesan
    fun sendMessage(message: String) {
        // Menambahkan pesan pengguna ke dalam daftar pesan
        _chatMessages.value = _chatMessages.value + Pair(message, true) // true untuk pesan pengguna

        // Mengirim pesan ke repository untuk mendapatkan respons
        repository.sendMessage(message) { result ->
            result.onSuccess { chatData ->
                // Menyimpan balasan dari chatbot
                _chatReply.value = chatData.reply
            }.onFailure { error ->
                // Menangani kesalahan jika ada
                _chatReply.value = "Error: ${error.localizedMessage}"
            }
        }
    }

    // Fungsi untuk menambahkan respons bot jika belum ada
    fun addBotReplyIfNotExist(reply: String) {
        // Cek apakah balasan bot sudah ada
        if (_chatMessages.value.none { it.first == reply && !it.second }) {
            _chatMessages.value = _chatMessages.value + Pair(reply, false) // false untuk pesan bot
        }
    }
}
