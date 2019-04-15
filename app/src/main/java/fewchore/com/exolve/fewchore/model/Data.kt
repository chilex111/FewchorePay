package fewchore.com.exolve.fewchore.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class Data {

    @Expose
    var amount: Long? = null
    @Expose
    var authorization: Authorization? = null
    @Expose
    var channel: String? = null
    @Expose
    var currency: String? = null
    @Expose
    var customer: Customer? = null
    @Expose
    var domain: String? = null
    @Expose
    var fees: Any? = null
    @SerializedName("gateway_response")
    var gatewayResponse: String? = null
    @SerializedName("ip_address")
    var ipAddress: Any? = null
    @Expose
    var log: Any? = null
    @Expose
    var message: Any? = null
    @Expose
    var metadata: Any? = null
    @Expose
    var plan: Long? = null
    @Expose
    var reference: String? = null
    @Expose
    var status: String? = null
    @SerializedName("transaction_date")
    var transactionDate: String? = null

}
