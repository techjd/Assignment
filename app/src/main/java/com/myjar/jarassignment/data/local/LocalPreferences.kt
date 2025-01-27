package com.myjar.jarassignment.data.local

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")
val computerData = stringPreferencesKey("local_computer_data")

class LocalPreferences(
    private val context: Context
) {

    fun getComputerData(): Flow<String?> {
        return context.dataStore.data
            .map { preferences ->
                // No type safety.
                preferences[computerData]
            }
    }

    suspend fun saveComputerData(data: String) {
        context.dataStore.edit { settings ->
            settings[computerData] = data
        }
    }
}