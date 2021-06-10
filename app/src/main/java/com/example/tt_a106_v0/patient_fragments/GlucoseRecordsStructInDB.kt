package com.example.tt_a106_v0.patient_fragments

import android.os.Parcel
import android.os.Parcelable
import com.google.firebase.firestore.IgnoreExtraProperties

@IgnoreExtraProperties
class GlucoseRecordsStructInDB : Parcelable {

    var glucoseLevel: String? = null
    var unit: String? = null
    var date: String? = null

    constructor()

    private constructor(parcel: Parcel) {
        glucoseLevel = parcel.readString()
        unit = parcel.readString()
        date = parcel.readString()
    }

    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(dest: Parcel?, flags: Int) {
        dest?.writeString(glucoseLevel)
        dest?.writeString(unit)
        dest?.writeString(date)
    }

    companion object CREATOR : Parcelable.Creator<GlucoseRecordsStructInDB> {
        override fun createFromParcel(parcel: Parcel): GlucoseRecordsStructInDB {
            return GlucoseRecordsStructInDB(parcel)
        }

        override fun newArray(size: Int): Array<GlucoseRecordsStructInDB?> {
            return arrayOfNulls(size)
        }
    }
}