package com.alfardev.dipinjamin.models

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class DeliveryAddress(
    @SerializedName("id") var id : Int? = null,
    @SerializedName("provinsi_id") var provinsi_id : String? = null,
    @SerializedName("provinsi") var provinsi : String? = null,
    @SerializedName("kabupaten_id") var kabupaten_id : String? = null,
    @SerializedName("kabupaten") var kabupaten : String? = null,
    @SerializedName("kecamatan_id") var kecamatan_id : String? = null,
    @SerializedName("kecamatan") var kecamatan : String? = null,
    @SerializedName("kode_pos") var kode_pos : String? = null,
    @SerializedName("address") var address : String? = null,
    @SerializedName("lat") var lat : String? = null,
    @SerializedName("lng") var lng : String? = null,
    @SerializedName("phone") var phone : String? = null,
    @SerializedName("name") var name : String? = null
) : Parcelable