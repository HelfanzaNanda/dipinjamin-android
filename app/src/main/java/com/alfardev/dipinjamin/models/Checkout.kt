package com.alfardev.dipinjamin.models

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Checkout(
    @SerializedName("id") var id : Int?= null,
    @SerializedName("book") var book : Book?= null,
    @SerializedName("borrower") var borrower : User?= null,
    @SerializedName("owner") var owner : User?= null,
    @SerializedName("address") var address : DeliveryAddress?= null,
    @SerializedName("duration") var duration : String?= null,
    @SerializedName("first_day_borrow") var first_day_borrow : String?= null,
    @SerializedName("last_day_borrow") var last_day_borrow : String?= null
) : Parcelable

data class CreateCheckout(
    @SerializedName("book_id") var book_id : String? = null,
    @SerializedName("delivery_address_id") var delivery_address_id : String?= null,
    @SerializedName("owner_id") var owner_id : String?= null,
    @SerializedName("duration") var duration : String?= null
)