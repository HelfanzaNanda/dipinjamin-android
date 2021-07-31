package com.alfardev.dipinjamin.webservices

import com.alfardev.dipinjamin.models.*
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

    @Headers("Content-Type: application/json")
    @POST("login/provider")
    fun loginProvider(
        @Body body: RequestBody
    ) : Call<WrappedResponse<User>>

    @GET("banners")
    fun fetchBanners() : Call<WrappedListResponse<Image>>

    @GET("categories")
    fun fetchCategories() : Call<WrappedListResponse<Category>>

    @GET("books/category/{id}")
    fun fetchBooksByCategory(
        @Path("id") categoryId : Int
    ) : Call<WrappedListResponse<Book>>

    @GET("books/search/{title}")
    fun searchBooks(
            @Path("title") title : String
    ) : Call<WrappedListResponse<Book>>

    @GET("books/new")
    fun fetchBooksNew(
        @Header("Authorization") token : String
    ) : Call<WrappedListResponse<Book>>

    @GET("books/most")
    fun fetchBooksMost(
        @Header("Authorization") token : String
    ) : Call<WrappedListResponse<Book>>

    @GET("books/recommended")
    fun fetchBooksRecommended(
        @Header("Authorization") token : String
    ) : Call<WrappedListResponse<Book>>

    @GET("books/me")
    fun fetchMyBooks(
        @Header("Authorization") token : String
    ) : Call<WrappedListResponse<Book>>

    @GET("books/{id}/get")
    fun fetchBook(
        @Path("id") bookId : Int
    ) : Call<WrappedResponse<Book>>

    @Multipart
    @POST("books/store")
    fun createOrUpdateBook(
        @Header("Authorization") token : String,
        @PartMap json : HashMap<String, RequestBody>,
        @Part image : MultipartBody.Part
    ) : Call<WrappedResponse<NewBook>>

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

    @GET("user")
    fun currentUser(
        @Header("Authorization") token : String
    ) : Call<WrappedResponse<User>>

    @FormUrlEncoded
    @POST("user/password")
    fun updatePassword(
        @Header("Authorization") token : String,
        @Field("password") password : String
    ) : Call<WrappedResponse<User>>

    @FormUrlEncoded
    @POST("user")
    fun updateProfile(
        @Header("Authorization") token : String,
        @Field("first_name") first_name : String,
        @Field("last_name") last_name : String,
        @Field("email") email : String,
        @Field("phone") phone : String
    ) : Call<WrappedResponse<User>>


    @GET("carts")
    fun fetchCarts(
        @Header("Authorization") token : String
    ) : Call<WrappedListResponse<Cart>>

    @FormUrlEncoded
    @POST("carts")
    fun addToCart(
        @Header("Authorization") token : String,
        @Field("book_id") book_id :Int,
        @Field("owner_id") owner_id :Int
    ) : Call<WrappedResponse<Cart>>


    @DELETE("carts/{id}/delete")
    fun deleteCart(
        @Header("Authorization") token : String,
        @Path("id") id :Int
    ) : Call<WrappedResponse<Cart>>

    @GET("check-user-is-added-cart/{bookId}")
    fun checkUserIsAdded(
        @Header("Authorization") token : String,
        @Path("bookId") bookId : Int
    ) :Call<WrappedResponse<Boolean>>

    @Headers("Content-Type: application/json")
    @POST("delivery-addresses")
    fun createUpdateAddress(
        @Header("Authorization") token : String,
        @Body body: RequestBody
    ) :Call<WrappedResponse<DeliveryAddress>>

    @GET("delivery-addresses")
    fun fetchDeliveryAddresses(
        @Header("Authorization") token : String
    ) : Call<WrappedListResponse<DeliveryAddress>>

    @GET("delivery-addresses/{id}/get")
    fun getDeliveryAddress(
        @Header("Authorization") token : String,
        @Path("id") bookId : Int
    ) : Call<WrappedResponse<DeliveryAddress>>

    @DELETE("delivery-addresses/{id}/delete")
    fun deleteDeliveryAddress(
        @Header("Authorization") token : String,
        @Path("id") bookId : Int
    ) : Call<WrappedResponse<DeliveryAddress>>

    @Multipart
    @POST("orders")
    fun checkout(
        @Header("Authorization") token : String,
        @PartMap json : HashMap<String, RequestBody>,
        @Part image : MultipartBody.Part
    ) : Call<WrappedResponse<CreateCheckout>>

    @GET("orders/by-borrower")
    fun getOrderByBorrower(
        @Header("Authorization") token : String
    ) : Call<WrappedListResponse<Checkout>>

    @GET("orders/by-owner")
    fun getOrderByOwner(
            @Header("Authorization") token : String
    ) : Call<WrappedListResponse<Checkout>>

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