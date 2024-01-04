package com.rbrauwers.newsapp.common

import androidx.biometric.BiometricManager
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.channels.trySendBlocking
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

interface BiometricAuthenticator {

    fun authenticate(
        activity: FragmentActivity,
        title: String = "Authentication required",
        subtitle: String = "Log in using your biometric credential"
    ): Flow<BiometricStatus>

    sealed interface BiometricStatus {
        data object Failure : BiometricStatus
        data object NotRequested : BiometricStatus
        data object NotAvailable : BiometricStatus
        data object Success : BiometricStatus
        class Error(val error: String) : BiometricStatus
    }
}

internal class DefaultBiometricAuthenticator : BiometricAuthenticator {

    override fun authenticate(
        activity: FragmentActivity,
        title: String,
        subtitle: String
    ): Flow<BiometricAuthenticator.BiometricStatus> {
        return callbackFlow {
            val biometricManager = BiometricManager.from(activity)
            val canAuthenticate = biometricManager
                .canAuthenticate(BiometricManager.Authenticators.BIOMETRIC_STRONG) == BiometricManager.BIOMETRIC_SUCCESS

            if (!canAuthenticate) {
                trySendBlocking(BiometricAuthenticator.BiometricStatus.NotAvailable)
                channel.close()
                return@callbackFlow
            }

            val biometricPrompt = BiometricPrompt(
                activity,
                ContextCompat.getMainExecutor(activity),
                object : BiometricPrompt.AuthenticationCallback() {
                    override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                        trySendBlocking(BiometricAuthenticator.BiometricStatus.Success)
                        channel.close()
                    }

                    override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                        trySendBlocking(BiometricAuthenticator.BiometricStatus.Error(errString.toString()))
                        channel.close()
                    }

                    override fun onAuthenticationFailed() {
                        /**
                         * In case of authentication failure we do not close the channel,
                         * because user can try again with same biometric prompt.
                         */
                        trySendBlocking(BiometricAuthenticator.BiometricStatus.Failure)
                    }
                }
            )

            val promptInfo = BiometricPrompt.PromptInfo.Builder()
                .setTitle(title)
                .setSubtitle(subtitle)
                .setAllowedAuthenticators(BiometricManager.Authenticators.BIOMETRIC_STRONG or BiometricManager.Authenticators.DEVICE_CREDENTIAL)
                .build()

            biometricPrompt.authenticate(promptInfo)

            awaitClose {  }
        }
    }

}
