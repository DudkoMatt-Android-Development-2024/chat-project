import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.firstOrNull

object SessionTokenManager {

    private const val DATA_STORE_NAME = "session_token"
    private const val TOKEN_KEY = "token"

    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = DATA_STORE_NAME)

    suspend fun storeToken(context: Context, token: String) {
        context.dataStore.edit { preferences ->
            preferences[stringPreferencesKey(TOKEN_KEY)] = token
        }
    }

    suspend fun getToken(context: Context): String? {
        val preferences = context.dataStore.data.firstOrNull()
        return preferences?.get(stringPreferencesKey(TOKEN_KEY))
    }
}
