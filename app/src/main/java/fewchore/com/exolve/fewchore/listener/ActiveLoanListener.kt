package fewchore.com.exolve.fewchore.listener

import fewchore.com.exolve.fewchore.model.Activeloan

interface ActiveLoanListener {
    fun activeLoanListener(activeLoan: Activeloan?, msg: String?, status: Boolean)
}
