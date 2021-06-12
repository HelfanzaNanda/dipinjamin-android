package com.alfardev.dipinjamin.webservices

import com.alfardev.dipinjamin.utils.Constants
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class ApiClientTerritory {
    companion object {
        private var retrofit: Retrofit? = null

        private val opt = OkHttpClient.Builder().apply {
            connectTimeout(30, TimeUnit.SECONDS)
            writeTimeout(30, TimeUnit.SECONDS)
            readTimeout(30, TimeUnit.SECONDS)
        }.build()

        private fun getClient(): Retrofit {
            return if (retrofit == null) {
                retrofit = Retrofit.Builder().apply {
                    client(opt)
                    baseUrl(Constants.END_POINT_TERRITORY)
                    addConverterFactory(GsonConverterFactory.create())
                }.build()
                retrofit!!
            } else {
                retrofit!!
            }
        }

        fun instance() = getClient().create(ApiServiceTerritory::class.java)
    }
}