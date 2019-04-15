package fewchore.com.exolve.fewchore.listener

import fewchore.com.exolve.fewchore.model.Msg

interface CardLoanListener {
    fun accessCodeListener(cardDetails: Msg?, status: Boolean)
}
