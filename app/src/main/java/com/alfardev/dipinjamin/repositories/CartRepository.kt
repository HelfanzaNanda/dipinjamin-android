package com.alfardev.dipinjamin.repositories

import com.alfardev.dipinjamin.models.Cart
import com.alfardev.dipinjamin.utils.ArrayResponse
import com.alfardev.dipinjamin.utils.SingleResponse
import com.alfardev.dipinjamin.webservices.ApiService
import com.alfardev.dipinjamin.webservices.WrappedListResponse
import com.alfardev.dipinjamin.webservices.WrappedResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

interface CartContract{
    fun fetchCarts(token :String, listener : ArrayResponse<Cart>)
    fun addToCart(token: String, bookId : Int, ownerId : Int, listener : SingleResponse<Cart>)
    fun deleteCart(token: String, id : Int, listener: SingleResponse<Cart>)
    fun checkUserIsAdded(token : String, bookId: Int, listener: SingleResponse<Boolean>)
}

class CartRepository(private val api : ApiService) : CartContract{
    override fun fetchCarts(token: String, listener: ArrayResponse<Cart>) {
        api.fetchCarts(token).enqueue(object : Callback<WrappedListResponse<Cart>>{
            override fun onFailure(call: Call<WrappedListResponse<Cart>>, t: Throwable) {
                listener.onFailure(Error(t.message))
            }

            override fun onResponse(
                call: Call<WrappedListResponse<Cart>>,
                response: Response<WrappedListResponse<Cart>>
            ) {
                when{
                    response.isSuccessful -> listener.onSuccess(response.body()?.data)
                    else -> listener.onFailure(Error(response.message()))
                }
            }

        })
    }

    override fun addToCart(
        token: String,
        bookId: Int,
        ownerId: Int,
        listener: SingleResponse<Cart>
    ) {
        api.addToCart(token, bookId, ownerId).enqueue(object : Callback<WrappedResponse<Cart>>{
            override fun onFailure(call: Call<WrappedResponse<Cart>>, t: Throwable) {
                listener.onFailure(Error(t.message))
            }

            override fun onResponse(
                call: Call<WrappedResponse<Cart>>,
                response: Response<WrappedResponse<Cart>>
            ) {
                when{
                    response.isSuccessful -> listener.onSuccess(response.body()?.data)
                    else -> listener.onFailure(Error(response.message()))
                }
            }

        })
    }

    override fun deleteCart(token: String, id: Int, listener: SingleResponse<Cart>) {
        api.deleteCart(token, id).enqueue(object : Callback<WrappedResponse<Cart>>{
            override fun onFailure(call: Call<WrappedResponse<Cart>>, t: Throwable) {
                listener.onFailure(Error(t.message))
            }

            override fun onResponse(
                call: Call<WrappedResponse<Cart>>,
                response: Response<WrappedResponse<Cart>>
            ) {
                when{
                    response.isSuccessful -> listener.onSuccess(response.body()?.data)
                    else -> listener.onFailure(Error(response.message()))
                }
            }

        })
    }

    override fun checkUserIsAdded(token: String, bookId: Int, listener: SingleResponse<Boolean>) {
        api.checkUserIsAdded(token, bookId).enqueue(object : Callback<WrappedResponse<Boolean>>{
            override fun onFailure(call: Call<WrappedResponse<Boolean>>, t: Throwable) {
                listener.onFailure(Error(t.message))
            }

            override fun onResponse(
                call: Call<WrappedResponse<Boolean>>,
                response: Response<WrappedResponse<Boolean>>
            ) {
                when{
                    response.isSuccessful -> listener.onSuccess(response.body()!!.data)
                    else -> listener.onFailure(Error(response.message()))
                }
            }

        })
    }

}