import android.content.Context
import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
//import androidx.datastore.preferences.edit
import androidx.datastore.preferences.preferencesDataStore
//import com.bangkit.dermascan.dataArticles.local.UserData
import com.bangkit.dermascan.data.Pref.UserModel
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
                preferences[EMAIL_KEY] ?: "",
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
