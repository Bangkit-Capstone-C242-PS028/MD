import android.content.Context
import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
//import androidx.datastore.preferences.edit
import androidx.datastore.preferences.preferencesDataStore
//import com.bangkit.dermascan.dataArticles.local.UserData
import com.bangkit.dermascan.data.pref.UserModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "session")
class UserPreference private constructor(private val dataStore: DataStore<Preferences>) {

    suspend fun saveSession(user: UserModel) {
        dataStore.edit { preferences ->
            preferences[UID_KEY] = user.uid ?: ""
            preferences[TOKEN_KEY] = user.token ?: ""
            preferences[IS_LOGIN_KEY] = user.isLogin
        }
    }

    suspend fun saverUpdate(user: UserModel){
        dataStore.edit { preferences ->
            preferences[FIRST_NAME_KEY] = user.firstName ?: ""
            preferences[LAST_NAME_KEY] = user.lastName ?: ""
            preferences[ADDRESS_KEY] = user.address ?: ""
            preferences[IMAGE_URL_KEY] = user.profileImageUrl ?: ""

        }
        Log.d("DataStore", "data saved successfully: $user")
    }

    // Save user data (personal info like name, dob, etc.)
    suspend fun saveUserData(user: UserModel) {
        dataStore.edit { preferences ->
            preferences[FIRST_NAME_KEY] = user.firstName ?: ""
            preferences[LAST_NAME_KEY] = user.lastName ?: ""
            preferences[ROLE_KEY] = user.role ?: ""
            preferences[DOB_KEY] = user.dob ?: ""
            preferences[ADDRESS_KEY] = user.address ?: ""
            preferences[SPECIALIZATION_KEY] = user.specialization ?: ""
            preferences[WORKPLACE_KEY] = user.workplace ?: ""
            preferences[EMAIL_KEY] = user.email ?: ""
            preferences[POINT_KEY] = user.point ?: 0
            preferences[IMAGE_URL_KEY] = user.profileImageUrl ?: ""
            Log.d("DataStore", "data saved successfully: $user")
        }
    }

    // Fungsi untuk mengganti token yang ada
    suspend fun updateToken(newToken: String) {
        dataStore.edit { preferences ->
            preferences[TOKEN_KEY] = newToken // Mengubah nilai token dengan token baru
            Log.d("DataStore", "Token saved successfully: $newToken")
        }
    }



    // Get token only
    fun getToken(): Flow<String> {
        return dataStore.data.map { preferences ->
            preferences[TOKEN_KEY] ?: ""
        }
    }

    fun getRoles(): Flow<String> {
        return dataStore.data.map { preferences ->
            preferences[ROLE_KEY] ?: ""
        }
    }

    fun getPoints(): Flow<Int> {
        return dataStore.data.map { preferences ->
            preferences[POINT_KEY] ?: 0
        }
    }

    // Get session (UserModel)
    fun getSession(): Flow<UserModel> {
        return dataStore.data.map { preferences ->
            UserModel(
                preferences[UID_KEY] ?: "",
                preferences[FIRST_NAME_KEY] ?: "",
                preferences[LAST_NAME_KEY] ?: "",
                preferences[ROLE_KEY] ?: "",
                preferences[DOB_KEY] ?: "",
                preferences[ADDRESS_KEY] ?: "",
                preferences[SPECIALIZATION_KEY] ?: "",
                preferences[POINT_KEY] ?: 0,
                preferences[EMAIL_KEY] ?: "",
                preferences[IMAGE_URL_KEY] ?: "",
                preferences[WORKPLACE_KEY] ?: "",
                preferences[TOKEN_KEY] ?: "",
                preferences[IS_LOGIN_KEY] ?: false,
            )
        }
    }

    // Clear session (Logout)
    suspend fun logout() {
        dataStore.edit { preferences ->
            preferences.clear()
        }
    }

    // Save Data
    suspend fun updateUserDetail(firstName: String, lastName: String, address: String, imageUrl: String) {
        dataStore.edit { preferences ->
            preferences[FIRST_NAME_KEY] = firstName
            preferences[LAST_NAME_KEY] = lastName
            preferences[ADDRESS_KEY] = address
            preferences[IMAGE_URL_KEY] = imageUrl
        }
    }

    // Get First Name
    val firstName: Flow<String?> = dataStore.data.map { preferences ->
        preferences[FIRST_NAME_KEY]
    }

    // Get Last Name
    val lastName: Flow<String?> = dataStore.data.map { preferences ->
        preferences[LAST_NAME_KEY]
    }

    // Get Address
    val address: Flow<String?> = dataStore.data.map { preferences ->
        preferences[ADDRESS_KEY]
    }

    // Get Image URL
    val imageUrl: Flow<String?> = dataStore.data.map { preferences ->
        preferences[IMAGE_URL_KEY]
    }

    // Singleton pattern for UserPreference
    companion object {
        @Volatile
        private var INSTANCE: UserPreference? = null

        // Keys for preferences
        private val EMAIL_KEY = stringPreferencesKey("name")
        private val TOKEN_KEY = stringPreferencesKey("token")
        private val IS_LOGIN_KEY = booleanPreferencesKey("isLogin")
        private val UID_KEY = stringPreferencesKey("uid")
        private val FIRST_NAME_KEY = stringPreferencesKey("firstName")
        private val LAST_NAME_KEY = stringPreferencesKey("lastName")
        private val ROLE_KEY = stringPreferencesKey("role")
        private val DOB_KEY = stringPreferencesKey("dob")
        private val ADDRESS_KEY = stringPreferencesKey("address")
        private val SPECIALIZATION_KEY = stringPreferencesKey("specialization")
        private val WORKPLACE_KEY = stringPreferencesKey("workplace")
        private val IMAGE_URL_KEY = stringPreferencesKey("imageUrl")
        private val POINT_KEY = intPreferencesKey("points")


//        private val USER_DATA_KEY = stringPreferencesKey("user_data")

        // Get an instance of UserPreference
        fun getInstance(dataStore: DataStore<Preferences>): UserPreference {
            return INSTANCE ?: synchronized(this) {
                val instance = UserPreference(dataStore)
                INSTANCE = instance
                instance
            }
        }
    }
}
