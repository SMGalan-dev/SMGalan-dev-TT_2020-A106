package com.example.tt_a106_v0.bleglucometer


import android.os.Parcel
import android.os.Parcelable
import com.google.firebase.firestore.IgnoreExtraProperties

@IgnoreExtraProperties
class DoctorRegisterStructInDB : Parcelable {

    var name: String? = null
    var lastName: String? = null
    var mail: String? = null
    var phone:String? = null

    constructor()

    private constructor(parcel: Parcel) {
        name = parcel.readString()
        lastName = parcel.readString()
        mail = parcel.readString()
        phone = parcel.readString()

    }

    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(dest: Parcel?, flags: Int) {
        dest?.writeString(name)
        dest?.writeString(lastName)
        dest?.writeString(mail)
        dest?.writeString(phone)

    }

    companion object CREATOR : Parcelable.Creator<DoctorRegisterStructInDB> {
        override fun createFromParcel(parcel: Parcel): DoctorRegisterStructInDB {
            return DoctorRegisterStructInDB(parcel)
        }

        override fun newArray(size: Int): Array<DoctorRegisterStructInDB?> {
            return arrayOfNulls(size)
        }
    }
}