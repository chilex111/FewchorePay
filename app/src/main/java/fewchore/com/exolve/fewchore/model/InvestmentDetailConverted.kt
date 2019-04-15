package fewchore.com.exolve.fewchore.model

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

class InvestmentDetailConverted(): Parcelable {
     var bankName: String? = null
     var investmentAmount: String? = null
     var investmentCreated: String? = null
     var investmentDuedate: String? = null
     var investmentDuration: String? = null
     var investmentId: String? = null
     var investmentInterest: String? = null
     var investmentName: String? = null
     var investmentTotal: String? = null
     var statusTitle: String? = null
     var userbankAccno: String? = null

    constructor(parcel: Parcel) : this() {
        bankName = parcel.readString()
        investmentAmount = parcel.readString()
        investmentCreated = parcel.readString()
        investmentDuedate = parcel.readString()
        investmentDuration = parcel.readString()
        investmentId = parcel.readString()
        investmentInterest = parcel.readString()
        investmentName = parcel.readString()
        investmentTotal = parcel.readString()
        statusTitle = parcel.readString()
        userbankAccno = parcel.readString()
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(bankName)
        parcel.writeString(investmentAmount)
        parcel.writeString(investmentCreated)
        parcel.writeString(investmentDuedate)
        parcel.writeString(investmentDuration)
        parcel.writeString(investmentId)
        parcel.writeString(investmentInterest)
        parcel.writeString(investmentName)
        parcel.writeString(investmentTotal)
        parcel.writeString(statusTitle)
        parcel.writeString(userbankAccno)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<InvestmentDetailConverted> {
        override fun createFromParcel(parcel: Parcel): InvestmentDetailConverted {
            return InvestmentDetailConverted(parcel)
        }

        override fun newArray(size: Int): Array<InvestmentDetailConverted?> {
            return arrayOfNulls(size)
        }
    }
}