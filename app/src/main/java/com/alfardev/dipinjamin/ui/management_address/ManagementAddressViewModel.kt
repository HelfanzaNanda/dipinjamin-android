package com.alfardev.dipinjamin.ui.management_address

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.alfardev.dipinjamin.models.DeliveryAddress
import com.alfardev.dipinjamin.models.Kabupaten
import com.alfardev.dipinjamin.models.Kecamatan
import com.alfardev.dipinjamin.models.Provinsi
import com.alfardev.dipinjamin.repositories.DeliveryAddressRepository
import com.alfardev.dipinjamin.repositories.TerritoryRepository
import com.alfardev.dipinjamin.utils.ArrayResponse
import com.alfardev.dipinjamin.utils.SingleLiveEvent
import com.alfardev.dipinjamin.utils.SingleResponse

class ManagementAddressViewModel (private val deliveryAddressRepository: DeliveryAddressRepository,
private val territoryRepository: TerritoryRepository) : ViewModel(){
    private val state : SingleLiveEvent<ManagementAddressState> = SingleLiveEvent()
    private val deliveryAddresses = MutableLiveData<List<DeliveryAddress>>()
    private val deliveryAddress = MutableLiveData<DeliveryAddress>()
    private val provinsi = MutableLiveData<List<Provinsi>>()
    private val kabupaten = MutableLiveData<List<Kabupaten>>()
    private val kecamatan = MutableLiveData<List<Kecamatan>>()

    private val provinsiNames = MutableLiveData<List<String>>()
    private val kabupatenNames = MutableLiveData<List<String>>()
    private val kecamatanNames = MutableLiveData<List<String>>()

    private val provinsi_id = MutableLiveData<String>()
    private val kabupaten_id = MutableLiveData<String>()
    private val kecamatan_id = MutableLiveData<String>()

    private val provinsi_name = MutableLiveData<String>()
    private val kabupaten_name = MutableLiveData<String>()
    private val kecamatan_name = MutableLiveData<String>()

    private val deliveryAddressId = MutableLiveData<Int>()

    private fun isLoading(b : Boolean){ state.value = ManagementAddressState.Loading(b) }
    private fun toast(message: String){ state.value = ManagementAddressState.ShowToast(message) }
    private fun success(){ state.value = ManagementAddressState.Success }

    fun fetchProvinsi(){
        isLoading(true)
        territoryRepository.fetchProvinsi(object : ArrayResponse<Provinsi>{
            override fun onSuccess(datas: List<Provinsi>?) {
                isLoading(false)
                datas?.let {provinsi.postValue(it) }
            }

            override fun onFailure(err: Error) {
                isLoading(false)
                println("error territory "+err.message.toString())
                toast(err.message.toString())
            }

        })
    }

    fun fetchKabupaten(id_provinsi : String){
        isLoading(true)
        territoryRepository.fetchKabupaten(id_provinsi, object : ArrayResponse<Kabupaten>{
            override fun onSuccess(datas: List<Kabupaten>?) {
                isLoading(false)
                datas?.let {
                    println("kabupaten "+it)
                    kabupaten.postValue(it)
                }
            }

            override fun onFailure(err: Error) {
                isLoading(false)
                println("error territory "+err.message.toString())
                toast(err.message.toString())
            }

        })
    }

    fun fetchKecamatan(id_kota : String){
        isLoading(true)
        territoryRepository.fetchKecamatan(id_kota, object : ArrayResponse<Kecamatan>{
            override fun onSuccess(datas: List<Kecamatan>?) {
                isLoading(false)
                datas?.let { kecamatan.postValue(it) }
            }

            override fun onFailure(err: Error) {
                isLoading(false)
                println("error territory "+err.message.toString())
                toast(err.message.toString())
            }

        })
    }

    fun fetchDeliveryAddresses(token : String){
        isLoading(true)
        deliveryAddressRepository.fetchDeliveryAddresses(token, object : ArrayResponse<DeliveryAddress>{
            override fun onSuccess(datas: List<DeliveryAddress>?) {
                isLoading(false)
                datas?.let { deliveryAddresses.postValue(it) }
            }

            override fun onFailure(err: Error) {
                isLoading(false)
                toast(err.message.toString())
            }
        })
    }

    fun createUpdate(token: String, deliveryAddress: DeliveryAddress){
        isLoading(true)
        deliveryAddressRepository.createUpdateDeliveryAddress(token, deliveryAddress, object : SingleResponse<DeliveryAddress>{
            override fun onSuccess(data: DeliveryAddress?) {
                isLoading(false)
                data?.let { success() }
            }

            override fun onFailure(err: Error) {
                isLoading(false)
                toast(err.message.toString())
            }
        })
    }


    fun getDeliveryAddress(token: String, id: String){
        isLoading(true)
        deliveryAddressRepository.getDeliveryAddress(token, id, object : SingleResponse<DeliveryAddress>{
            override fun onSuccess(data: DeliveryAddress?) {
                isLoading(false)
                data?.let { deliveryAddress.postValue(it) }
            }

            override fun onFailure(err: Error) {
                isLoading(false)
                toast(err.message.toString())
            }

        })
    }

    fun deleteDeliveryAddress(token: String, id : String){
        isLoading(true)
        deliveryAddressRepository.deleteDeliveryAddress(token, id, object : SingleResponse<DeliveryAddress>{
            override fun onSuccess(data: DeliveryAddress?) {
                isLoading(false)
                data?.let { success() }
            }

            override fun onFailure(err: Error) {
                isLoading(false)
                toast(err.message.toString())
            }
        })
    }

    fun setProvinsi(id : String, name : String){
        provinsi_id.value = id
        provinsi_name.value = name
    }

    fun setKabupaten(id : String, name : String){
        kabupaten_id.value = id
        kabupaten_name.value = name
    }

    fun setKecamatan(id : String, name : String){
        kecamatan_id.value = id
        kecamatan_name.value = name
    }

    fun setProvinsiNames(names : List<String>){
        provinsiNames.value = names
    }

    fun setKabupatenNames(names : List<String>){
        kabupatenNames.value = names
    }

    fun setKecamatanNames(names : List<String>){
        kecamatanNames.value = names
    }

    fun setDeliveryAddressId(id : Int){
        deliveryAddressId.value = id
    }


    fun listenToState() = state
    fun listenToDeliveryAddresses() = deliveryAddresses
    fun listenToProvinsi() = provinsi
    fun listenTokabupaten() = kabupaten
    fun listenToKecamatan() = kecamatan
    fun listenToDeliveryAddress() = deliveryAddress

    fun getProvinsiId() = provinsi_id.value
    fun getKabupatenId() = kabupaten_id.value
    fun getKecamatanId() = kecamatan_id.value

    fun getProvinsiName() = provinsi_name.value
    fun getKabupatenName() = kabupaten_name.value
    fun getKecamatanName() = kecamatan_name.value

    fun getProvinsiNames() = provinsiNames.value
    fun getKabupatenNames() = kabupatenNames.value
    fun getKecamatanNames() = kecamatanNames.value

    fun getDeliveryAddressId() = deliveryAddressId.value

}

sealed class ManagementAddressState{
    data class Loading(var state : Boolean): ManagementAddressState()
    data class ShowToast(var message : String) : ManagementAddressState()
    object Success : ManagementAddressState()
}