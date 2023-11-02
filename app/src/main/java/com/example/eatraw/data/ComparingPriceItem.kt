package com.example.eatraw.data

import android.os.Parcel
import android.os.Parcelable

data class ComparingPriceItem(val fishName: String, val price: String) : Parcelable {
    // Parcelable 인터페이스를 구현하는 코드
    constructor(parcel: Parcel) : this(
        parcel.readString() ?: "",
        parcel.readString() ?: ""
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(fishName)
        parcel.writeString(price)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<ComparingPriceItem> {
        override fun createFromParcel(parcel: Parcel): ComparingPriceItem {
            return ComparingPriceItem(parcel)
        }

        override fun newArray(size: Int): Array<ComparingPriceItem?> {
            return arrayOfNulls(size)
        }
    }
}