package com.alfardev.dipinjamin.ui.update_profile

import androidx.lifecycle.ViewModel
import com.alfardev.dipinjamin.models.User
import com.alfardev.dipinjamin.repositories.UserRepository
import com.alfardev.dipinjamin.utils.Constants
import com.alfardev.dipinjamin.utils.SingleLiveEvent
import com.alfardev.dipinjamin.utils.SingleResponse

class UpdateProfileViewModel (private val userRepository: UserRepository) : ViewModel(){
    private val state : SingleLiveEvent<UpdateProfileState> = SingleLiveEvent()

    private fun isLoading(b : Boolean){ state.value = UpdateProfileState.Loading(b) }
    private fun toast(m : String){ state.value = UpdateProfileState.ShowToast(m) }
    private fun success() { state.value = UpdateProfileState.Success }

    fun validate(firstName: String, lastName : String, email: String, phone : String) : Boolean{
        state.value = UpdateProfileState.Reset
        if (firstName.isEmpty()){
            state.value = UpdateProfileState.Validate(firstName = "nama depan tidak boleh kosong")
            return false
        }
        if (lastName.isEmpty()){
            state.value = UpdateProfileState.Validate(lastName = "nama belakang tidak boleh kosong")
            return false
        }
        if (email.isEmpty()){
            state.value = UpdateProfileState.Validate(email = "email tidak boleh kosong")
            return false
        }
        if(!Constants.isValidEmail(email)){
            state.value = UpdateProfileState.Validate(email = "Email tidak valid")
            return false
        }

        if (phone.isEmpty()){
            state.value = UpdateProfileState.Validate(phone = "no telepon harus di isi")
            return false
        }
        if (phone.length <= 10 || phone.length >= 13){
            state.value = UpdateProfileState.Validate(phone = "no telepon setidaknya 11 sampai 13 karakter")
            return false
        }
        return true
    }

    fun updateProfile(token : String, firstName: String, lastName: String, email: String, phone: String){
        isLoading(true)
        userRepository.updateProfile(token, firstName, lastName, email, phone, object : SingleResponse<User> {
            override fun onSuccess(data: User?) {
                isLoading(false)
                data?.let {
                    success()
                }
            }

            override fun onFailure(err: Error) {
                isLoading(false)
                toast(err.message.toString())
            }
        })
    }

    fun listenToState() = state

}

sealed class UpdateProfileState {
    data class Loading(var state : Boolean) : UpdateProfileState()
    data class ShowToast(var message : String) : UpdateProfileState()
    object Success : UpdateProfileState()
    data class Validate(
            var firstName : String? = null,
            var lastName : String? = null,
            var email: String? = null,
            var phone: String? = null
    ) : UpdateProfileState()
    object Reset : UpdateProfileState()
}
