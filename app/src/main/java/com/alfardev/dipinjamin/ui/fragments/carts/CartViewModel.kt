package com.alfardev.dipinjamin.ui.fragments.carts

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.alfardev.dipinjamin.models.Cart
import com.alfardev.dipinjamin.repositories.CartRepository
import com.alfardev.dipinjamin.utils.ArrayResponse
import com.alfardev.dipinjamin.utils.SingleLiveEvent
import com.alfardev.dipinjamin.utils.SingleResponse

class CartViewModel(private val cartRepository: CartRepository) : ViewModel(){
    private val state : SingleLiveEvent<CartState> = SingleLiveEvent()
    private val carts = MutableLiveData<List<Cart>>()

    private fun isLoading(b : Boolean){ state.value = CartState.Loading(b) }
    private fun toast(m : String){ state.value = CartState.ShowToast(m) }
    private fun success() { state.value = CartState.Success }


    fun getCarts(token : String){
        isLoading(true)
        cartRepository.fetchCarts(token, object : ArrayResponse<Cart>{
            override fun onSuccess(datas: List<Cart>?) {
                isLoading(false)
                datas?.let { carts.postValue(it) }
            }

            override fun onFailure(err: Error) {
                isLoading(false)
                toast(err.message.toString())
            }
        })
    }


    fun deleteCart(token: String, id : Int){
        isLoading(true)
        cartRepository.deleteCart(token,id, object : SingleResponse<Cart>{
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
    fun listenToCarts() = carts
}

sealed class CartState{
    data class Loading(var state : Boolean) : CartState()
    data class ShowToast(var message : String) : CartState()
    object Success : CartState()
}