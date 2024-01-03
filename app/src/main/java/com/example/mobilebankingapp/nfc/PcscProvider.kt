package com.example.mobilebankingapp.nfc

import android.nfc.tech.IsoDep
import com.github.devnied.emvnfccard.exception.CommunicationException
import com.github.devnied.emvnfccard.parser.IProvider
import java.io.IOException

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