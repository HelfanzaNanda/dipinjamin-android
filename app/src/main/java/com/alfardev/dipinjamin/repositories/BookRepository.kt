package com.alfardev.dipinjamin.repositories

import com.alfardev.dipinjamin.models.Book
import com.alfardev.dipinjamin.models.NewBook
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
    fun new(token: String, listener : ArrayResponse<Book>)
    fun most(token : String, listener: ArrayResponse<Book>)
    fun recommended(token : String, listener: ArrayResponse<Book>)
    fun me(token : String, listener: ArrayResponse<Book>)
    fun fetchBook(bookId : Int, listener : SingleResponse<Book>)
    fun createOrUpdate(token: String, requestBody: HashMap<String, RequestBody>,
                       image: MultipartBody.Part, listener: SingleResponse<NewBook>)
    fun delete(token: String, bookId: Int, listener: SingleResponse<Book>)
    fun fetchBooksByCategory(categoryId: Int, listener: ArrayResponse<Book>)
    fun searchBooks(title : String, listener: ArrayResponse<Book>)
}

class BookRepository (private val api : ApiService) : BookContract{
    override fun new(token: String, listener: ArrayResponse<Book>) {
        api.fetchBooksNew(token).enqueue(object : Callback<WrappedListResponse<Book>>{
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

    override fun most(token : String, listener: ArrayResponse<Book>) {
        api.fetchBooksMost(token).enqueue(object : Callback<WrappedListResponse<Book>>{
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

    override fun recommended(token : String, listener: ArrayResponse<Book>) {
        api.fetchBooksRecommended(token).enqueue(object : Callback<WrappedListResponse<Book>>{
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

    override fun createOrUpdate(token: String, requestBody: HashMap<String, RequestBody>, image: MultipartBody.Part, listener: SingleResponse<NewBook>) {
        api.createOrUpdateBook(token, requestBody, image).enqueue(object : Callback<WrappedResponse<NewBook>>{
            override fun onFailure(call: Call<WrappedResponse<NewBook>>, t: Throwable) {
                listener.onFailure(Error(t.message))
            }

            override fun onResponse(call: Call<WrappedResponse<NewBook>>, response: Response<WrappedResponse<NewBook>>) {
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

    override fun fetchBooksByCategory(categoryId: Int, listener: ArrayResponse<Book>) {
        api.fetchBooksByCategory(categoryId).enqueue(object : Callback<WrappedListResponse<Book>>{
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

    override fun searchBooks(title: String, listener: ArrayResponse<Book>) {
        api.searchBooks(title).enqueue(object : Callback<WrappedListResponse<Book>>{
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

}