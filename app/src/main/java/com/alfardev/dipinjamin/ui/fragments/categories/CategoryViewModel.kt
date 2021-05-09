package com.alfardev.dipinjamin.ui.fragments.categories

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.alfardev.dipinjamin.models.Category
import com.alfardev.dipinjamin.repositories.CategoryRepository
import com.alfardev.dipinjamin.utils.ArrayResponse
import com.alfardev.dipinjamin.utils.SingleLiveEvent

class CategoryViewModel (private val categoryRepository: CategoryRepository) : ViewModel() {
    private val state : SingleLiveEvent<CategoryState> = SingleLiveEvent()
    private val categories = MutableLiveData<List<Category>>()

    private fun isLoading(b : Boolean){ state.value = CategoryState.Loading(b) }
    private fun toast(m : String){ state.value = CategoryState.ShowToast(m) }

    fun fetchCategories(){
        isLoading(true)
        categoryRepository.fetchCategories(object : ArrayResponse<Category>{
            override fun onSuccess(datas: List<Category>?) {
                isLoading(false)
                datas?.let { categories.postValue(it) }
            }

            override fun onFailure(err: Error) {
                isLoading(false)
                toast(err.message.toString())
            }
        })
    }

    fun listenToState() = state
    fun listenToCategories() = categories
}

sealed class CategoryState{
    data class Loading(var state : Boolean = false) : CategoryState()
    data class ShowToast(var message : String) : CategoryState()
}