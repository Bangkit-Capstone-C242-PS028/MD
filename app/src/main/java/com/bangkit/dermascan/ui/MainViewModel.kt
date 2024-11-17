package com.bangkit.dermascan.ui

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.bangkit.dermascan.data.repository.UserRepository
import com.example.storyapp.data.pref.UserModel
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
//import com.google.firebase.auth.FirebaseAuth
//import android.util.Log

class MainViewModel(private val repository: UserRepository): ViewModel() {

    fun getSession(): LiveData<UserModel> {
        return repository.getSession().asLiveData()
    }

    fun logout() {
        viewModelScope.launch {
            repository.logout()
        }
    }

    fun saveSession(user: UserModel) {
        viewModelScope.launch {
            repository.saveSession(user)
        }
    }


    private val firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()

    fun signIn(email:String, pass:String, callback: (String?) -> Unit) {
//        val email = "irun@gmail.com"
//        val pass = "Irun1234"

        firebaseAuth.signInWithEmailAndPassword(email, pass)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    firebaseAuth.currentUser?.getIdToken(true)
                        ?.addOnCompleteListener { tokenTask ->
                            if (tokenTask.isSuccessful) {
                                val idToken = tokenTask.result?.token
                                Log.d("AccesToken", "Bearer $idToken")
                                callback(idToken) // Kembalikan idToken melalui callback
                            } else {
                                Log.e("AccesToken", "Error getting ID token: ${tokenTask.exception?.message}")
                                callback(null)
                            }
                        }
                } else {
                    Log.e("AccesToken", "Sign in failed: ${task.exception?.message}")
                    callback(null)
                }
            }
    }

}