package com.github.kitakkun.foos.common

import com.github.kitakkun.foos.common.model.InvalidPasswordException
import com.github.kitakkun.foos.common.model.Password
import org.junit.Assert.assertThrows
import org.junit.Test

class PasswordTest {
    @Test
    fun testTooShort() {
        assertThrows(InvalidPasswordException.TooShort::class.java) {
            Password("1".repeat(7))
        }
    }

    @Test
    fun testTooLong() {
        assertThrows(InvalidPasswordException.TooLong::class.java) {
            Password("1".repeat(257))
        }
    }

    @Test
    fun testNoNumericCharacters() {
        assertThrows(InvalidPasswordException.NoNumericCharacter::class.java) {
            Password("a".repeat(8))
        }
    }

    @Test
    fun testNoAlphabetCharacters() {
        assertThrows(InvalidPasswordException.NoAlphabeticCharacter::class.java) {
            Password("1".repeat(8))
        }
    }

    @Test
    fun testInvalidCharacters() {
        assertThrows(InvalidPasswordException.ContainInvalidCharacter::class.java) {
            Password("password123✋")
        }
        assertThrows(InvalidPasswordException.ContainInvalidCharacter::class.java) {
            Password("password123漢字")
        }
        assertThrows(InvalidPasswordException.ContainInvalidCharacter::class.java) {
            Password("password123 space")
        }
    }

    @Test
    fun testValidPasswords() {
        Password("password123")
        Password("password123!")
        Password("password123@")
        Password("password123#")
        Password("password123\$")
        Password("password123%")
        Password("password123^")
        Password("password123&")
        Password("password123*")
        Password("password123(")
        Password("password123)")
        Password("password123_")
        Password("password123+")
        Password("password123=")
        Password("password123[")
        Password("password123]")
        Password("password123{")
        Password("password123}")
        Password("password123|")
        Password("password123\\")
        Password("password123;")
        Password("password123:")
        Password("password123'")
        Password("password123\"")
        Password("password123,")
        Password("password123.")
        Password("password123<")
        Password("password123>")
        Password("password123/")
        Password("password123?")
        Password("password123~")
        Password("password123`")
    }
}
