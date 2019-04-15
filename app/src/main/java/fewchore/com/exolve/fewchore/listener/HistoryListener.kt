package fewchore.com.exolve.fewchore.listener

import fewchore.com.exolve.fewchore.model.History

interface HistoryListener {
    fun historyListener(history: List<History>?, status: Boolean)
}
