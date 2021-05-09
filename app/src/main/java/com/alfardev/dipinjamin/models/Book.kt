package com.alfardev.dipinjamin.models

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Book(
    @SerializedName("id") var id : Int? = null,
    @SerializedName("category_id") var category_id : Int? = null,
    @SerializedName("owner_id") var owner_id : Int? = null,
    @SerializedName("title") var title : String? = null,
    @SerializedName("description") var description : String? = null,
    @SerializedName("writer") var writer : String? = null,
    @SerializedName("publisher") var publisher : String? = null,
    @SerializedName("number_of_pages") var number_of_pages : String? = null,
    @SerializedName("viewer") var viewer : Int? = null,
    @SerializedName("images") var images : MutableList<Image> = mutableListOf()
) : Parcelable