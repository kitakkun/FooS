package com.github.kitakkun.foos.user.auth

enum class PasswordError {
    BLANK,
    TOO_SHORT,
    TOO_LONG,
    NO_NUMERIC_CHARACTER,
    NO_ALPHABETIC_CHARACTER,
    CONTAIN_INVALID_CHARACTER,
}
