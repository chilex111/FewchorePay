package fewchore.com.exolve.fewchore.listener

interface SaveEncryptionListener {
    fun saveEncryptListener(encryptValue: String, chargeToken: String?, card_id: String, transaction_ref: String, description: String)
}
