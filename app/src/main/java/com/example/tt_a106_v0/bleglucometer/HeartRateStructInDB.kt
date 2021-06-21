
package com.example.tt_a106_v0.bleglucometer

import android.os.Parcel
import android.os.Parcelable
import com.google.firebase.firestore.IgnoreExtraProperties

@IgnoreExtraProperties
class HeartRateStructInDB : Parcelable {

    var date: String? = null
    var time: String? = null
    var ppm: String? = null

    constructor()

    private constructor(parcel: Parcel) {
        date = parcel.readString()
        time = parcel.readString()
        ppm = parcel.readString()
    }

    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(dest: Parcel?, flags: Int) {
        dest?.writeString(date)
        dest?.writeString(time)
        dest?.writeString(ppm)
    }

    companion object CREATOR : Parcelable.Creator<HeartRateStructInDB> {
        override fun createFromParcel(parcel: Parcel): HeartRateStructInDB {
            return HeartRateStructInDB(parcel)
        }

        override fun newArray(size: Int): Array<HeartRateStructInDB?> {
            return arrayOfNulls(size)
        }
    }
}