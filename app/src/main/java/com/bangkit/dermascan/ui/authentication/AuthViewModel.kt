package com.bangkit.dermascan.ui.authentication

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.bangkit.dermascan.R
import com.bangkit.dermascan.data.Pref.UserModel
import com.bangkit.dermascan.data.model.response.message.SuccessMessage
import com.bangkit.dermascan.data.model.requestBody.AuthRequest
import com.bangkit.dermascan.data.model.requestBody.DoctorSignupRequest
import com.bangkit.dermascan.data.model.response.LoginResponse
import com.bangkit.dermascan.data.model.response.UserData
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
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File

@HiltViewModel
class AuthViewModel @Inject constructor(private val repository: UserRepository, private val apiRepository: ApiRepository) : ViewModel() {


    private val _token = MutableLiveData<String>()
    val token: LiveData<String> get() = _token

//    val userToken: LiveData<String> = repository.getToken().asLiveData(viewModelScope.coroutineContext)
    val currentUser: LiveData<UserModel> = repository.getSession().asLiveData(viewModelScope.coroutineContext)

    val roles: LiveData<String> = repository.getRoles().asLiveData(viewModelScope.coroutineContext)

    fun login(email: String, password: String) {
        apiRepository.login(email, password).enqueue(object : Callback<LoginResponse> {
            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                if (response.isSuccessful) {
                    // Ambil token dari response dan kirimkan ke UI
                    _token.postValue(apiRepository.getTokenFromResponse(response.body()))
                    Log.d("Token", _token.value.toString())
                    Log.d("AuthVM", "Login Succes")
                } else {
                    // Tangani kesalahan login
                    Log.e("LoginError", "Login failed: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                // Tangani kegagalan komunikasi
                Log.e("LoginError", "Login request failed: ${t.message}")
            }
        })
    }

    // Fungsi untuk mengambil detail user dan menyimpan ke DataStore
    private fun fetchUserDetail(onResult: (Result<UserData>) -> Unit) {
        viewModelScope.launch {
            val result = apiRepository.getDetailUser().first() // Collect data sekali
            onResult(result) // Callback untuk mengembalikan hasil ke UI
        }
    }

    private fun saveSession(user: UserModel) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.saveSession(user)
        }
    }
    private fun updateToken(newToken: String) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.updateToken(newToken)
        }
    }

    private fun getToken(): Flow<String> {
        return repository.getToken()
    }

    private fun saveUserData(userData: UserModel){
        viewModelScope.launch {
            repository.saveUserData(userData)
        }
//        return repository.saveUserData(userData)// Mengambil data dari DataStore sekali
    }





    private val firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()

    fun refreshToken() {

        firebaseAuth.currentUser?.getIdToken(true)?.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                viewModelScope.launch {
                    val idToken = task.result?.token
                    if (idToken != null) {
                        updateToken(idToken)  // Simpan token baru

                        // Ambil token setelah penyimpanan selesai
                        val savedToken = getToken().first()  // Mengambil nilai tunggal terbaru
                        Log.d("DataStore", "Saved Token: Bearer $savedToken")

                        if (idToken == savedToken) {
                            Log.d("TokenCheck", "Token matches!")
                        } else {
                            Log.e("TokenCheck", "Token mismatch detected")
                        }
                    }
                }
            } else {
                Log.e("TokenError", "Failed to refresh token: ${task.exception?.message}")
            }
        }
    }
    private val _signInStatus = MutableStateFlow<Result<Boolean>>(Result.Idle)
//    val signInStatus: StateFlow<Result<Boolean>> = _signInStatus

    // Mengonversi StateFlow menjadi LiveData
    val signInStatusLiveData: LiveData<Result<Boolean>> = _signInStatus.asLiveData()

    fun signIn(email: String, pass: String) {
        viewModelScope.launch {
            try {
                _signInStatus.value = Result.Loading
                firebaseAuth.signInWithEmailAndPassword(email, pass)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            firebaseAuth.currentUser?.getIdToken(true)
                                ?.addOnCompleteListener { tokenTask ->
                                    val idToken = tokenTask.result?.token
                                    val userName = firebaseAuth.currentUser?.displayName ?: "Unknown"
                                    Log.d("AccessToken", "Bearer $idToken")
                                    Log.d("User Name", "User : $userName")

                                    viewModelScope.launch {
                                        saveSession(UserModel(email = email, token = idToken.toString(), isLogin = true))
                                        delay(1000L)

                                        fetchUserDetail { result ->
                                            when (result) {
                                                is Result.Success -> {
                                                    val userData = UserModel(
                                                        uid = result.data.uid ?: "",
                                                        firstName = result.data.firstName ?: "",
                                                        lastName = result.data.lastName ?: "",
                                                        role = result.data.role ?: "",
                                                        dob = result.data.dob ?: "",
                                                        address = result.data.address ?: "",
                                                        email = result.data.email ?: "",
                                                        specialization = result.data.doctor?.specialization ?: "Not Doctor",
                                                        workplace = result.data.doctor?.workplace ?: "Not Doctor"
                                                    )
                                                    saveUserData(userData)
                                                    _signInStatus.value = Result.Success(true)  // Sign-in berhasil
                                                }
                                                is Result.Error -> {
                                                    Log.e("FetchUserDetail", "Error fetching user details: ${result.message}")
                                                    _signInStatus.value = Result.Error("Error fetching user details")
                                                }

                                                Result.Idle -> {}
                                                Result.Loading -> {}
                                            }
                                        }
                                    }
                                }
                        } else {
                            Log.e("AccessToken", "Sign in failed: ${task.exception?.message}")
                            _signInStatus.value = Result.Error("Sign-in failed")
                        }
                    }
            } catch (e: Exception) {
                Log.e("SignInError", "Error during sign-in: ${e.localizedMessage}")
                _signInStatus.value = Result.Error("Error during sign-in")
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

    private val _signupResult = MutableLiveData<Result<SuccessMessage>>()
    val signupResult: LiveData<Result<SuccessMessage>> get() = _signupResult

    fun doctorSignup(request: DoctorSignupRequest, documentFile: File) {
        viewModelScope.launch {
            _signupResult.value = Result.Loading  // Indikator loading

            try {
                val response = apiRepository.doctorSignup(request, documentFile)
                if (response.isSuccessful) {
                    _signupResult.value = Result.Success(response.body()!!)
                } else {
                    // Parsing error message array
                    val errorBody = response.errorBody()?.string()
                    val errorMessage = errorBody?.let { it ->
                        val jsonObject = JSONObject(it)
                        if (jsonObject.has("error")) {
                            // Mengambil array pesan error dan menggabungkannya menjadi satu string
                            val errorArray = jsonObject.getJSONArray("error")
                            val errors = (0 until errorArray.length()).joinToString("\n") {
                                errorArray.getString(it)
                            }
                            errors  // Mengembalikan semua pesan error
                        } else {
                            jsonObject.optString("message", "Signup failed")  // Default error message
                        }
                    } ?: "An unknown error occurred"

                    _signupResult.value = Result.Error(errorMessage)
                }
            } catch (e: Exception) {
                _signupResult.value = Result.Error(e.toString())
            }
        }
    }

//
//    fun signInWithCustomToken(customToken: String, callback: (String?) -> Unit) {
//        firebaseAuth.signInWithCustomToken(customToken)
//            .addOnCompleteListener { task ->
//                if (task.isSuccessful) {
//                    firebaseAuth.currentUser?.getIdToken(true)
//                        ?.addOnCompleteListener { tokenTask ->
//                            if (tokenTask.isSuccessful) {
//                                val idToken = tokenTask.result?.token
//                                val userName = firebaseAuth.currentUser?.displayName ?: "Unknown"
//                                Log.d("AccessToken", "Bearer $idToken")
//                                Log.d("User Name", "User : $userName")
//                                saveSession(UserModel(firebaseAuth.currentUser?.email ?: "Unknown", idToken.toString(), true))
//                                callback(idToken) // Kembalikan idToken melalui callback
//                            } else {
//                                Log.e("AccessToken", "Error getting ID token: ${tokenTask.exception?.message}")
//                                callback(null)
//                            }
//                        }
//                } else {
//                    Log.e("AccessToken", "Sign in failed: ${task.exception?.message}")
//                    callback(null)
//                }
//            }
//    }

}

