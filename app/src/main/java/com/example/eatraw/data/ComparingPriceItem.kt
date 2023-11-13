package com.example.eatraw.data

import android.os.Parcel
import android.os.Parcelable

data class ComparingPriceItem(
    val fishName: String,
    val count: Int,
    val minCost: Long,
    val avgCost: Long,
    val maxCost: Long,
    val fishImg: String?,
    val season: String?
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString() ?: "",
        parcel.readInt(),
        parcel.readLong(),
        parcel.readLong(),
        parcel.readLong(),
        parcel.readString(),
        parcel.readString()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(fishName)
        parcel.writeLong(minCost)
        parcel.writeLong(avgCost)
        parcel.writeLong(maxCost)
        parcel.writeString(fishImg)
        parcel.writeString(season)
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

