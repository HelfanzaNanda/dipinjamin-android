package com.alfardev.dipinjamin.ui.my_orders

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.alfardev.dipinjamin.models.Checkout
import com.alfardev.dipinjamin.repositories.CheckoutRepository
import com.alfardev.dipinjamin.utils.ArrayResponse
import com.alfardev.dipinjamin.utils.SingleLiveEvent

class MyOrderViewModel (private val checkoutRepository: CheckoutRepository) : ViewModel(){
    private val state : SingleLiveEvent<MyOrderState> = SingleLiveEvent()
    private val orders = MutableLiveData<List<Checkout>>()

    private fun isLoading(b : Boolean){ state.value = MyOrderState.Loading(b) }
    private fun toast(m :String){ state.value = MyOrderState.ShowToast(m) }
    private fun success(){ state.value = MyOrderState.Success }

    fun fetchOrdersByBorrower(token : String){
        isLoading(true)
        checkoutRepository.fetchByBorrower(token, object : ArrayResponse<Checkout>{
            override fun onSuccess(datas: List<Checkout>?) {
                isLoading(false)
                datas?.let { orders.postValue(it) }
            }

            override fun onFailure(err: Error) {
                isLoading(false)
                toast(err.message.toString())
            }
        })
    }

    fun fetchOrdersByOwner(token: String){
        isLoading(true)
        checkoutRepository.fetchByOwner(token, object : ArrayResponse<Checkout>{
            override fun onSuccess(datas: List<Checkout>?) {
                isLoading(false)
                datas?.let { orders.postValue(it) }
            }

            override fun onFailure(err: Error) {
                isLoading(false)
                toast(err.message.toString())
            }
        })
    }

    fun listenToState() = state
    fun listenToOrders() = orders
}

sealed class MyOrderState{
    data class Loading(var state : Boolean) : MyOrderState()
    data class ShowToast(var message : String) : MyOrderState()
    object Success : MyOrderState()
}