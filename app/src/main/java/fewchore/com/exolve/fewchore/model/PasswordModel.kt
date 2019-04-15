package fewchore.com.exolve.fewchore.model

import android.os.Parcel
import android.os.Parcelable

class PasswordModel() : Parcelable {


    var password: String ?= null

    constructor(parcel: Parcel) : this() {
        password = parcel.readString()
    }
    override fun writeToParcel(p0: Parcel?, p1: Int) {
        p0!!.writeString(password)

    }

    override fun describeContents(): Int {
        return 0
    }
    companion object CREATOR : Parcelable.Creator<PasswordModel> {
        override fun createFromParcel(parcel: Parcel): PasswordModel {
            return PasswordModel(parcel)
        }

        override fun newArray(size: Int): Array<PasswordModel?> {
            return arrayOfNulls(size)
        }
    }

}