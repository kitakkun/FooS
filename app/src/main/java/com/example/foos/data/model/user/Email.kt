package com.example.foos.data.model.user

data class Email(
    val value: String,
) {
    init {
        require(value.isNotBlank()) { "Email cannot be blank" }
        require(value.length <= 50) { "Email cannot be longer than 50 characters" }
        require(value.matches(Regex("^[a-zA-Z0-9_.+-]+@[a-zA-Z0-9-]+\\.[a-zA-Z0-9-.]+\$"))) { "Email must be a valid email address" }
    }
}
