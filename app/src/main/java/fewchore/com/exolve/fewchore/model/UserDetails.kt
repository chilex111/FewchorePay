package fewchore.com.exolve.fewchore.model

import com.google.gson.annotations.SerializedName

class UserDetails {

    @SerializedName("role_title")
    var roleTitle: String? = null
    @SerializedName("status_title")
    var statusTitle: String? = null
    @SerializedName("user_email")
    var userEmail: String? = null
    @SerializedName("user_firstname")
    var userFirstname: String? = null
    @SerializedName("user_id")
    var userId: String? = null
    @SerializedName("user_image")
    var userImage: Any? = null
    @SerializedName("user_lastname")
    var userLastname: String? = null
    @SerializedName("user_loanstatus")
    var userLoanstatus: String? = null
    @SerializedName("user_investmentstatus")
    var userInveststatus: String? = null
    @SerializedName("user_phone")
    var userPhone: String? = null

}
