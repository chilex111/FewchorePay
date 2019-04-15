package fewchore.com.exolve.fewchore.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class Authorization {

    @SerializedName("authorization_code")
    var authorizationCode: String? = null
    @Expose
    var bank: String? = null
    @Expose
    var bin: String? = null
    @Expose
    var brand: String? = null
    @SerializedName("card_type")
    var cardType: String? = null
    @Expose
    var channel: String? = null
    @SerializedName("country_code")
    var countryCode: String? = null
    @SerializedName("exp_month")
    var expMonth: String? = null
    @SerializedName("exp_year")
    var expYear: String? = null
    @Expose
    var last4: String? = null
    @Expose
    var reusable: Boolean? = null
    @Expose
    var signature: String? = null

}
