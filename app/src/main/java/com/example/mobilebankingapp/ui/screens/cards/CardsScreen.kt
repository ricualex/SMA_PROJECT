package com.example.mobilebankingapp.ui.screens.cards

import android.app.Activity
import android.nfc.NfcAdapter
import android.nfc.NfcAdapter.ReaderCallback
import android.nfc.Tag
import android.nfc.tech.IsoDep
import android.os.Bundle
import android.util.Log
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.example.mobilebankingapp.nfc.PcscProvider
import com.github.devnied.emvnfccard.exception.CommunicationException
import com.github.devnied.emvnfccard.parser.EmvTemplate
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.runBlocking
import java.io.Closeable

@Composable
fun CardsScreen() {
}

@Composable
fun RegisterCard(activity: Activity, modifier: Modifier = Modifier) {
    Button(onClick = {
        CardReaderCallback(activity).use {
            println(it.getResult())
        }
    }) {
        Text(text = "Test")
    }
}

sealed interface CardReaderState {
    data class Failure(val msg: String) : CardReaderState
    data class Success(val cardNumber: String) : CardReaderState
}


class CardReaderCallback(private val activity: Activity) : ReaderCallback, Closeable {
    private val nfcAdapter = NfcAdapter.getDefaultAdapter(activity)

    private val channel = Channel<CardReaderState>() {
    }


    init {
        val options = Bundle()
        options.putInt(NfcAdapter.EXTRA_READER_PRESENCE_CHECK_DELAY, 250)

        nfcAdapter.enableReaderMode(
            activity,
            this,
            NfcAdapter.FLAG_READER_NFC_A or
                    NfcAdapter.FLAG_READER_NFC_B or
                    NfcAdapter.FLAG_READER_NFC_F or
                    NfcAdapter.FLAG_READER_NFC_V or
                    NfcAdapter.FLAG_READER_NFC_BARCODE or
                    NfcAdapter.FLAG_READER_NO_PLATFORM_SOUNDS,
            options
        )
    }

    override fun onTagDiscovered(tag: Tag?) {
        try {
            IsoDep.get(tag)?.use {
                it.connect()
                val config = EmvTemplate.Config()
                    .setContactLess(true)
                    .setReadAllAids(true)
                    .setReadTransactions(true)
                    .setRemoveDefaultParsers(false)
                    .setReadAt(true)

                val provider = PcscProvider(it)
                val parser = EmvTemplate.Builder()
                    .setProvider(provider)
                    .setConfig(config)
                    .build()

                val card = parser.readEmvCard()
                runBlocking {
                    channel.send(CardReaderState.Success(card.toString()))
                }
            }
                ?: throw RuntimeException()
        } catch (e: CommunicationException) {
            runBlocking {
                channel.send(
                    CardReaderState.Failure(
                        e.message ?: "No card found, make sure you hold the card still"
                    )
                )
            }
        }
    }

    override fun close() {
        nfcAdapter.disableReaderMode(activity)
        Log.d("Card", "disable reader mode")
    }

    fun getResult() = runBlocking { channel.receive() }
}
