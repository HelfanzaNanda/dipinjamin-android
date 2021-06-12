package com.alfardev.dipinjamin.models

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Book(
    @SerializedName("id") var id : Int? = null,
    @SerializedName("category") var category : Category? = null,
    @SerializedName("category_id") var category_id : Int? = null,
    @SerializedName("owner") var owner : User? = null,
    @SerializedName("title") var title : String? = null,
    @SerializedName("year") var year : String? = null,
    @SerializedName("description") var description : String? = null,
    @SerializedName("writer") var writer : String? = null,
    @SerializedName("publisher") var publisher : String? = null,
    @SerializedName("viewer") var viewer : Int? = null,
    @SerializedName("number_of_pages") var number_of_pages : String? = null,
    @SerializedName("image") var image : String? = null,
    @SerializedName("is_available") var isAvailable : Boolean = false
) : Parcelable

@Parcelize
data class NewBook (
    @SerializedName("id") var id : Int? = null,
    @SerializedName("category_id") var category_id : Int? = null,
    @SerializedName("title") var title : String? = null,
    @SerializedName("year") var year : String? = null,
    @SerializedName("description") var description : String? = null,
    @SerializedName("writer") var writer : String? = null,
    @SerializedName("publisher") var publisher : String? = null,
    @SerializedName("number_of_pages") var number_of_pages : String? = null
) : Parcelable