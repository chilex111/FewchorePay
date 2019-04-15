package fewchore.com.exolve.fewchore.listener

import fewchore.com.exolve.fewchore.model.BankDetail

interface AcctListListener {
    fun acctDetailsListener(bankDetail: List<BankDetail>?, msg: String)
}
