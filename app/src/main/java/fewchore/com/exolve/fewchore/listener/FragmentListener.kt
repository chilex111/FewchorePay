package fewchore.com.exolve.fewchore.listener

import fewchore.com.exolve.fewchore.enums.NavigationDirection
import fewchore.com.exolve.fewchore.model.BankModel
import fewchore.com.exolve.fewchore.model.CardModel
import fewchore.com.exolve.fewchore.model.FormModel
import fewchore.com.exolve.fewchore.model.PasswordModel

interface FragmentListener {
    fun onFragmentNavigation(navigationDirection: NavigationDirection)

    fun onFormDetailSubmit(formModel: FormModel)

    fun onBankDetailSubmit(bankModel: BankModel)

    fun onCardDetailSubmit(cardModel: CardModel)

    fun onLookUp()

    fun onPasswordDetailSubmit(passwordModel: PasswordModel)

}
