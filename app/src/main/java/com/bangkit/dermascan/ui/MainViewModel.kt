package com.bangkit.dermascan.ui

import androidx.lifecycle.ViewModel
import com.bangkit.dermascan.data.repository.UserRepository
import com.bangkit.dermascan.data.repository.ApiRepository
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel

import javax.inject.Inject

//import com.google.firebase.auth.FirebaseAuth
//import android.util.Log

@HiltViewModel
class MainViewModel @Inject constructor(private val repository: UserRepository, private val apiRepository: ApiRepository): ViewModel() {
    private lateinit var auth: FirebaseAuth









//    fun signInWithGoogle(context: Context) {
//        val credentialManager = CredentialManager.create(context)
//        val googleIdOption = GetGoogleIdOption.Builder()
//            .setFilterByAuthorizedAccounts(false)
//            .setServerClientId(context.getString(R.string.web_client_id))
//            .build()
//        val request = GetCredentialRequest.Builder()
//            .addCredentialOption(googleIdOption)
//            .build()
//
//        viewModelScope.launch {
//            try {
//                val result: GetCredentialResponse = credentialManager.getCredential(request = request,context = context)
//                handleSignIn(result)
//            } catch (e: GetCredentialException) {
//                Log.e(TAG, e.message.toString())
//                _signInStatus.postValue(false)
//            }
//        }
//    }

//    private fun handleSignIn(result: GetCredentialResponse) {
//        when (val credential = result.credential) {
//            is CustomCredential -> {
//                if (credential.type == GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL) {
//                    try {
//                        val googleIdTokenCredential = GoogleIdTokenCredential.createFrom(credential.dataArticles)
//                        firebaseAuthWithGoogle(googleIdTokenCredential.idToken)
//                    } catch (e: GoogleIdTokenParsingException) {
//                        Log.e(TAG, "Invalid Google ID token", e)
//                        _signInStatus.postValue(false)
//                    }
//                } else {
//                    Log.e(TAG, "Unexpected credential type")
//                    _signInStatus.postValue(false)
//                }
//            }
//            else -> {
//                Log.e(TAG, "Unexpected credential type")
//                _signInStatus.postValue(false)
//            }
//        }
//    }

//    private fun firebaseAuthWithGoogle(idToken: String) {
//        val credential = GoogleAuthProvider.getCredential(idToken, null)
//        firebaseAuth.signInWithCredential(credential)
//            .addOnCompleteListener { task ->
//                if (task.isSuccessful) {
//                    Log.d(TAG, "signInWithCredential:success")
//                    _signInStatus.postValue(true)
//                } else {
//                    Log.w(TAG, "signInWithCredential:failure", task.exception)
//                    _signInStatus.postValue(false)
//                }
//            }
//    }

//    private val _signInStatus = MutableLiveData<Boolean>()
//    val signInStatus: LiveData<Boolean> = _signInStatus


}