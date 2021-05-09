package com.alfardev.dipinjamin.webservices

import com.alfardev.dipinjamin.models.Book
import com.alfardev.dipinjamin.models.Category
import com.alfardev.dipinjamin.models.Order
import com.alfardev.dipinjamin.models.User
import com.google.gson.annotations.SerializedName
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*

interface ApiService{

    @Headers("Content-Type: application/json")
    @POST("register")
    fun register(
        @Body body: RequestBody
    ) : Call<WrappedResponse<User>>

    @FormUrlEncoded
    @POST("login")
    fun login(
        @Field("email") email : String,
        @Field("password") password : String,
        @Field("fcm") fcm : String
    ) : Call<WrappedResponse<User>>

    @GET("categories")
    fun fetchCategories() : Call<WrappedListResponse<Category>>

    @GET("categories/{id}/books")
    fun fetchBookByCategory(
        @Path("id") categoryId : Int
    ) : Call<WrappedListResponse<Book>>

    @GET("books/new")
    fun fetchBooksNew() : Call<WrappedListResponse<Book>>

    @GET("books/most")
    fun fetchBooksMost() : Call<WrappedListResponse<Book>>

    @GET("books/recommended")
    fun fetchBooksRecommended() : Call<WrappedListResponse<Book>>

    @GET("books/me")
    fun fetchMyBooks(
        @Header("Authorization") token : String
    ) : Call<WrappedListResponse<Book>>

    @GET("books/{id}/get")
    fun fetchBook(
        @Path("id") bookId : Int
    ) : Call<WrappedResponse<Book>>

    @Multipart
    fun createOrUpdateBook(
        @Header("Authorization") token : String,
        @PartMap json : HashMap<String, RequestBody>,
        @Part images : Array<MultipartBody.Part?>
    ) : Call<WrappedResponse<Book>>

    @DELETE("books/{id}/delete")
    fun deleteBook(
        @Header("Authorization") token : String,
        @Path("id") bookId : Int
    ) : Call<WrappedResponse<Book>>

    @Multipart
    @POST("orders")
    fun order(
        @Header("Authorization") token : String,
        @PartMap json : HashMap<String, RequestBody>,
        @Part images : Array<MultipartBody.Part?>
    ) : Call<WrappedResponse<Order>>

    @GET("orders/me")
    fun myOrders(
        @Header("Authorization") token : String
    ) : Call<WrappedListResponse<Order>>



}

data class WrappedResponse<T>(
    @SerializedName("message") var message : String?,
    @SerializedName("status") var status : Boolean?,
    @SerializedName("data") var data : T?
)

data class WrappedListResponse<T>(
    @SerializedName("message") var message : String?,
    @SerializedName("status") var status : Boolean?,
    @SerializedName("data") var data : List<T>?
)