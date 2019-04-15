package fewchore.com.exolve.fewchore.listener

import fewchore.com.exolve.fewchore.model.InvestmentDetails

interface SingleInvestmentListener {
    fun singleInvestmentListener(investmentDetails: MutableList<InvestmentDetails>?, status: Boolean, msg: String?)

}