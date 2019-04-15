package fewchore.com.exolve.fewchore.model

import android.os.Parcel
import android.os.Parcelable

class BankModel() : Parcelable {
    override fun writeToParcel(p0: Parcel?, p1: Int) {
        p0!!.writeString(bankName)
        p0.writeString(acctNo)
        p0.writeString(acctType)
        p0.writeString(bvn)
        p0.writeString(acctName)
    }

    override fun describeContents(): Int {
        return 0
    }

    var bankName: String? = null
    var acctNo: String? = null
    var acctType: String? = null
    var bvn: String? = null
    var acctName: String? = null

    constructor(parcel: Parcel) : this() {
        bankName = parcel.readString()
        acctNo = parcel.readString()
        acctType = parcel.readString()
        bvn = parcel.readString()
        acctName = parcel.readString()
    }

    companion object CREATOR : Parcelable.Creator<BankModel> {
        override fun createFromParcel(parcel: Parcel): BankModel {
            return BankModel(parcel)
        }

        override fun newArray(size: Int): Array<BankModel?> {
            return arrayOfNulls(size)
        }
    }
}
