package com.rbrauwers.newsapp.data.serializers

import androidx.datastore.core.Serializer
import com.rbrauwers.newsapp.common.CryptoManager
import com.rbrauwers.newsapp.model.UserSettings
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.Json
import java.io.InputStream
import java.io.OutputStream

class UserSettingsSerializer(
    private val cryptoManager: CryptoManager
) : Serializer<UserSettings> {

    override val defaultValue: UserSettings
        get() = UserSettings()

    override suspend fun readFrom(input: InputStream): UserSettings {
        val decryptedBytes = cryptoManager.decrypt(input)
        return try {
            Json.decodeFromString(
                deserializer = UserSettings.serializer(),
                string = decryptedBytes.decodeToString()
            )
        } catch(e: SerializationException) {
            e.printStackTrace()
            defaultValue
        }
    }

    override suspend fun writeTo(t: UserSettings, output: OutputStream) {
        val json = Json.encodeToString(
            serializer = UserSettings.serializer(),
            value = t
        )

        cryptoManager.encrypt(
            bytes = json.encodeToByteArray(),
            outputStream = output
        )
    }
}