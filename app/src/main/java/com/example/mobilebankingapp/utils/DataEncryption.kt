package com.example.mobilebankingapp.utils

import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import android.util.Base64
import java.security.KeyStore
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey
import javax.crypto.spec.GCMParameterSpec


fun encryptData(plainText: String, secretKey: SecretKey): String {
    val cipher = Cipher.getInstance("AES/GCM/NoPadding")
    cipher.init(Cipher.ENCRYPT_MODE, secretKey)
    val iv = cipher.iv
    val encryptedBytes = cipher.doFinal(plainText.toByteArray())
    return Base64.encodeToString(encryptedBytes, Base64.DEFAULT) + ":" + Base64.encodeToString(
        iv,
        Base64.DEFAULT
    )
}

fun decryptData(encryptedText: String, secretKey: SecretKey): String {
    val parts = encryptedText.split(":")
    val encryptedData = Base64.decode(parts[0], Base64.DEFAULT)
    val iv = Base64.decode(parts[1], Base64.DEFAULT)

    val cipher = Cipher.getInstance("AES/GCM/NoPadding")
    val gcmParameterSpec = GCMParameterSpec(128, iv)
    cipher.init(Cipher.DECRYPT_MODE, secretKey, gcmParameterSpec)
    val decryptedBytes = cipher.doFinal(encryptedData)
    return String(decryptedBytes)
}

fun getSecretKey(keyAlias: String): SecretKey {
    val keyStore = KeyStore.getInstance("AndroidKeyStore")
    keyStore.load(null)

    if (!keyStore.containsAlias(keyAlias)) {
        val keyGenerator =
            KeyGenerator.getInstance(KeyProperties.KEY_ALGORITHM_AES, "AndroidKeyStore")

        val builder = KeyGenParameterSpec.Builder(
            keyAlias,
            KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT
        )
            .setKeySize(256)
            .setBlockModes(KeyProperties.BLOCK_MODE_GCM)
            .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_NONE)
            .setRandomizedEncryptionRequired(true)

        keyGenerator.init(builder.build())
        keyGenerator.generateKey()
    }

    return keyStore.getKey(keyAlias, null) as SecretKey
}