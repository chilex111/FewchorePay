package fewchore.com.exolve.fewchore.listener

import fewchore.com.exolve.fewchore.model.CardDetails

interface CardListListener {
    fun cardDetailsListener(cardDetails: MutableList<CardDetails>?, msg: String)
}
