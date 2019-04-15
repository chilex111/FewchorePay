package fewchore.com.exolve.fewchore.model

import android.os.Parcel
import android.os.Parcelable

class FormModel() : Parcelable {
    override fun writeToParcel(p0: Parcel?, p1: Int) {
        p0!!.writeString(loanAmount)
        p0.writeString(duration)
        p0.writeString(interest)
        p0.writeString(totalPayback)
        p0.writeString(planName)
        p0.writeString(shortDescription)
        p0.writeString(totalInvest)
        p0.writeString(investAmt)
    }

    override fun describeContents(): Int {
        return 0
    }

    var loanAmount: String? = null
    var duration: String? = null
    var interest: String? = null
    var totalPayback: String?= null
    var planName: String? = null
    var shortDescription: String? = null
    var totalInvest: String?= null
    var investAmt: String ?= null

    constructor(parcel: Parcel) : this() {
        loanAmount = parcel.readString()
        duration = parcel.readString()
        interest = parcel.readString()
        totalPayback = parcel.readString()
        planName = parcel.readString()
        duration = parcel.readString()
        shortDescription = parcel.readString()
        totalInvest = parcel.readString()
        investAmt = parcel.readString()

    }

    companion object CREATOR : Parcelable.Creator<FormModel> {
        override fun createFromParcel(parcel: Parcel): FormModel {
            return FormModel(parcel)
        }

        override fun newArray(size: Int): Array<FormModel?> {
            return arrayOfNulls(size)
        }
    }

}
