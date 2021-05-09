package com.alfardev.dipinjamin.models

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Order(
    @SerializedName("id") var id : Int? = null,
    @SerializedName("book_id") var book_id : Int? = null,
    @SerializedName("borrower_id") var borrower_id : Int? = null,
    @SerializedName("owner_id") var owner_id : Int? = null,
    @SerializedName("duration") var duration : Int? = null,
    @SerializedName("first_day_borrow") var first_day_borrow : String? = null,
    @SerializedName("last_day_borrow") var last_day_borrow : String? = null,
    @SerializedName("address") var address : String? = null,
    @SerializedName("lat") var lat : String? = null,
    @SerializedName("lng") var lng : String? = null,
    @SerializedName("ktp") var ktp : Image? = null
) : Parcelable