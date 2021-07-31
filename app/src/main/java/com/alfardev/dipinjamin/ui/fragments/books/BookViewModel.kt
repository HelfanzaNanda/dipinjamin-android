package com.alfardev.dipinjamin.ui.fragments.books

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.alfardev.dipinjamin.models.Book
import com.alfardev.dipinjamin.repositories.BookRepository
import com.alfardev.dipinjamin.utils.ArrayResponse
import com.alfardev.dipinjamin.utils.SingleLiveEvent
import com.alfardev.dipinjamin.utils.SingleResponse

class BookViewModel (private val bookRepository: BookRepository) : ViewModel(){
    private val state : SingleLiveEvent<BookState> = SingleLiveEvent()
    private val myBooks = MutableLiveData<List<Book>>()

    private fun isLoading(b : Boolean){ state.value = BookState.Loading(b) }
    private fun toast(m : String){ state.value = BookState.ShowToast(m) }
    private fun successDelete(){ state.value = BookState.SuccessDelete }

    fun fetchMyBooks(token : String) {
        isLoading(true)
        bookRepository.me(token, object : ArrayResponse<Book>{
            override fun onSuccess(datas: List<Book>?) {
                isLoading(false)
                datas?.let {
                    myBooks.postValue(it)
                }
            }

            override fun onFailure(err: Error) {
                isLoading(false)
                toast(err.message.toString())
            }
        })
    }

    fun deleteBook(token: String, id : Int){
        isLoading(true)
        bookRepository.delete(token, id, object : SingleResponse<Book>{
            override fun onSuccess(data: Book?) {
                isLoading(false)
                data?.let { successDelete() }
            }

            override fun onFailure(err: Error) {
                isLoading(false)
                toast(err.message.toString())
            }

        })
    }

    fun listenToState() = state
    fun listenToMyBooks() = myBooks
}

sealed class BookState{
    object SuccessDelete : BookState()
    data class Loading(var state : Boolean = false) : BookState()
    data class ShowToast(var message : String) : BookState()
}