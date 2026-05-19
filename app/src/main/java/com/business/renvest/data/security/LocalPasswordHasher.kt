package com.business.renvest.data.security

import java.security.MessageDigest
import java.security.SecureRandom
import java.util.Base64

/** Local-only credential hashing for device unlock (not server auth). */
object LocalPasswordHasher {

    fun generateSalt(): String {
        val bytes = ByteArray(16)
        SecureRandom().nextBytes(bytes)
        return Base64.getEncoder().encodeToString(bytes)
    }

    fun hash(password: String, salt: String): String {
        val digest = MessageDigest.getInstance("SHA-256")
        val input = "$salt:$password".toByteArray(Charsets.UTF_8)
        return Base64.getEncoder().encodeToString(digest.digest(input))
    }

    fun verify(password: String, salt: String, expectedHash: String): Boolean =
        hash(password, salt) == expectedHash
}
