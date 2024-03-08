package com.example.studentmanagement.models

import android.os.Parcel
import android.os.Parcelable
import kotlinx.serialization.Serializable

@Serializable
data class Student(var name: String?, var className: String?, var dob: String?, var gender: String?) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(name)
        parcel.writeString(className)
        parcel.writeString(dob)
        parcel.writeString(gender)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Student> {
        override fun createFromParcel(parcel: Parcel): Student {
            return Student(parcel)
        }

        override fun newArray(size: Int): Array<Student?> {
            return arrayOfNulls(size)
        }
    }

    override fun toString(): String {
        return "$name,$className,$dob,$gender\n"
    }

}