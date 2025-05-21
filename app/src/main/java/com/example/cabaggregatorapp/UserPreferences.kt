package com.example.cabaggregatorapp.utils

import android.content.Context
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map


// Extension property for DataStore
val Context.dataStore by preferencesDataStore(name = "user_prefs")

// Preference keys
object UserPrefKeys {
    val EMAIL = stringPreferencesKey("email")
    val IS_LOGGED_IN = booleanPreferencesKey("is_logged_in")
}

class UserPreferences(private val context: Context) {

    val userEmail: Flow<String?> = context.dataStore.data
        .map { preferences -> preferences[UserPrefKeys.EMAIL] }

    val isLoggedIn: Flow<Boolean> = context.dataStore.data
        .map { preferences -> preferences[UserPrefKeys.IS_LOGGED_IN] ?: false }

    suspend fun saveUser(email: String) {
        context.dataStore.edit { preferences ->
            preferences[UserPrefKeys.EMAIL] = email
            preferences[UserPrefKeys.IS_LOGGED_IN] = true
        }
    }

    suspend fun logout() {
        context.dataStore.edit { preferences ->
            preferences.clear()
        }
    }
}

