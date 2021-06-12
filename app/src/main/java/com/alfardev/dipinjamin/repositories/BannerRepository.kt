package com.alfardev.dipinjamin.repositories

import com.alfardev.dipinjamin.models.Image
import com.alfardev.dipinjamin.utils.ArrayResponse
import com.alfardev.dipinjamin.webservices.ApiService
import com.alfardev.dipinjamin.webservices.WrappedListResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

interface BannerContract{
    fun fetchBanner(listener : ArrayResponse<Image>)
}

class BannerRepository(private val api : ApiService) : BannerContract{
    override fun fetchBanner(listener: ArrayResponse<Image>) {
        api.fetchBanners().enqueue(object : Callback<WrappedListResponse<Image>>{
            override fun onFailure(call: Call<WrappedListResponse<Image>>, t: Throwable) {
                listener.onFailure(Error(t.message))
            }

            override fun onResponse(
                call: Call<WrappedListResponse<Image>>,
                response: Response<WrappedListResponse<Image>>
            ) {
                when{
                    response.isSuccessful -> listener.onSuccess(response.body()?.data)
                    else -> listener.onFailure(Error(response.message()))
                }
            }

        })
    }

}