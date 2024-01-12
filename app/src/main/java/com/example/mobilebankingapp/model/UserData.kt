package com.example.mobilebankingapp.model

import com.example.mobilebankingapp.utils.decryptData
import com.example.mobilebankingapp.utils.encryptData
import com.example.mobilebankingapp.utils.getSecretKey

data class UserData(
    val firstName: String? = "",
    val lastName: String? = "",
    val birthDate: String? = "",
    val cnp: String? = "",
    val balance: Map<String, Double> = mapOf("RON" to 0.0),
    val cards: Map<String, CreditCard> = mapOf()
) {
    fun encrypt(keyStoreKey: String): UserData {
        val key = getSecretKey(keyStoreKey)
        return copy(
            firstName = encryptData(firstName!!, key),
            lastName = encryptData(lastName!!, key),
            cnp = encryptData(cnp!!, key),
            birthDate = encryptData(birthDate!!, key)
        )
    }

    fun decrypt(keyStoreKey: String): UserData {
        val key = getSecretKey(keyStoreKey)
        return copy(
            firstName = decryptData(firstName!!, key),
            lastName = decryptData(lastName!!, key),
            cnp = decryptData(cnp!!, key),
            birthDate = decryptData(birthDate!!, key)
        )
    }
}
