package com.example.tt_a106_v0.bleglucometer

import android.os.Parcel
import android.os.Parcelable
import com.google.firebase.firestore.IgnoreExtraProperties

@IgnoreExtraProperties
class DietsDocsStructInDB : Parcelable {

    var url: String? = null
    var date: String? = null

    constructor()

    private constructor(parcel: Parcel) {
        url = parcel.readString()
        date = parcel.readString()
    }

    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(dest: Parcel?, flags: Int) {
        dest?.writeString(url)
        dest?.writeString(date)
    }

    companion object CREATOR : Parcelable.Creator<DietsDocsStructInDB> {
        override fun createFromParcel(parcel: Parcel): DietsDocsStructInDB {
            return DietsDocsStructInDB(parcel)
        }

        override fun newArray(size: Int): Array<DietsDocsStructInDB?> {
            return arrayOfNulls(size)
        }
    }
}