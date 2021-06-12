package com.alfardev.dipinjamin.repositories

import com.alfardev.dipinjamin.models.DeliveryAddress
import com.alfardev.dipinjamin.utils.ArrayResponse
import com.alfardev.dipinjamin.utils.SingleResponse
import com.alfardev.dipinjamin.webservices.ApiService
import com.alfardev.dipinjamin.webservices.WrappedListResponse
import com.alfardev.dipinjamin.webservices.WrappedResponse
import com.google.gson.GsonBuilder
import okhttp3.MediaType
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

interface DeliveryContract{
    fun fetchDeliveryAddresses(token : String, listener : ArrayResponse<DeliveryAddress>)
    fun createUpdateDeliveryAddress(token: String, deliveryAddress: DeliveryAddress, listener : SingleResponse<DeliveryAddress>)
    fun getDeliveryAddress(token: String, id: String, listener: SingleResponse<DeliveryAddress>)
    fun deleteDeliveryAddress(token: String, id : String, listener: SingleResponse<DeliveryAddress>)
}

class DeliveryAddressRepository(private val api: ApiService) : DeliveryContract{
    override fun fetchDeliveryAddresses(token: String, listener: ArrayResponse<DeliveryAddress>) {
        api.fetchDeliveryAddresses(token).enqueue(object : Callback<WrappedListResponse<DeliveryAddress>>{
            override fun onFailure(call: Call<WrappedListResponse<DeliveryAddress>>, t: Throwable) {
                listener.onFailure(Error(t.message))
            }

            override fun onResponse(call: Call<WrappedListResponse<DeliveryAddress>>, response: Response<WrappedListResponse<DeliveryAddress>>) {
                when {
                    response.isSuccessful -> listener.onSuccess(response.body()?.data)
                    else -> listener.onFailure(Error(response.message()))
                }
            }

        })
    }

    override fun createUpdateDeliveryAddress(token: String, deliveryAddress: DeliveryAddress, listener: SingleResponse<DeliveryAddress>) {
        val g = GsonBuilder().create()
        val json = g.toJson(deliveryAddress)
        val b = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), json)
        api.createUpdateAddress(token, b).enqueue(object : Callback<WrappedResponse<DeliveryAddress>>{
            override fun onFailure(call: Call<WrappedResponse<DeliveryAddress>>, t: Throwable) {
                listener.onFailure(Error(t.message))
            }

            override fun onResponse(call: Call<WrappedResponse<DeliveryAddress>>, response: Response<WrappedResponse<DeliveryAddress>>) {
                when{
                    response.isSuccessful -> {
                        val body = response.body()
                        if (body?.status!!) {
                            listener.onSuccess(body.data)
                        }else{
                            listener.onFailure(Error(body.message))
                        }
                    }
                    else -> listener.onFailure(Error(response.message()))
                }
            }

        })
    }

    override fun getDeliveryAddress(token: String, id: String, listener: SingleResponse<DeliveryAddress>) {
        api.getDeliveryAddress(token, id.toInt()).enqueue(object : Callback<WrappedResponse<DeliveryAddress>>{
            override fun onFailure(call: Call<WrappedResponse<DeliveryAddress>>, t: Throwable) {
                listener.onFailure(Error(t.message))
            }

            override fun onResponse(call: Call<WrappedResponse<DeliveryAddress>>, response: Response<WrappedResponse<DeliveryAddress>>) {
                when {
                    response.isSuccessful -> listener.onSuccess(response.body()?.data)
                    else -> listener.onFailure(Error(response.message()))
                }
            }

        })
    }

    override fun deleteDeliveryAddress(token: String, id: String, listener: SingleResponse<DeliveryAddress>) {
        api.deleteDeliveryAddress(token, id.toInt()).enqueue(object : Callback<WrappedResponse<DeliveryAddress>>{
            override fun onFailure(call: Call<WrappedResponse<DeliveryAddress>>, t: Throwable) {
                listener.onFailure(Error(t.message))
            }

            override fun onResponse(call: Call<WrappedResponse<DeliveryAddress>>, response: Response<WrappedResponse<DeliveryAddress>>) {
                when{
                    response.isSuccessful -> {
                        val body = response.body()
                        if (body?.status!!) {
                            listener.onSuccess(body.data)
                        }else{
                            listener.onFailure(Error(body.message))
                        }
                    }
                    else -> listener.onFailure(Error(response.message()))
                }
            }

        })
    }

}