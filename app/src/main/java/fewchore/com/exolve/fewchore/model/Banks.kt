package fewchore.com.exolve.fewchore.model

import com.google.gson.annotations.SerializedName

class Banks {

    @SerializedName("bank_created")
    var bankCreated: String? = null
    @SerializedName("bank_id")
    var bankId: String? = null
    @SerializedName("bank_modified")
    var bankModified: String? = null
    @SerializedName("bank_name")
    var bankName: String? = null
    @SerializedName("bank_slug")
    var bankSlug: String? = null

}
