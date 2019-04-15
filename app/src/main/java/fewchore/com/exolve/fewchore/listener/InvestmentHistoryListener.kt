package fewchore.com.exolve.fewchore.listener

import fewchore.com.exolve.fewchore.model.Investments

interface InvestmentHistoryListener {
    fun historyListener(history: List<Investments>?, status: Boolean, msg: String?)
}
