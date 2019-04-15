package fewchore.com.exolve.fewchore.model

import android.os.Parcel
import android.os.Parcelable

class CardModel() : Parcelable {
    var cardNo: String? = null
    var cardExpiry: String? = null
    var cardId: String? = null
    var cardType: String?= null
    var authCode: String?= null

    constructor(parcel: Parcel) : this() {
        cardNo = parcel.readString()
        cardExpiry = parcel.readString()
        cardId = parcel.readString()
        cardType = parcel.readString()
        authCode = parcel.readString()
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(cardNo)
        parcel.writeString(cardExpiry)
        parcel.writeString(cardId)
        parcel.writeString(cardType)
        parcel.writeString(authCode)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<CardModel> {
        override fun createFromParcel(parcel: Parcel): CardModel {
            return CardModel(parcel)
        }

        override fun newArray(size: Int): Array<CardModel?> {
            return arrayOfNulls(size)
        }
    }
}
