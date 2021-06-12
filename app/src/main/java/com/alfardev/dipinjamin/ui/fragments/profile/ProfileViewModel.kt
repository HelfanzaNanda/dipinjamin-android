package com.alfardev.dipinjamin.ui.fragments.profile

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.alfardev.dipinjamin.models.User
import com.alfardev.dipinjamin.repositories.UserRepository
import com.alfardev.dipinjamin.utils.SingleLiveEvent
import com.alfardev.dipinjamin.utils.SingleResponse

class ProfileViewModel (private val userRepository: UserRepository) : ViewModel(){
    private val state : SingleLiveEvent<ProfileState> = SingleLiveEvent()
    private val currentUser = MutableLiveData<User>()

    private fun isLoading(b : Boolean){ state.value = ProfileState.Loading(b) }
    private fun toast(m : String){ state.value = ProfileState.ShowToast(m) }

    fun fetchCurrentUser(token : String){
        isLoading(true)
        userRepository.currentUser(token, object : SingleResponse<User>{
            override fun onSuccess(data: User?) {
                isLoading(false)
                data?.let { currentUser.postValue(it) }
            }

            override fun onFailure(err: Error) {
                isLoading(false)
                toast(err.message.toString())
            }
        })
    }

    fun listenToState() = state
    fun listenToCurrentUser() = currentUser
}

sealed class ProfileState{
    data class Loading(var state : Boolean) : ProfileState()
    data class ShowToast(var message : String) : ProfileState()
}