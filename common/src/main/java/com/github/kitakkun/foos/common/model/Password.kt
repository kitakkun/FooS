package com.github.kitakkun.foos.common.model

data class Password(
    val value: String,
) {
    init {
        require(value.isNotBlank()) { "Password cannot be blank" }
        require(value.length >= 8) { "Password must be at least 8 characters" }
        require(value.length <= 20) { "Password cannot be longer than 20 characters" }
        require(value.matches(Regex("^[a-zA-Z0-9_]*\$"))) { "Password can only contain letters, numbers, and underscores" }
    }
}
