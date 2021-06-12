package com.alfardev.dipinjamin.webservices

import com.alfardev.dipinjamin.models.Cart
import com.alfardev.dipinjamin.models.Kabupaten
import com.alfardev.dipinjamin.models.Kecamatan
import com.alfardev.dipinjamin.models.Provinsi
import com.google.gson.annotations.SerializedName
import retrofit2.Call
import retrofit2.http.*

interface ApiServiceTerritory {
    @GET("provinsi")
    fun fetchProvinsi() : Call<WrappedListProvinsiResponse<Provinsi>>

    @GET("kota")
    fun fetchKabupaten(
        @Query("id_provinsi") id_provinsi :Int
    ) : Call<WrappedListKabupatenResponse<Kabupaten>>

    @GET("kecamatan")
    fun fetchKecamatan(
        @Query("id_kota") id_kota :Int
    ) : Call<WrappedListKecamatanResponse<Kecamatan>>


}

data class WrappedListProvinsiResponse<T>(
    @SerializedName("provinsi") var data : List<T>?
)

data class WrappedListKabupatenResponse<T>(
        @SerializedName("kota_kabupaten") var data : List<T>?
)

data class WrappedListKecamatanResponse<T>(
        @SerializedName("kecamatan") var data : List<T>?
)