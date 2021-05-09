package com.alfardev.dipinjamin.repositories

import com.alfardev.dipinjamin.models.Book
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

interface BookContract{
    fun new(listener : ArrayResponse<Book>)
    fun most(listener: ArrayResponse<Book>)
    fun recommended(listener: ArrayResponse<Book>)
    fun me(token : String, listener: ArrayResponse<Book>)
    fun fetchBook(bookId : Int, listener : SingleResponse<Book>)
    fun createOrUpdate(token: String, requestBody: HashMap<String, RequestBody>,
                       images: Array<MultipartBody.Part?>, listener: SingleResponse<Book>)
    fun delete(token: String, bookId: Int, listener: SingleResponse<Book>)
}

class BookRepository (private val api : ApiService) : BookContract{
    override fun new(listener: ArrayResponse<Book>) {
        api.fetchBooksNew().enqueue(object : Callback<WrappedListResponse<Book>>{
            override fun onFailure(call: Call<WrappedListResponse<Book>>, t: Throwable) {
                listener.onFailure(Error(t.message))
            }

            override fun onResponse(call: Call<WrappedListResponse<Book>>, response: Response<WrappedListResponse<Book>>) {
                when{
                    response.isSuccessful -> listener.onSuccess(response.body()!!.data)
                    else -> listener.onFailure(Error(response.message()))
                }
            }

        })
    }

    override fun most(listener: ArrayResponse<Book>) {
        api.fetchBooksMost().enqueue(object : Callback<WrappedListResponse<Book>>{
            override fun onFailure(call: Call<WrappedListResponse<Book>>, t: Throwable) {
                listener.onFailure(Error(t.message))
            }

            override fun onResponse(call: Call<WrappedListResponse<Book>>, response: Response<WrappedListResponse<Book>>) {
                when{
                    response.isSuccessful -> listener.onSuccess(response.body()!!.data)
                    else -> listener.onFailure(Error(response.message()))
                }
            }

        })
    }

    override fun recommended(listener: ArrayResponse<Book>) {
        api.fetchBooksRecommended().enqueue(object : Callback<WrappedListResponse<Book>>{
            override fun onFailure(call: Call<WrappedListResponse<Book>>, t: Throwable) {
                listener.onFailure(Error(t.message))
            }

            override fun onResponse(call: Call<WrappedListResponse<Book>>, response: Response<WrappedListResponse<Book>>) {
                when{
                    response.isSuccessful -> listener.onSuccess(response.body()!!.data)
                    else -> listener.onFailure(Error(response.message()))
                }
            }

        })
    }

    override fun me(token: String, listener: ArrayResponse<Book>) {
        api.fetchMyBooks(token).enqueue(object : Callback<WrappedListResponse<Book>>{
            override fun onFailure(call: Call<WrappedListResponse<Book>>, t: Throwable) {
                listener.onFailure(Error(t.message))
            }

            override fun onResponse(call: Call<WrappedListResponse<Book>>, response: Response<WrappedListResponse<Book>>) {
                when{
                    response.isSuccessful -> listener.onSuccess(response.body()!!.data)
                    else -> listener.onFailure(Error(response.message()))
                }
            }

        })
    }

    override fun fetchBook(bookId: Int, listener: SingleResponse<Book>) {
        api.fetchBook(bookId).enqueue(object : Callback<WrappedResponse<Book>>{
            override fun onFailure(call: Call<WrappedResponse<Book>>, t: Throwable) {
                listener.onFailure(Error(t.message))
            }

            override fun onResponse(call: Call<WrappedResponse<Book>>, response: Response<WrappedResponse<Book>>) {
                when{
                    response.isSuccessful -> listener.onSuccess(response.body()!!.data)
                    else -> listener.onFailure(Error(response.message()))
                }
            }

        })
    }

    override fun createOrUpdate(token: String, requestBody: HashMap<String, RequestBody>, images: Array<MultipartBody.Part?>, listener: SingleResponse<Book>) {
        api.createOrUpdateBook(token, requestBody, images).enqueue(object : Callback<WrappedResponse<Book>>{
            override fun onFailure(call: Call<WrappedResponse<Book>>, t: Throwable) {
                listener.onFailure(Error(t.message))
            }

            override fun onResponse(call: Call<WrappedResponse<Book>>, response: Response<WrappedResponse<Book>>) {
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

    override fun delete(token: String, bookId: Int, listener: SingleResponse<Book>) {
        api.deleteBook(token, bookId).enqueue(object : Callback<WrappedResponse<Book>>{
            override fun onFailure(call: Call<WrappedResponse<Book>>, t: Throwable) {
                listener.onFailure(Error(t.message))
            }

            override fun onResponse(call: Call<WrappedResponse<Book>>, response: Response<WrappedResponse<Book>>) {
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

}