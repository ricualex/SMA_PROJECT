package com.example.mobilebankingapp.ui.screens.cards

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.mobilebankingapp.data.FirebaseRepository
import com.example.mobilebankingapp.model.CardIssuer
import com.example.mobilebankingapp.model.CreditCard
import com.github.devnied.emvnfccard.model.EmvCard
import java.text.SimpleDateFormat
import java.time.format.DateTimeFormatter
import java.util.Locale

val DATE_PATTERN = Regex("^(0[1-9]|1[0-2])[2-9][0-9]$")

class CreditCardViewModel : ViewModel() {
    val creditCard = mutableStateOf(CreditCard())
    val isDisplayed = mutableStateOf(false)
    var identityConfirmed = false

    fun updateCreditCard(emvCard: EmvCard) {
        println(emvCard)
        val expDate = SimpleDateFormat("MMyy", Locale.getDefault())
            .format(emvCard.expireDate)

        creditCard.value =
            creditCard.value.copy(
                holderName = "${emvCard.holderFirstname ?: ""} ${emvCard.holderLastname ?: ""}".trim(),
                panNumber = emvCard.cardNumber,
                expirationDate = expDate
            )
    }

    fun updatePanNumber(panNumber: String) {
        if (panNumber.length in 0..19) {
            val issuer = when {
                isMasterCard() -> CardIssuer.MasterCard
                isVisa() -> CardIssuer.Visa
                else -> CardIssuer.Unknown
            }

            creditCard.value = creditCard.value.copy(panNumber = panNumber, issuer = issuer)
        }
    }

    fun updateCvv(cvv: String) {
        if (cvv.length <= 3) {
            creditCard.value = creditCard.value.copy(cvv = cvv)
        }
    }

    fun updateHolderName(holderName: String) {
        if (holderName.length <= 20) {
            creditCard.value = creditCard.value.copy(holderName = holderName)
        }
    }

    fun updateExpirationDate(date: String) {
        if (date.length <= 4) {
            creditCard.value = creditCard.value.copy(expirationDate = date)
        }
    }

    fun updateDefault(default: Boolean) {
        creditCard.value = creditCard.value.copy(default = default)
    }

    fun toggleDisplay() {
        if (identityConfirmed) {
            isDisplayed.value = isDisplayed.value.not()
        }
    }

    fun isPanNumberValid() = creditCard.value.panNumber.length in 8..19 &&
            // Luhn algo
            creditCard.value.panNumber.reversed().mapIndexed { i, v ->
                if ((i + 1) % 2 == 0) {
                    val num = v.digitToInt() * 2
                    num / 10 + num % 10
                } else {
                    v.digitToInt()
                }
            }.sum() % 10 == 0 && (isMasterCard() || isVisa())

    private fun isMasterCard() = creditCard.value.panNumber.take(4).toIntOrNull() in 2221..2720
            || creditCard.value.panNumber.take(2).toIntOrNull() in 51..55

    private fun isVisa() = creditCard.value.panNumber.take(1) == "4"

    fun isExpirationDateValid() = DATE_PATTERN.matches(creditCard.value.expirationDate)

    fun isCvvValid() = creditCard.value.cvv.length == 3

    fun isCreditCardValid() = isExpirationDateValid()
            && isPanNumberValid()
            && isCvvValid()
            && creditCard.value.holderName.isNotBlank()

    fun resetState() {
        creditCard.value = CreditCard()
    }
}