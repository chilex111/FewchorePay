package fewchore.com.exolve.fewchore.helper

import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import co.paystack.android.model.Card
import fewchore.com.exolve.fewchore.enums.CardValidity
import fewchore.com.exolve.fewchore.listener.PayStackCardValidationListener
import fewchore.com.exolve.fewchore.listener.TextWatch
import java.util.*

class CreditCardFormatter(private val cardValidity: CardValidity, private val listener: PayStackCardValidationListener, private val editText: EditText, private var card: Card?, private val digits: Int) : TextWatcher, TextWatch {
    private var charCount = 1
    private val watch: TextWatch

    init {
        this.watch = this
    }

    override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}

    override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
        when (this.cardValidity) {
            CardValidity.EXPIRY_DATE -> {
                this.watch.onChange(count)
                return
            }
            else -> {
            }
        }
    }

    override fun afterTextChanged(s: Editable) {

        when (cardValidity) {
            CardValidity.CARD_NO -> {
                // Remove all spacing char
                var pos = 0
                while (true) {
                    if (pos >= s.length) break
                    if (space == s[pos] && ((pos + 1) % 5 != 0 || pos + 1 == s.length)) {
                        s.delete(pos, pos + 1)
                    } else {
                        pos++
                    }
                }

                // Insert char where needed.
                pos = 4
                while (true) {
                    if (pos >= s.length) break
                    val c = s[pos]
                    // Only if its a digit where there should be a space we insert a space
                    if ("0123456789".indexOf(c) >= 0) {
                        s.insert(pos, "" + space)
                    }
                    pos += 5
                }
                val editableString = s.toString()
                var firstTwo: String? = null
                if (editableString.length >= 2) {
                    firstTwo = editableString.substring(0, 2)
                }
                if (firstTwo != null && firstTwo == "50") {
                    if (s.length == digits || s.length == 23) {
                        val `val` = s.toString().trim { it <= ' ' }
                        val cardNo = `val`.replace(" ", "")
                        card = Card.Builder(cardNo, 0, 0, "").build()
                        val isValid = card!!.validNumber()
                        listener.paramIsValid(isValid, CardValidity.CARD_NO)

                        if (isValid) {

                            editText.requestFocus()
                        }
                    }
                } else {
                    if (s.length == digits || s.length == 19) {
                        val `val` = s.toString().trim { it <= ' ' }
                        val cardNo = `val`.replace(" ", "")
                        card = Card.Builder(cardNo, 0, 0, "").build()
                        val isValid = card!!.validNumber()
                        listener.paramIsValid(isValid, CardValidity.CARD_NO)

                        if (isValid) {

                            editText.requestFocus()
                        }
                    }
                }

            }

            CardValidity.CVV -> if (s.length == digits) {
                val `val` = s.toString()
                card!!.cvc = `val`

                val isValid = card!!.validCVC()
                listener.paramIsValid(isValid, CardValidity.CVV)

                if (isValid) {

                    editText.requestFocus()
                }
            }

            CardValidity.EXPIRY_DATE -> {
                listener.afterChange(CardValidity.EXPIRY_DATE, s)
                if (s.length == 2 && charCount != 0) {

                    val sMonth = s.toString().trim { it <= ' ' }
                    var month = -1
                    try {
                        month = Integer.parseInt(sMonth)
                    } catch (ignored: Exception) {
                    }

                    if (month in 1..12) {
                        s.insert(2, "" + forwardSlash)
                    }

                }
                if (s.length == 2 && charCount == 0) {
                    s.delete(1, 2)
                }
                if (s.length == 5) {

                    val date = s.toString()
                    if (date.contains("/")) {
                        val rawYear = Calendar.getInstance().get(Calendar.YEAR).toString()
                        val yearPrefix = rawYear.substring(0, 2)
                        val monthYear = date.split("/".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
                        val monthStr = monthYear[0]
                        val yearStr = yearPrefix + monthYear[1]

                        var month = -1
                        try {
                            month = Integer.parseInt(monthStr)
                        } catch (ignored: Exception) {
                        }

                        if (month in 1..12) {
                            card!!.expiryMonth = month
                        }

                        var year = -1
                        try {
                            year = Integer.parseInt(yearStr)
                        } catch (ignored: Exception) {
                        }

                        if (year > 0) {
                            card!!.expiryYear = year
                        }

                        val isValid = card!!.validExpiryDate()
                        listener.paramIsValid(isValid, CardValidity.EXPIRY_DATE)

                        if (isValid) {
                            editText.requestFocus()
                        }
                    }
                }
            }
        }
    }

    override fun onChange(i: Int) {
        this.charCount = i
    }

    companion object {
        private const val forwardSlash = '/'
        private const val space = ' '
    }
}


