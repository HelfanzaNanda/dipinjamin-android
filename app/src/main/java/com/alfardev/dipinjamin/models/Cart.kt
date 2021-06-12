package com.alfardev.dipinjamin.models

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Cart(
    @SerializedName("id") var id : Int? = null,
    @SerializedName("book") var book : Book? = null,
    @SerializedName("owner") var owner_name : String? = null,
    @SerializedName("owner_id") var owner_id : Int? = null,
    @SerializedName("borrwoer") var borrower_name : String? = null
) : Parcelable