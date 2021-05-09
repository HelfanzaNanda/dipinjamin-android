package com.alfardev.dipinjamin.models

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Image(
    @SerializedName("id") var id : Int? = null,
    @SerializedName("filepath") var filepath : String? = null,
    @SerializedName("filename") var filename : String? = null
) : Parcelable