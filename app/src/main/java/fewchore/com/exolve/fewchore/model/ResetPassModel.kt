package fewchore.com.exolve.fewchore.model

import com.google.gson.annotations.Expose

class ResetPassModel {

    @Expose
    var code: Long? = null
    @Expose
    var email: String? = null
    @Expose
    var msg: String? = null
    @Expose
    var name: String? = null
    @Expose
    var status: Boolean? = null
    @Expose
    var subject: String? = null

}
