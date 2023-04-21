package com.github.kitakkun.foos.common.model

import com.github.michaelbull.result.Err
import com.github.michaelbull.result.Ok
import com.github.michaelbull.result.Result

data class Password(
    val value: String,
) {
    companion object {
        const val MAX_LENGTH = 256
        const val MIN_LENGTH = 8

        const val availableCharacters =
            "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz" +
                    "0123456789" +
                    "!\"#\$%&'()*-^\\@\\[;:\\\\],./=~|`{+*}<>?_"

        fun validate(value: String): Result<Password, Exception> {
            return try {
                val password = Password(value)
                Ok(password)
            } catch (e: Exception) {
                Err(e)
            }
        }
    }

    init {
        if (value.isBlank()) throw InvalidPasswordException.Blank()
        if (value.length < MIN_LENGTH) throw InvalidPasswordException.TooShort()
        if (value.length > MAX_LENGTH) throw InvalidPasswordException.TooLong()
        if (!value.contains(Regex("\\d"))) throw InvalidPasswordException.NoNumericCharacter()
        if (!value.contains(Regex("[a-zA-Z]"))) throw InvalidPasswordException.NoAlphabeticCharacter()
        if (value.any { it !in availableCharacters }) throw InvalidPasswordException.ContainInvalidCharacter()
    }
}

sealed class InvalidPasswordException(message: String) : Exception(message) {
    class Blank : InvalidPasswordException(
        message = "Password cannot be blank"
    )

    class TooShort : InvalidPasswordException(
        message = "Password must be at least ${Password.MIN_LENGTH} characters"
    )

    class TooLong : InvalidPasswordException(
        message = "Password cannot be longer than ${Password.MAX_LENGTH} characters"
    )

    class ContainInvalidCharacter : InvalidPasswordException(
        "Password can only contain alphabets, numbers, and symbols(!\"#\$%&'()*-^\\@\\[;:\\\\],./=~|`{+*}<>?_)"
    )

    class NoNumericCharacter : InvalidPasswordException(
        "Password must contain at least one number"
    )

    class NoAlphabeticCharacter : InvalidPasswordException(
        "Password must contain at least one alphabetic character"
    )
}
