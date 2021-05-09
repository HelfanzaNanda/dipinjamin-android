package com.alfardev.dipinjamin.ui.login

import androidx.lifecycle.ViewModel
import com.alfardev.dipinjamin.models.User
import com.alfardev.dipinjamin.repositories.FirebaseRepository
import com.alfardev.dipinjamin.repositories.UserRepository
import com.alfardev.dipinjamin.utils.Constants
import com.alfardev.dipinjamin.utils.SingleLiveEvent
import com.alfardev.dipinjamin.utils.SingleResponse

class LoginViewModel (private val userRepository: UserRepository,
    private val firebaseRepository: FirebaseRepository) : ViewModel(){
    private val state : SingleLiveEvent<LoginState> = SingleLiveEvent()
    private fun isLoading(b : Boolean) { state.value = LoginState.Loading(b) }
    private fun toast(message: String){ state.value = LoginState.ShowToast(message) }
    private fun alert(message: String){ state.value = LoginState.ShowAlert(message) }
    private fun success(token: String){ state.value = LoginState.Success(token) }

    fun validate(email: String, password : String) : Boolean{
        state.value = LoginState.Reset
        if (email.isEmpty()){
            state.value = LoginState.Validate(email = "email tidak boleh kosong")
            return false
        }
        if(!Constants.isValidEmail(email)){
            state.value = LoginState.Validate(email = "Email tidak valid")
            return false
        }
        if (password.isEmpty()){
            state.value = LoginState.Validate(password = "password tidak boleh kosong")
            return false
        }
        if(!Constants.isValidPassword(password)){
            state.value = LoginState.Validate(password = "Password setidaknya delapan karakter")
            return false
        }
        return true
    }

    fun login(email: String, password: String){
        isLoading(true)
        generateTokenFirebase(email, password)
    }

    private fun generateTokenFirebase(email: String, password: String){
        firebaseRepository.generateToken(object : SingleResponse<String>{
            override fun onSuccess(data: String?) {
                data?.let {fcm_token ->
                    sendLogin(email, password, fcm_token)
                }
            }

            override fun onFailure(err: Error) {
                isLoading(false)
                alert(err.message.toString())
            }

        })
    }

    private fun sendLogin(email: String, password: String, fcm_token: String){
        userRepository.login(email, password, fcm_token, object : SingleResponse<User>{
            override fun onSuccess(data: User?) {
                isLoading(false)
                data?.let {
                    success(it.api_token!!)
                }
            }

            override fun onFailure(err: Error) {
                isLoading(false)
                alert(err.message.toString())
            }

        })
    }

    fun listenToState() = state
}

sealed class LoginState{
    data class Loading(var state : Boolean = false) : LoginState()
    data class ShowToast(var message : String) : LoginState()
    data class ShowAlert(var message : String) : LoginState()
    data class Success(var token : String) : LoginState()
    data class Validate(var email : String? = null, var password : String? = null) : LoginState()
    object Reset : LoginState()
}