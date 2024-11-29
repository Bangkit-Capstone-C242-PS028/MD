import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
//import androidx.datastore.preferences.edit
import androidx.datastore.preferences.preferencesDataStore
//import com.bangkit.dermascan.dataArticles.local.UserData
import com.bangkit.dermascan.data.local.UserModel
import com.bangkit.dermascan.data.model.response.UserData
import com.google.gson.Gson
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "session")
class UserPreference private constructor(private val dataStore: DataStore<Preferences>) {

    // Save session dataArticles (UserModel)
    suspend fun saveSession(user: UserModel) {
        dataStore.edit { preferences ->
            preferences[NAME_KEY] = user.email
            preferences[TOKEN_KEY] = user.token
            preferences[IS_LOGIN_KEY] = user.isLogin
        }
    }

    // Fungsi untuk mengganti token yang ada
    suspend fun updateToken(newToken: String) {
        dataStore.edit { preferences ->
            preferences[TOKEN_KEY] = newToken // Mengubah nilai token dengan token baru
        }
    }


    // Get token only
    fun getToken(): Flow<String> {
        return dataStore.data.map { preferences ->
            preferences[TOKEN_KEY] ?: ""
        }
    }

    // Get session (UserModel)
    fun getSession(): Flow<UserModel> {
        return dataStore.data.map { preferences ->
            UserModel(
                preferences[NAME_KEY] ?: "",
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

    // Save detailed user dataArticles (UserData)
    suspend fun saveUserData(userData: UserData) {
        val jsonUserData = Gson().toJson(userData) // Serialize UserData to JSON
        dataStore.edit { preferences ->
            preferences[USER_DATA_KEY] = jsonUserData
        }
    }

    // Get detailed user dataArticles (UserData)
    fun getUserData(): Flow<UserData?> {
        return dataStore.data.map { preferences ->
            preferences[USER_DATA_KEY]?.let { json ->
                Gson().fromJson(json, UserData::class.java) // Deserialize JSON to UserData
            }
        }
    }

    // Singleton pattern for UserPreference
    companion object {
        @Volatile
        private var INSTANCE: UserPreference? = null

        // Keys for preferences
        private val NAME_KEY = stringPreferencesKey("name")
        private val TOKEN_KEY = stringPreferencesKey("token")
        private val IS_LOGIN_KEY = booleanPreferencesKey("isLogin")
        private val USER_DATA_KEY = stringPreferencesKey("user_data")

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
