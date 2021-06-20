
package com.example.tt_a106_v0.bleglucometer


import android.os.Parcel
import android.os.Parcelable
import com.google.firebase.firestore.IgnoreExtraProperties

@IgnoreExtraProperties
class MedicationStructInDB : Parcelable {

    var date: String? = null
    var name: String? = null
    var type: String? = null
    var time: String? = null
    var dosis: String? = null
    var frequency: String? = null
    var comment: String? = null

    constructor()

    private constructor(parcel: Parcel) {
        comment = parcel.readString()
        date = parcel.readString()
        name = parcel.readString()
        type = parcel.readString()
        time = parcel.readString()
        dosis = parcel.readString()
        frequency = parcel.readString()
    }

    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(dest: Parcel?, flags: Int) {
        dest?.writeString(comment)
        dest?.writeString(date)
        dest?.writeString(name)
        dest?.writeString(type)
        dest?.writeString(time)
        dest?.writeString(dosis)
        dest?.writeString(frequency)
    }

    companion object CREATOR : Parcelable.Creator<MedicationStructInDB> {
        override fun createFromParcel(parcel: Parcel): MedicationStructInDB {
            return MedicationStructInDB(parcel)
        }

        override fun newArray(size: Int): Array<MedicationStructInDB?> {
            return arrayOfNulls(size)
        }
    }
}



