package com.alfardev.dipinjamin.ui.checkout

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.alfardev.dipinjamin.models.Book
import com.alfardev.dipinjamin.models.Checkout
import com.alfardev.dipinjamin.models.CreateCheckout
import com.alfardev.dipinjamin.models.DeliveryAddress
import com.alfardev.dipinjamin.repositories.CheckoutRepository
import com.alfardev.dipinjamin.repositories.DeliveryAddressRepository
import com.alfardev.dipinjamin.utils.ArrayResponse
import com.alfardev.dipinjamin.utils.SingleLiveEvent
import com.alfardev.dipinjamin.utils.SingleResponse
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File

class CheckoutViewModel (private val deliveryAddressRepository: DeliveryAddressRepository,
                         private val checkoutRepository: CheckoutRepository) : ViewModel() {

    private val state : SingleLiveEvent<CheckoutState> = SingleLiveEvent()
    private val deliveryAddresses = MutableLiveData<List<DeliveryAddress>>()
    private val ktp = MutableLiveData<String>()
    private val deliveryAddressId = MutableLiveData<String>()

    private fun isLoading(b : Boolean){ state.value = CheckoutState.Loading(b) }
    private fun toast(m : String){ state.value = CheckoutState.ShowToast(m) }
    private fun success(){ state.value = CheckoutState.Success }
    //private fun reset() { state.value = CheckoutState.Reset }
    private fun createPartFromString(s: String) : RequestBody = RequestBody.create(MultipartBody.FORM, s)

    fun setImage(path : String){
        ktp.value = path
    }

    fun setDeliveryAddressId(id : String){
        deliveryAddressId.value = id
    }

    private fun setFileImage(): MultipartBody.Part {
        val file = File(ktp.value!!)
        val requestBodyForFile = RequestBody.create(MediaType.parse("image/*"), file)
        return MultipartBody.Part.createFormData("ktp", file.name, requestBodyForFile)
    }

    private fun setHashMap(createCheckout: CreateCheckout): HashMap<String, RequestBody> {
        val map = HashMap<String, RequestBody>()
        map["book_id"] = createPartFromString(createCheckout.book_id!!)
        map["delivery_address_id"] = createPartFromString(createCheckout.delivery_address_id!!)
        map["owner_id"] = createPartFromString(createCheckout.owner_id!!)
        map["duration"] = createPartFromString(createCheckout.duration!!)
        return map
    }

    fun fetchDeliveryAddresses(token: String){
        isLoading(true)
        deliveryAddressRepository.fetchDeliveryAddresses(token, object : ArrayResponse<DeliveryAddress>{
            override fun onSuccess(datas: List<DeliveryAddress>?) {
                isLoading(false)
                datas?.let {
                    deliveryAddresses.postValue(it)
                    println("delivery addresses "+it)
                }
            }

            override fun onFailure(err: Error) {
                isLoading(false)
                toast(err.message.toString())
            }

        })
    }

    fun validate(duration : String) : Boolean{
        //reset()
        if (duration.isEmpty()){
            toast("durasi harus di isi")
            return false
        }
        if (deliveryAddressId.value.isNullOrEmpty()){
            toast("alamat harus di isi")
        }
        if (ktp.value.isNullOrEmpty()){
            toast("ktp harus di isi")
        }
        return true
    }

    fun checkout(token : String, createCheckout: CreateCheckout){
        isLoading(true)
        val map = setHashMap(createCheckout)
        checkoutRepository.checkout(token, map, setFileImage(), object : SingleResponse<CreateCheckout>{
            override fun onSuccess(data: CreateCheckout?) {
                isLoading(false)
                data?.let { success() }
            }

            override fun onFailure(err: Error) {
                isLoading(false)
                println("errronya "+err.message.toString())
                toast(err.message.toString())
            }
        })
    }

    fun listenToState() = state
    fun listenToDeliveryAddresses() = deliveryAddresses
    fun getDeliveryAddressId() = deliveryAddressId.value


}

sealed class CheckoutState{
    data class Loading(var state : Boolean = false) : CheckoutState()
    data class ShowToast(var message : String) : CheckoutState()
    object Success : CheckoutState()
    //object Reset : CheckoutState()
}