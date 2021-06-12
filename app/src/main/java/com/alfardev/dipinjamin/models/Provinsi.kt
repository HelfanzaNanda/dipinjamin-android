package com.alfardev.dipinjamin.models

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Provinsi(
        @SerializedName("id") var id : Int? = null,
        @SerializedName("nama") var name : String? = null
) : Parcelable