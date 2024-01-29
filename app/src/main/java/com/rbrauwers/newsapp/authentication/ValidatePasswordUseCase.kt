package com.rbrauwers.newsapp.authentication

internal class ValidatePasswordUseCase {

    fun validate(password: String?, passwordConfirmation: String?): Result {
        val hasMinLength = (password?.length ?: 0) >= passwordMinLength

        if (hasMinLength && password == passwordConfirmation) {
            return Result.Success
        }

        val passwordError: String? = when {
            password.isNullOrEmpty() || hasMinLength -> null
            else -> "Min length is $passwordMinLength"
        }

        val passwordConfirmationError: String? = when {
            passwordConfirmation.isNullOrEmpty() -> null
            passwordConfirmation != password -> "Passwords doesn't matches"
            else -> null
        }

        return Result.Failure(
            passwordError = passwordError,
            passwordConfirmationError = passwordConfirmationError
        )
    }

    sealed interface Result {
        data object Success : Result
        data class Failure(
            val passwordError: String?,
            val passwordConfirmationError: String?
        ) : Result
    }

    companion object {
        private const val passwordMinLength = 8
    }

}