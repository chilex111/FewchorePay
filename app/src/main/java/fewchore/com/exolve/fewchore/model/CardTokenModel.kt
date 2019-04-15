package fewchore.com.exolve.fewchore.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class CardTokenModel {

    @SerializedName("card_details")
    var cardDetails: CardDetails0? = null
    @SerializedName("card_id")
    var cardId: String? = null
    @Expose
    var msg: String? = null
    @Expose
    var status: String? = null

}
