package fewchore.com.exolve.fewchore.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class Customer {

    @SerializedName("customer_code")
    var customerCode: String? = null
    @Expose
    var email: String? = null
    @SerializedName("first_name")
    var firstName: String? = null
    @Expose
    var id: Long? = null
    @SerializedName("last_name")
    var lastName: String? = null

}
