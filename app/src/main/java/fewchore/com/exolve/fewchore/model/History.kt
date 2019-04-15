package fewchore.com.exolve.fewchore.model

import com.google.gson.annotations.SerializedName

class History {

    @SerializedName("loan_amount")
    var loanAmount: String? = null
    @SerializedName("loan_id")
    var loanId: String? = null
    @SerializedName("loan_interest")
    var loanInterest: String? = null
    @SerializedName("loan_paybackduration")
    var loanPaybackduration: String? = null
    @SerializedName("loan_totalpayback")
    var loanTotalpayback: String? = null
    @SerializedName("status_title")
    var statusTitle: String? = null

}
