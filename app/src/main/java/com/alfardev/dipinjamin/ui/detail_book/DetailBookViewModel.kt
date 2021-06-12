package com.alfardev.dipinjamin.ui.detail_book

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.alfardev.dipinjamin.models.Book
import com.alfardev.dipinjamin.models.Cart
import com.alfardev.dipinjamin.repositories.BookRepository
import com.alfardev.dipinjamin.repositories.CartRepository
import com.alfardev.dipinjamin.utils.SingleLiveEvent
import com.alfardev.dipinjamin.utils.SingleResponse

class DetailBookViewModel (private val bookRepository: BookRepository,
                           private val cartRepository: CartRepository) : ViewModel(){
    private val state : SingleLiveEvent<DetailBookState> = SingleLiveEvent()
    private val book = MutableLiveData<Book>()
    private val isAddedCart = MutableLiveData<Boolean>()

    private fun isLoading(b : Boolean) { state.value = DetailBookState.Loading(b) }
    private fun toast(m : String){ state.value = DetailBookState.ShowToast(m) }
    private fun success(){ state.value = DetailBookState.SuccessAddToCart }

    fun getBook(book_id : Int){
        isLoading(true)
        bookRepository.fetchBook(book_id, object : SingleResponse<Book>{
            override fun onSuccess(data: Book?) {
                isLoading(false)
                data?.let { book.postValue(it)  }
            }

            override fun onFailure(err: Error) {
                isLoading(false)
                toast(err.message.toString())
            }
        })
    }

    fun checkUserIsAdded(token: String, book_id: Int){
        isLoading(true)
        cartRepository.checkUserIsAdded(token, book_id, object : SingleResponse<Boolean>{
            override fun onSuccess(data: Boolean?) {
                isLoading(false)
                data?.let { isAddedCart.value = it }
            }

            override fun onFailure(err: Error) {
                isLoading(false)
                toast(err.message.toString())
            }

        })
    }

    fun addToCart(token : String, book_id: Int, owner_id : Int){
        isLoading(true)
        cartRepository.addToCart(token, book_id, owner_id, object : SingleResponse<Cart>{
            override fun onSuccess(data: Cart?) {
                isLoading(false)
                data?.let { success() }
            }

            override fun onFailure(err: Error) {
                isLoading(false)
                toast(err.message.toString())
            }
        })
    }

    fun listenToState() = state
    fun listenToBook() = book
    fun isUserAddedCart() = isAddedCart.value
}

sealed class DetailBookState{
    data class ShowToast(var message : String) : DetailBookState()
    data class Loading(var state : Boolean) : DetailBookState()
    object SuccessAddToCart : DetailBookState()
}