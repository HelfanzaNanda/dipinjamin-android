package com.alfardev.dipinjamin.ui.register

import androidx.lifecycle.ViewModel
import com.alfardev.dipinjamin.models.User
import com.alfardev.dipinjamin.repositories.FirebaseRepository
import com.alfardev.dipinjamin.repositories.UserRepository
import com.alfardev.dipinjamin.utils.Constants
import com.alfardev.dipinjamin.utils.SingleLiveEvent
import com.alfardev.dipinjamin.utils.SingleResponse

class RegisterViewModel (private val userRepository: UserRepository,
    private val firebaseRepository: FirebaseRepository) : ViewModel(){
    private val state : SingleLiveEvent<RegisterState> = SingleLiveEvent()

    private fun isLoading(b : Boolean) { state.value = RegisterState.Loading(b) }
    private fun toast(message: String){ state.value = RegisterState.ShowToast(message) }
    private fun alert(message: String){ state.value = RegisterState.ShowAlert(message) }
    private fun success(email: String) { state.value = RegisterState.Success(email) }
    private fun successLoginGoogle(token: String){ state.value = RegisterState.SuccessLoginGoogle(token) }

    fun validate(name: String, email: String, password: String, confirmPass: String, phone : String) : Boolean {
        state.value = RegisterState.Reset
        if (name.isEmpty()){
            state.value = RegisterState.Validate(name = "nama tidak boleh kosong")
            return false
        }

        if (!Constants.isAlphaAndSpace(name)){
            state.value = RegisterState.Validate(name = "nama hanya mengandung huruf saja dan spasi saja")
            return false
        }

        if (name.length < 5){
            state.value = RegisterState.Validate(name= "nama setidaknya 5 karakter")
            return false
        }

        if (email.isEmpty()){
            state.value = RegisterState.Validate(email = "email tidak boleh kosong")
            return false
        }

        if (!Constants.isValidEmail(email)){
            state.value = RegisterState.Validate(email = "email tidak valid")
            return false
        }

        if (password.isEmpty()){
            state.value = RegisterState.Validate(password = "password tidak boleh kosong")
            return false
        }

        if (!Constants.isValidPassword(password)){
            state.value = RegisterState.Validate(password = "password minimal 8 karakter")
            return false
        }

        if (confirmPass.isEmpty()){
            state.value = RegisterState.Validate(confirmPass = "konfirmasi password tidak boleh kosong")
            return false
        }

        if(confirmPass != password){
            state.value = RegisterState.Validate(confirmPass = "konfirmasi password tidak cocok")
            return false
        }
        if (phone.isEmpty()){
            state.value = RegisterState.Validate(phone = "no telepon harus di isi")
            return false
        }
        if (phone.length <= 10 || phone.length >= 13){
            state.value = RegisterState.Validate(phone = "no telepon setidaknya 11 sampai 13 karakter")
            return false
        }
        return true
    }

    fun register(user: User){
        isLoading(true)
        generateTokenFirebase(user)
    }

    private fun generateTokenFirebase(user: User){
        firebaseRepository.generateToken(object : SingleResponse<String> {
            override fun onSuccess(data: String?) {
                data?.let {fcm_token ->
                    user.fcm = fcm_token
                    sendRegister(user)
                }
            }

            override fun onFailure(err: Error) {
                isLoading(false)
                alert(err.message.toString())
            }

        })
    }

    private fun sendRegister(user: User){
        userRepository.register(user, object : SingleResponse<User>{
            override fun onSuccess(data: User?) {
                isLoading(false)
                data?.let {
                    success(user.email!!)
                }
            }

            override fun onFailure(err: Error) {
                isLoading(false)
                alert(err.message.toString())
            }

        })
    }

    fun loginProvider(user: User){
        isLoading(true)
        firebaseRepository.generateToken(object : SingleResponse<String>{
            override fun onSuccess(data: String?) {
                data?.let {fcm_token ->
                    sendLoginProvider(user, fcm_token)
                }
            }

            override fun onFailure(err: Error) {
                isLoading(false)
                alert(err.message.toString())
            }

        })

    }

    private fun sendLoginProvider(user: User, fcm_token: String){
        user.fcm = fcm_token
        userRepository.loginProvider(user, object : SingleResponse<User>{
            override fun onSuccess(data: User?) {
                isLoading(false)
                data?.let { successLoginGoogle(it.api_token!!) }
            }

            override fun onFailure(err: Error) {
                isLoading(false)
                alert(err.message.toString())
            }

        })
    }

    fun listenToState() = state
}

sealed class RegisterState{
    data class Loading(var state : Boolean = false) : RegisterState()
    data class ShowToast(var message : String) : RegisterState()
    data class ShowAlert(var message : String) : RegisterState()
    data class Success(var email : String) : RegisterState()
    data class SuccessLoginGoogle(var token : String) : RegisterState()
    data class Validate(
        var name : String? = null,
        var email : String? = null,
        var password : String? = null,
        var confirmPass : String? = null,
        var phone : String? = null
    ) : RegisterState()
    object Reset : RegisterState()
}