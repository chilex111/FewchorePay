package fewchore.com.exolve.fewchore.listener

import android.text.Editable
import fewchore.com.exolve.fewchore.enums.CardValidity


interface PayStackCardValidationListener {
    fun afterChange(cardValidity: CardValidity, editable: Editable)

    fun paramIsValid(z: Boolean, cardValidity: CardValidity)
}
