package fewchore.com.exolve.fewchore.listener

interface StatusListener {
    fun statusListener(status_msg: String, status: Boolean?, user_status: String?)
}
