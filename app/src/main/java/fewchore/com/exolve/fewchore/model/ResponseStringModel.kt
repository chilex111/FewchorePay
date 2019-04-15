package fewchore.com.exolve.fewchore.model

import com.google.gson.annotations.Expose

class ResponseStringModel {

    @Expose
    var msg: String? = null
    @Expose
    var error_msg: String? = null
    @Expose
    var status: String? = null
    @Expose
    var loan_id: String? = null
    @Expose
    var name: String? = null
    @Expose
    var user_status: String? = null

}
