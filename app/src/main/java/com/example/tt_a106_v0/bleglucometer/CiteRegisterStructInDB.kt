
package com.example.tt_a106_v0.bleglucometer

import android.os.Parcel
import android.os.Parcelable
import com.google.firebase.firestore.IgnoreExtraProperties

@IgnoreExtraProperties
class CiteRegisterStructInDB : Parcelable {

    var comment: String? = null
    var date: String? = null
    var medic: String? = null
    var place: String? = null
    var time: String? = null
    var title: String? = null

    constructor()

    private constructor(parcel: Parcel) {
        comment = parcel.readString()
        date = parcel.readString()
        medic = parcel.readString()
        place = parcel.readString()
        time = parcel.readString()
        title = parcel.readString()
    }

    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(dest: Parcel?, flags: Int) {
        dest?.writeString(comment)
        dest?.writeString(date)
        dest?.writeString(medic)
        dest?.writeString(place)
        dest?.writeString(time)
        dest?.writeString(title)
    }

    companion object CREATOR : Parcelable.Creator<CiteRegisterStructInDB> {
        override fun createFromParcel(parcel: Parcel): CiteRegisterStructInDB {
            return CiteRegisterStructInDB(parcel)
        }

        override fun newArray(size: Int): Array<CiteRegisterStructInDB?> {
            return arrayOfNulls(size)
        }
    }
}