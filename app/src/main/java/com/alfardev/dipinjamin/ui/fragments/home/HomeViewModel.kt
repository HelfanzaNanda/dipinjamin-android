package com.alfardev.dipinjamin.ui.fragments.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.alfardev.dipinjamin.models.Book
import com.alfardev.dipinjamin.models.Image
import com.alfardev.dipinjamin.repositories.BannerRepository
import com.alfardev.dipinjamin.repositories.BookRepository
import com.alfardev.dipinjamin.utils.ArrayResponse
import com.alfardev.dipinjamin.utils.SingleLiveEvent

class HomeViewModel(private val bookRepository: BookRepository,
                    private val bannerRepository: BannerRepository) : ViewModel() {
    private val state : SingleLiveEvent<HomeState> = SingleLiveEvent()
    private val newBooks = MutableLiveData<List<Book>>()
    private val mostBooks = MutableLiveData<List<Book>>()
    private val recommendedBooks = MutableLiveData<List<Book>>()
    private val banners = MutableLiveData<List<Image>>()

    private fun isLoading(b : Boolean) { state.value = HomeState.Loading(b) }
    private fun toast(m : String){ state.value = HomeState.ShowToast(m) }
    private fun alert(m : String){ state.value = HomeState.ShowAlert(m) }

    fun banners(){
        isLoading(true)
        bannerRepository.fetchBanner(object : ArrayResponse<Image>{
            override fun onSuccess(datas: List<Image>?) {
                isLoading(false)
                datas?.let { banners.postValue(it) }
            }

            override fun onFailure(err: Error) {
                isLoading(false)
                toast(err.message.toString())
            }

        })
    }


    fun new(token : String){
        isLoading(true)
        bookRepository.new(token, object : ArrayResponse<Book>{
            override fun onSuccess(datas: List<Book>?) {
                isLoading(false)
                datas?.let {
                    newBooks.postValue(it)
                }
            }

            override fun onFailure(err: Error) {
                isLoading(false)
                toast(err.message.toString())
            }
        })
    }

    fun most(token : String){
        isLoading(true)
        bookRepository.most(token, object : ArrayResponse<Book>{
            override fun onSuccess(datas: List<Book>?) {
                isLoading(false)
                datas?.let {
                    mostBooks.postValue(it)
                }
            }

            override fun onFailure(err: Error) {
                isLoading(false)
                toast(err.message.toString())
            }
        })
    }

    fun recommended(token : String){
        isLoading(true)
        bookRepository.recommended(token, object : ArrayResponse<Book>{
            override fun onSuccess(datas: List<Book>?) {
                isLoading(false)
                datas?.let {
                    recommendedBooks.postValue(it)
                }
            }

            override fun onFailure(err: Error) {
                isLoading(false)
                toast(err.message.toString())
            }
        })
    }

    fun listenToState() = state
    fun listenToNewBooks() = newBooks
    fun listenToMostBooks() = mostBooks
    fun listenToRecommendedBooks() = recommendedBooks
    fun listenToBanners() = banners
}

sealed class HomeState{
    data class Loading(var state: Boolean = false) : HomeState()
    data class ShowToast(var message : String) : HomeState()
    data class ShowAlert(var message : String) : HomeState()
}