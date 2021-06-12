package com.alfardev.dipinjamin.ui.search

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.alfardev.dipinjamin.models.Book
import com.alfardev.dipinjamin.repositories.BookRepository
import com.alfardev.dipinjamin.utils.ArrayResponse
import com.alfardev.dipinjamin.utils.SingleLiveEvent

class SearchViewModel (private val bookRepository: BookRepository) : ViewModel(){
    private val state : SingleLiveEvent<SearchState> = SingleLiveEvent()
    private val books = MutableLiveData<List<Book>>()

    private fun isLoading(b : Boolean) { state.value = SearchState.Loading(b) }
    private fun toast(m : String){ state.value = SearchState.ShowToast(m) }
    private fun alert(m : String){ state.value = SearchState.ShowAlert(m) }

    fun search(title :String) {
        isLoading(true)
        bookRepository.searchBooks(title, object : ArrayResponse<Book> {
            override fun onSuccess(datas: List<Book>?) {
                isLoading(false)
                datas?.let {
                    books.postValue(it)
                }
            }

            override fun onFailure(err: Error) {
                isLoading(false)
                toast(err.message.toString())
            }
        })
    }

    fun listenToState() = state
    fun listenToBooks() = books

}

sealed class SearchState{
    data class Loading(var state: Boolean = false) : SearchState()
    data class ShowToast(var message : String) : SearchState()
    data class ShowAlert(var message : String) : SearchState()
}