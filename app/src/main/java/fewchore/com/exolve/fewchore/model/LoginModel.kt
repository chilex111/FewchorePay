package fewchore.com.exolve.fewchore.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class LoginModel {

    @Expose
    var msg: String? = null
    @Expose
    var status: String? = null
    @SerializedName("user_details")
    var userDetails: UserDetails? = null
    @SerializedName("user_image")
    var userImage: String? = null

}
