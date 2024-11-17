package com.bangkit.dermascan.ui

import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.provider.Settings.Global.getString
import android.util.Log
import androidx.credentials.CredentialManager
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialRequest
import androidx.credentials.GetCredentialResponse
import androidx.credentials.exceptions.GetCredentialException
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.bangkit.dermascan.R
import com.bangkit.dermascan.data.repository.UserRepository
import com.example.storyapp.data.pref.UserModel
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.android.libraries.identity.googleid.GoogleIdTokenParsingException
import com.google.firebase.Firebase
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.auth
import kotlinx.coroutines.Dispatchers

import kotlinx.coroutines.launch
//import com.google.firebase.auth.FirebaseAuth
//import android.util.Log

class MainViewModel(private val repository: UserRepository): ViewModel() {
    private lateinit var auth: FirebaseAuth
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
                                val userName = firebaseAuth.currentUser?.displayName ?: "Unknown"
                                Log.d("AccesToken", "Bearer $idToken")
                                Log.d("User Name", "User : $userName")
                                saveSession(UserModel(email, idToken.toString()))
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

    fun signOut(context: Context) {
        viewModelScope.launch {
            // Logout from the repository (you can remove this if you already handle logout in repo)
            repository.logout()

            // Setup Google Sign-In client using context and GoogleSignInOptions (gso)
            val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(context.getString(R.string.default_web_client_id)) // Replace with your web client ID
                .requestEmail()
                .build()

            val googleSignInClient = GoogleSignIn.getClient(context, gso)

            // Sign out from Google
            googleSignInClient.signOut()
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        // Tindakan setelah berhasil sign out dari Google
                        Log.d("SignOut", "Successfully signed out from Google.")
                    } else {
                        // Tindakan jika terjadi kesalahan saat sign out dari Google
                        Log.e("SignOut", "Error signing out from Google: ${task.exception?.message}")
                    }
                }

            // Reset status login after sign out
            _signInStatus.value = false
        }

        // Sign out from Firebase
        firebaseAuth.signOut()
    }

    private val _signInStatus = MutableLiveData<Boolean>()
    val signInStatus: LiveData<Boolean> = _signInStatus

    fun handleSignInResult(data: Intent?) {
        val task = GoogleSignIn.getSignedInAccountFromIntent(data)
        try {
            val account = task.getResult(ApiException::class.java)
            val credential = GoogleAuthProvider.getCredential(account.idToken, null)

            // Login ke Firebase dengan kredensial Google
            FirebaseAuth.getInstance().signInWithCredential(credential)
                .addOnCompleteListener { task ->
                    _signInStatus.value = task.isSuccessful
                    if (task.isSuccessful) {
                        // Dapatkan ID token dari Firebase
                        firebaseAuth.currentUser?.getIdToken(true)?.addOnCompleteListener { tokenTask ->
                            if (tokenTask.isSuccessful) {
                                val idToken = tokenTask.result?.token

                                // Buat UserModel dengan token yang didapat
                                val user = firebaseAuth.currentUser?.let {
                                    UserModel(it.email ?: "Unknown", idToken ?: "", isLogin = true)
                                }

                                // Pastikan user tidak null sebelum menyimpan session
                                if (user != null) {
                                    Log.d("Diplay name", user.name)
                                    Log.d("AccessToken", "Bearer $idToken")
                                    saveSession(user) // Simpan session setelah login berhasil
                                }
                            } else {
                                Log.e("AccessToken", "Error getting ID token: ${tokenTask.exception?.message}")
                            }
                        }
                    }
                }
        } catch (e: ApiException) {
            _signInStatus.value = false
            Log.e("GoogleSignIn", "Google sign-in failed", e)
        }
    }



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
//                        val googleIdTokenCredential = GoogleIdTokenCredential.createFrom(credential.data)
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