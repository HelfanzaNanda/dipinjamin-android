package com.alfardev.dipinjamin.repositories

import com.alfardev.dipinjamin.models.Book
import com.alfardev.dipinjamin.models.Checkout
import com.alfardev.dipinjamin.models.CreateCheckout
import com.alfardev.dipinjamin.utils.ArrayResponse
import com.alfardev.dipinjamin.utils.SingleResponse
import com.alfardev.dipinjamin.webservices.ApiService
import com.alfardev.dipinjamin.webservices.WrappedListResponse
import com.alfardev.dipinjamin.webservices.WrappedResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

interface CheckoutContract{
    fun checkout(token : String, requestBody: HashMap<String, RequestBody>,
                 image: MultipartBody.Part, listener: SingleResponse<CreateCheckout>)
    fun fetchByBorrower(token: String, listener : ArrayResponse<Checkout>)
    fun fetchByOwner(token: String, listener : ArrayResponse<Checkout>)
}

class CheckoutRepository(private val api :ApiService) : CheckoutContract{
    override fun checkout(token: String, requestBody: HashMap<String, RequestBody>, image: MultipartBody.Part, listener: SingleResponse<CreateCheckout>) {
        api.checkout(token, requestBody, image).enqueue(object : Callback<WrappedResponse<CreateCheckout>> {
            override fun onFailure(call: Call<WrappedResponse<CreateCheckout>>, t: Throwable) {
                listener.onFailure(Error(t.message))
            }

            override fun onResponse(call: Call<WrappedResponse<CreateCheckout>>, response: Response<WrappedResponse<CreateCheckout>>) {
                when{
                    response.isSuccessful -> {
                        val body = response.body()
                        if (body?.status!!) listener.onSuccess(body.data) else listener.onFailure(Error(body.message))
                    }
                    else -> listener.onFailure(Error(response.message()))
                }
            }

        })
    }

    override fun fetchByBorrower(token: String, listener: ArrayResponse<Checkout>) {
        api.getOrderByBorrower(token).enqueue(object : Callback<WrappedListResponse<Checkout>>{
            override fun onFailure(call: Call<WrappedListResponse<Checkout>>, t: Throwable) {
                listener.onFailure(Error(t.message))
            }

            override fun onResponse(call: Call<WrappedListResponse<Checkout>>, response: Response<WrappedListResponse<Checkout>>) {
                when{
                    response.isSuccessful -> listener.onSuccess(response.body()!!.data)
                    else -> listener.onFailure(Error(response.message()))
                }
            }

        })
    }

    override fun fetchByOwner(token: String, listener: ArrayResponse<Checkout>) {
        api.getOrderByOwner(token).enqueue(object : Callback<WrappedListResponse<Checkout>>{
            override fun onFailure(call: Call<WrappedListResponse<Checkout>>, t: Throwable) {
                listener.onFailure(Error(t.message))
            }

            override fun onResponse(call: Call<WrappedListResponse<Checkout>>, response: Response<WrappedListResponse<Checkout>>) {
                when{
                    response.isSuccessful -> listener.onSuccess(response.body()!!.data)
                    else -> listener.onFailure(Error(response.message()))
                }
            }

        })
    }

}