package com.rbrauwers.newsapp.tests

import com.rbrauwers.newsapp.data.repository.UserSettingsRepository
import com.rbrauwers.newsapp.model.UserSettings
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update

class FakeUserSettingsRepository : UserSettingsRepository {

    override val flow = MutableStateFlow(UserSettings())

    override suspend fun clear() {
        flow.update {
            UserSettings.Empty
        }
    }

    override suspend fun load(): UserSettings? {
        return flow.value
    }

    override suspend fun save(userSettings: UserSettings) {
        flow.update {
            userSettings
        }
    }

}