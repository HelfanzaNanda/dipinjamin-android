package com.alfardev.dipinjamin.models

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class User(
    @SerializedName("provider_id") var provider_id : String? = null,
    @SerializedName("provider_name") var provider_name : String? = null,
    @SerializedName("id") var id : Int? = null,
    @SerializedName("name") var name : String? = null,
    @SerializedName("email") var email : String? = null,
    @SerializedName("password") var password : String? = null,
    @SerializedName("phone") var phone : String? = null,
    @SerializedName("fcm") var fcm : String? = null,
    @SerializedName("api_token") var api_token : String? = null
) : Parcelable