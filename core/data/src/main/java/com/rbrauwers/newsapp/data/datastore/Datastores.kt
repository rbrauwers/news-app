package com.rbrauwers.newsapp.data.datastore

import android.content.Context
import androidx.datastore.dataStore
import com.rbrauwers.newsapp.common.CryptoManager
import com.rbrauwers.newsapp.data.serializers.UserSettingsSerializer

val Context.userSettingsDataStore by dataStore(
    fileName = "user-settings.json",
    serializer = UserSettingsSerializer(CryptoManager())
)

