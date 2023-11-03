package com.example.eatraw.data

import android.os.Parcel
import android.os.Parcelable

data class ComparingPriceItem(
    val fishName: String,
    val minCost: String,
    val avgCost: String,
    val maxCost: String,
    val fishImg: String?,
    val season: String?
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: ""
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(fishName)
        parcel.writeString(minCost)
        parcel.writeString(avgCost)
        parcel.writeString(maxCost)
        parcel.writeString(fishImg)
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
