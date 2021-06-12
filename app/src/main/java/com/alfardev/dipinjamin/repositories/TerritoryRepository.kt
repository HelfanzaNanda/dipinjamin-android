package com.alfardev.dipinjamin.repositories

import com.alfardev.dipinjamin.models.Kabupaten
import com.alfardev.dipinjamin.models.Kecamatan
import com.alfardev.dipinjamin.models.Provinsi
import com.alfardev.dipinjamin.utils.ArrayResponse
import com.alfardev.dipinjamin.webservices.ApiServiceTerritory
import com.alfardev.dipinjamin.webservices.WrappedListKabupatenResponse
import com.alfardev.dipinjamin.webservices.WrappedListKecamatanResponse
import com.alfardev.dipinjamin.webservices.WrappedListProvinsiResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

interface TerritoryContract{
    fun fetchProvinsi(listener : ArrayResponse<Provinsi>)
    fun fetchKabupaten(id_provinsi : String,  listener : ArrayResponse<Kabupaten>)
    fun fetchKecamatan(id_kota: String, listener : ArrayResponse<Kecamatan>)
}

class TerritoryRepository (private val api : ApiServiceTerritory) : TerritoryContract {
    override fun fetchProvinsi(listener: ArrayResponse<Provinsi>) {
        api.fetchProvinsi().enqueue(object : Callback<WrappedListProvinsiResponse<Provinsi>>{
            override fun onFailure(call: Call<WrappedListProvinsiResponse<Provinsi>>, t: Throwable) {
                listener.onFailure(Error(t.message))
            }

            override fun onResponse(call: Call<WrappedListProvinsiResponse<Provinsi>>, response: Response<WrappedListProvinsiResponse<Provinsi>>) {
                when{
                    response.isSuccessful -> listener.onSuccess(response.body()?.data)
                    else -> listener.onFailure(Error(response.message()))
                }
            }

        })
    }

    override fun fetchKabupaten(id_provinsi: String, listener: ArrayResponse<Kabupaten>) {
        api.fetchKabupaten(id_provinsi.toInt()).enqueue(object : Callback<WrappedListKabupatenResponse<Kabupaten>>{
            override fun onFailure(call: Call<WrappedListKabupatenResponse<Kabupaten>>, t: Throwable) {
                listener.onFailure(Error(t.message))
            }

            override fun onResponse(call: Call<WrappedListKabupatenResponse<Kabupaten>>, response: Response<WrappedListKabupatenResponse<Kabupaten>>) {
                when{
                    response.isSuccessful -> listener.onSuccess(response.body()?.data)
                    else -> listener.onFailure(Error(response.message()))
                }
            }

        })
    }

    override fun fetchKecamatan(id_kota: String, listener: ArrayResponse<Kecamatan>) {
        api.fetchKecamatan(id_kota.toInt()).enqueue(object : Callback<WrappedListKecamatanResponse<Kecamatan>>{
            override fun onFailure(call: Call<WrappedListKecamatanResponse<Kecamatan>>, t: Throwable) {
                listener.onFailure(Error(t.message))
            }

            override fun onResponse(call: Call<WrappedListKecamatanResponse<Kecamatan>>, response: Response<WrappedListKecamatanResponse<Kecamatan>>) {
                when{
                    response.isSuccessful -> listener.onSuccess(response.body()?.data)
                    else -> listener.onFailure(Error(response.message()))
                }
            }

        })
    }

}