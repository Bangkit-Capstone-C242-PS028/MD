package com.bangkit.dermascan.ui.login

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.bangkit.dermascan.R
import com.bangkit.dermascan.data.local.UserModel
import com.bangkit.dermascan.data.model.requestBody.AuthRequest
import com.bangkit.dermascan.data.repository.ApiRepository
import com.bangkit.dermascan.data.repository.UserRepository
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import com.bangkit.dermascan.util.Result

@HiltViewModel
class AuthViewModel @Inject constructor(private val repository: UserRepository, private val apiRepository: ApiRepository) : ViewModel() {

    private fun saveSession(user: UserModel) {
        viewModelScope.launch {
            repository.saveSession(user)
        }
    }
    private fun updateToken(newToken: String) {
        viewModelScope.launch {
            repository.updateToken(newToken)
        }
    }


    fun refreshToken() {
        val firebaseAuth = FirebaseAuth.getInstance()

        firebaseAuth.currentUser?.getIdToken(true)?.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val idToken = task.result?.token
                if (idToken != null) {
                    updateToken(idToken)  // Update token if successfully retrieved
                    Log.d("Refresh Token", "Bearer $idToken")
                } else {
                    Log.e("TokenError", "Token is null")
                }
            } else {
                Log.e("TokenError", "Failed to refresh token: ${task.exception?.message}")
            }
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
                                saveSession(UserModel(email, idToken.toString(), true))
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
//            _signInStatus.value = false
        }

        // Sign out from Firebase
        firebaseAuth.signOut()
    }

    fun getSession(): LiveData<UserModel> {
        return repository.getSession().asLiveData()
    }

    fun logout() {
        viewModelScope.launch {
            repository.logout()
        }
    }

    private val _signupState = MutableStateFlow<Result<String>?>(null) // StateFlow untuk status signup
    val signupState: StateFlow<Result<String>?> = _signupState

    fun signup(signupRequest: AuthRequest) {
        viewModelScope.launch {
            _signupState.value = Result.Loading  // Set loading state
            when (val result = apiRepository.signup(signupRequest)) {  // Call signup method
                is Result.Success -> {
                    _signupState.value = Result.Success(result.data)  // Registration success
                }
                is Result.Error -> {
                    _signupState.value = Result.Error(result.message)  // Pass error message
                }
                else -> {
                    _signupState.value = Result.Error("Unknown error occurred")
                }
            }
        }
    }

    fun signInWithCustomToken(customToken: String, callback: (String?) -> Unit) {
        firebaseAuth.signInWithCustomToken(customToken)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    firebaseAuth.currentUser?.getIdToken(true)
                        ?.addOnCompleteListener { tokenTask ->
                            if (tokenTask.isSuccessful) {
                                val idToken = tokenTask.result?.token
                                val userName = firebaseAuth.currentUser?.displayName ?: "Unknown"
                                Log.d("AccessToken", "Bearer $idToken")
                                Log.d("User Name", "User : $userName")
                                saveSession(UserModel(firebaseAuth.currentUser?.email ?: "Unknown", idToken.toString(), true))
                                callback(idToken) // Kembalikan idToken melalui callback
                            } else {
                                Log.e("AccessToken", "Error getting ID token: ${tokenTask.exception?.message}")
                                callback(null)
                            }
                        }
                } else {
                    Log.e("AccessToken", "Sign in failed: ${task.exception?.message}")
                    callback(null)
                }
            }
    }

}

