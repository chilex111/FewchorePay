package fewchore.com.exolve.fewchore.model

import com.google.gson.annotations.SerializedName

class Msg {

    @SerializedName("access_code")
    var accessCode: String? = null
    @SerializedName("user_id")
    var userId: String? = null

}
