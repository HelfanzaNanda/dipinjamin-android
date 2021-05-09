package com.alfardev.dipinjamin.ui.fragments.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.alfardev.dipinjamin.models.Book
import com.alfardev.dipinjamin.repositories.BookRepository
import com.alfardev.dipinjamin.utils.ArrayResponse
import com.alfardev.dipinjamin.utils.SingleLiveEvent

class HomeViewModel(private val bookRepository: BookRepository) : ViewModel() {
    private val state : SingleLiveEvent<HomeState> = SingleLiveEvent()
    private val newBooks = MutableLiveData<List<Book>>()
    private val mostBooks = MutableLiveData<List<Book>>()
    private val recommendedBooks = MutableLiveData<List<Book>>()

    private fun isLoading(b : Boolean) { state.value = HomeState.Loading(b) }
    private fun toast(m : String){ state.value = HomeState.ShowToast(m) }
    private fun alert(m : String){ state.value = HomeState.ShowAlert(m) }

    fun new(){
        isLoading(true)
        bookRepository.new(object : ArrayResponse<Book>{
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

    fun most(){
        isLoading(true)
        bookRepository.most(object : ArrayResponse<Book>{
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

    fun recommended(){
        isLoading(true)
        bookRepository.recommended(object : ArrayResponse<Book>{
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
}

sealed class HomeState{
    data class Loading(var state: Boolean = false) : HomeState()
    data class ShowToast(var message : String) : HomeState()
    data class ShowAlert(var message : String) : HomeState()
}