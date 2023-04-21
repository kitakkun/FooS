package com.github.kitakkun.foos.common.model.auth

import com.github.michaelbull.result.Err
import com.github.michaelbull.result.Ok
import com.github.michaelbull.result.Result

data class Email(
    val value: String,
) {
    companion object {
        val EMAIL_REGEX = Regex("^[a-zA-Z0-9_.+-]+@[a-zA-Z0-9-]+\\.[a-zA-Z0-9-.]+\$")

        fun validate(value: String): Result<Email, Exception> {
            return try {
                val email = Email(value)
                Ok(email)
            } catch (e: Exception) {
                Err(e)
            }
        }
    }

    init {
        if (value.isBlank()) throw InvalidEmailException.Blank()
        if (!value.matches(EMAIL_REGEX)) throw InvalidEmailException.InvalidFormat()
    }

}

sealed class InvalidEmailException(message: String) : Exception(message) {
    class Blank : InvalidEmailException("Email cannot be blank")
    class InvalidFormat : InvalidEmailException("Email must be a valid email address")
}
