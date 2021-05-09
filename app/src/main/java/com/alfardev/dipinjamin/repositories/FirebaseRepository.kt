package com.alfardev.dipinjamin.repositories

import com.alfardev.dipinjamin.utils.SingleResponse
import com.google.firebase.installations.FirebaseInstallations

interface FirebaseContract{
    fun generateToken(listener : SingleResponse<String>)
}

class FirebaseRepository : FirebaseContract{
    override fun generateToken(listener: SingleResponse<String>) {
        FirebaseInstallations.getInstance().getToken(true).addOnCompleteListener {
            when {
                it.isSuccessful -> {
                    it.result?.let {result->
                        listener.onSuccess(result.token)
                    } ?: kotlin.run { listener.onFailure(Error("Failed to get firebase token")) }
                }
                else -> listener.onFailure(Error("Cannot get firebase token"))
            }
        }
    }

}