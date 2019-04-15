package fewchore.com.exolve.fewchore.listener

import fewchore.com.exolve.fewchore.model.Activeloan

interface RequestListener {
    fun requestListener(request: Activeloan, status: Boolean?, statusMsg: String)
}
