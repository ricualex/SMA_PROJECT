package com.example.mobilebankingapp.ui.screens.cards

import android.app.Activity
import android.nfc.NfcAdapter
import android.nfc.Tag
import android.nfc.tech.IsoDep
import android.os.Bundle
import com.github.devnied.emvnfccard.exception.CommunicationException
import com.github.devnied.emvnfccard.model.EmvCard
import com.github.devnied.emvnfccard.parser.EmvTemplate
import com.github.devnied.emvnfccard.parser.IProvider
import java.io.IOException

class CardReaderCallback(
    private val activity: Activity,
    private val onSuccess: (EmvCard) -> Unit,
    private val onFailure: () -> Unit
) :
    NfcAdapter.ReaderCallback {
    private val nfcAdapter = NfcAdapter.getDefaultAdapter(activity)

    init {
        val options = Bundle()
        options.putInt(NfcAdapter.EXTRA_READER_PRESENCE_CHECK_DELAY, 150)

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
                onSuccess(card)
            } ?: throw CommunicationException("Failed to get a connection")
        } catch (e: CommunicationException) {
            onFailure()
        } finally {
            nfcAdapter.disableReaderMode(activity)
        }
    }
}

class PcscProvider(private val tag: IsoDep) : IProvider {
    override fun transceive(pCommand: ByteArray?): ByteArray {
        return try {
            tag.transceive(pCommand)
        } catch (e: IOException) {
            throw CommunicationException(e.message)
        }
    }

    override fun getAt(): ByteArray {
        return tag.historicalBytes
    }
}

