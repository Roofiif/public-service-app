package com.dxid.publicservice.utils

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class PreferencesManager @Inject constructor(@ApplicationContext context: Context)  {
    private val Context.dataStore by preferencesDataStore(name = "settings")

    private val LAST_SYNC_TIME = longPreferencesKey("last_sync_time")

    private val dataStore = context.dataStore

    suspend fun setLastSyncTime(time: Long) {
        dataStore.edit { preferences ->
            preferences[LAST_SYNC_TIME] = time
        }
    }

    suspend fun getLastSyncTime(): Long {
        val preferences = dataStore.data.first()
        return preferences[LAST_SYNC_TIME] ?: 0L
    }
}