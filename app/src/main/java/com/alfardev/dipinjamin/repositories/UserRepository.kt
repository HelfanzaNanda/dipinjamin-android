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
                    else -> listener.onFailure(Error(response.message()))
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

}