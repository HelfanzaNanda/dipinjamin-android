package com.alfardev.dipinjamin.ui.books

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.alfardev.dipinjamin.models.Book
import com.alfardev.dipinjamin.repositories.BookRepository
import com.alfardev.dipinjamin.utils.ArrayResponse
import com.alfardev.dipinjamin.utils.SingleLiveEvent

class BookViewModel(private val bookRepository: BookRepository) : ViewModel() {
    private val state : SingleLiveEvent<BookState> = SingleLiveEvent()
    private val books = MutableLiveData<List<Book>>()

    private fun isLoading(b : Boolean) { state.value = BookState.Loading(b) }
    private fun toast(m : String){ state.value = BookState.ShowToast(m) }
    private fun alert(m : String){ state.value = BookState.ShowAlert(m) }

    fun new(token :String){
        isLoading(true)
        bookRepository.new(token, object : ArrayResponse<Book> {
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

    fun most(token : String){
        isLoading(true)
        bookRepository.most(token, object : ArrayResponse<Book> {
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

    fun recommended(token : String){
        isLoading(true)
        bookRepository.recommended(token, object : ArrayResponse<Book> {
            override fun onSuccess(datas: List<Book>?) {
                isLoading(false)
                datas?.let { books.postValue(it) }
            }

            override fun onFailure(err: Error) {
                isLoading(false)
                toast(err.message.toString())
            }
        })
    }

    fun fetchBooksByCategory(idCategory : Int){
        isLoading(true)
        bookRepository.fetchBooksByCategory(idCategory, object : ArrayResponse<Book>{
            override fun onSuccess(datas: List<Book>?) {
                isLoading(false)
                datas?.let { books.postValue(it) }
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

sealed class BookState{
    data class Loading(var state: Boolean = false) : BookState()
    data class ShowToast(var message : String) : BookState()
    data class ShowAlert(var message : String) : BookState()
}