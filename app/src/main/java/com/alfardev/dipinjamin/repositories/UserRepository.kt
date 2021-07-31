package com.alfardev.dipinjamin.repositories

import com.alfardev.dipinjamin.models.User
import com.alfardev.dipinjamin.utils.SingleResponse
import com.alfardev.dipinjamin.webservices.ApiService
import com.alfardev.dipinjamin.webservices.WrappedResponse
import com.google.gson.GsonBuilder
import okhttp3.MediaType
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

interface UserContract{
    fun register(user: User, listener : SingleResponse<User>)
    fun login(email : String, password : String, fcm : String, listener: SingleResponse<User>)
    fun loginProvider(user: User, listener : SingleResponse<User>)
    fun currentUser(token :String, listener: SingleResponse<User>)
    fun updatePassword(token: String, password: String, listener: SingleResponse<User>)
    fun updateProfile(token: String, first_name : String, last_name : String, email: String, phone : String, listener: SingleResponse<User>)

}

class UserRepository (private val api : ApiService) : UserContract{
    override fun register(user: User, listener: SingleResponse<User>) {
        val g = GsonBuilder().create()
        val json = g.toJson(user)
        val b = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), json)
        api.register(b).enqueue(object : Callback<WrappedResponse<User>>{
            override fun onFailure(call: Call<WrappedResponse<User>>, t: Throwable) {
                listener.onFailure(Error(t.message))
            }

            override fun onResponse(
                call: Call<WrappedResponse<User>>,
                response: Response<WrappedResponse<User>>
            ) {
                when{
                    response.isSuccessful -> {
                        val body = response.body()
                        if (body?.status!!) {
                            listener.onSuccess(body.data)
                        }else{
                            listener.onFailure(Error("maaf tidak bisa register, mungkin email sudah pernah di daftarkan"))
                        }
                    }
                    else -> listener.onFailure(Error("maaf tidak bisa register, mungkin email sudah pernah di daftarkan lewat login with google"))
                }
            }

        })
    }

    override fun login(
        email: String,
        password: String,
        fcm: String,
        listener: SingleResponse<User>
    ) {
        api.login(email, password, fcm).enqueue(object : Callback<WrappedResponse<User>>{
            override fun onFailure(call: Call<WrappedResponse<User>>, t: Throwable) {
                listener.onFailure(Error(t.message))
            }

            override fun onResponse(
                call: Call<WrappedResponse<User>>,
                response: Response<WrappedResponse<User>>
            ) {
                when{
                    response.isSuccessful -> {
                        val body = response.body()
                        if (body?.status!!) {
                            listener.onSuccess(body.data)
                        }else{
                            listener.onFailure(Error("maaf tidak bisa login, silahkan verifikasi email dahulu"))
                        }
                    }
                    else -> listener.onFailure(Error("masukkan email dan password yang benar"))
                }
            }

        })
    }

    override fun loginProvider(user: User, listener: SingleResponse<User>) {
        val g = GsonBuilder().create()
        val json = g.toJson(user)
        val b = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), json)
        api.loginProvider(b).enqueue(object : Callback<WrappedResponse<User>>{
            override fun onFailure(call: Call<WrappedResponse<User>>, t: Throwable) {
                listener.onFailure(Error(t.message))
            }

            override fun onResponse(
                call: Call<WrappedResponse<User>>,
                response: Response<WrappedResponse<User>>
            ) {
                when{
                    response.isSuccessful -> listener.onSuccess(response.body()?.data)
                    else -> listener.onFailure(Error(response.message()))
                }
            }

        })
    }

    override fun currentUser(token: String, listener: SingleResponse<User>) {
        api.currentUser(token).enqueue(object : Callback<WrappedResponse<User>>{
            override fun onFailure(call: Call<WrappedResponse<User>>, t: Throwable) {
                listener.onFailure(Error(t.message))
            }

            override fun onResponse(call: Call<WrappedResponse<User>>, response: Response<WrappedResponse<User>>) {
                when{
                    response.isSuccessful -> listener.onSuccess(response.body()!!.data)
                    else -> listener.onFailure(Error(response.message()))
                }
            }

        })
    }

    override fun updatePassword(token: String, password: String, listener: SingleResponse<User>) {
        println("token user "+token)
        println("password user "+password)
        api.updatePassword(token, password).enqueue(object : Callback<WrappedResponse<User>>{
            override fun onFailure(call: Call<WrappedResponse<User>>, t: Throwable) {
                println("res onfailure "+t.message)
                listener.onFailure(Error(t.message))
            }

            override fun onResponse(call: Call<WrappedResponse<User>>, response: Response<WrappedResponse<User>>) {
                when{
                    response.isSuccessful -> {
                        println("res success " + response.isSuccessful)
                        val b = response.body()
                        if (b?.status!!) {
                            listener.onSuccess(b.data)
                        }else{
                            listener.onFailure(Error(b.message))
                        }
                    }
                    else -> {
                        println("res not success " + response.message())
                        listener.onFailure(Error(response.message()))
                    }
                }
            }

        })
    }

    override fun updateProfile(token: String, first_name: String, last_name: String,
                               email: String, phone: String, listener: SingleResponse<User>) {
        api.updateProfile(token, first_name, last_name, email, phone).enqueue(object : Callback<WrappedResponse<User>>{
            override fun onFailure(call: Call<WrappedResponse<User>>, t: Throwable) {
                listener.onFailure(Error(t.message))
            }

            override fun onResponse(call: Call<WrappedResponse<User>>, response: Response<WrappedResponse<User>>) {
                when{
                    response.isSuccessful -> {
                        val b = response.body()
                        if (b?.status!!) {
                            listener.onSuccess(b.data)
                        }else{
                            listener.onFailure(Error(b.message))
                        }
                    }
                    else -> listener.onFailure(Error(response.message()))
                }
            }

        })
    }

}