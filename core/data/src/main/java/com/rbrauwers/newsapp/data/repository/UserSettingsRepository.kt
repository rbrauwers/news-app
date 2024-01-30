package com.rbrauwers.newsapp.data.repository

import androidx.datastore.core.DataStore
import com.rbrauwers.newsapp.model.UserSettings
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.withContext
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

interface UserSettingsRepository {
    val flow: Flow<UserSettings>
    suspend fun clear()
    suspend fun load(): UserSettings?
    suspend fun save(userSettings: UserSettings)
}

internal class DefaultUserSettingsRepository @Inject constructor(
    private val dataStore: DataStore<UserSettings>,
    private val coroutineContext: CoroutineContext
) : UserSettingsRepository {

    override val flow: Flow<UserSettings> = dataStore.data

    override suspend fun clear() {
        /**
         * Data store does not have an option to remove data,
         * so we save an empty UserSettings.
         */
        withContext(context = coroutineContext) {
            save(userSettings = UserSettings.Empty)
        }
    }

    override suspend fun load(): UserSettings? {
        return withContext(context = coroutineContext) {
            dataStore.data.firstOrNull()
        }
    }

    override suspend fun save(userSettings: UserSettings) {
        withContext(context = coroutineContext) {
            dataStore.updateData {
                userSettings
            }
        }
    }

}