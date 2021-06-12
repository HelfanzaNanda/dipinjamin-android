package com.alfardev.dipinjamin.ui.create_update_book

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.alfardev.dipinjamin.models.Book
import com.alfardev.dipinjamin.models.Category
import com.alfardev.dipinjamin.models.NewBook
import com.alfardev.dipinjamin.repositories.BookRepository
import com.alfardev.dipinjamin.repositories.CategoryRepository
import com.alfardev.dipinjamin.utils.ArrayResponse
import com.alfardev.dipinjamin.utils.SingleLiveEvent
import com.alfardev.dipinjamin.utils.SingleResponse
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File

class CreateUpdateBookViewModel (private val bookRepository: BookRepository,
                                 private val categoryRepository: CategoryRepository) : ViewModel(){
    private val state : SingleLiveEvent<CreateUpdateBookState> = SingleLiveEvent()
    private val categories = MutableLiveData<List<Category>>()
    private val image = MutableLiveData<String>()
    private val categoryId = MutableLiveData<String>()

    private fun isLoading(b : Boolean){ state.value = CreateUpdateBookState.Loading(b) }
    private fun toast(m : String){ state.value = CreateUpdateBookState.ShowToast(m) }
    private fun success() { state.value = CreateUpdateBookState.Success }
    private fun createPartFromString(s: String) : RequestBody = RequestBody.create(MultipartBody.FORM, s)

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

//    fun validate(category_id: String, title: String, description: String, writer: String,
//                    publisher: String, year: String, number_of_pages: String) : Boolean{
//        state.value = CreateUpdateBookState.Reset
//        if (title.isEmpty()){
//            state.value = CreateUpdateBookState.Validate(title = "Judul tidak boleh kosong")
//            return false
//        }
//        if (writer.isEmpty()){
//            state.value = CreateUpdateBookState.Validate(writer = "Penulis tidak boleh kosong")
//            return false
//        }
//
//        if (year.isEmpty()){
//            state.value = CreateUpdateBookState.Validate(year = "Tahun tidak boleh kosong")
//            return false
//        }
//
//        if (category_id.isEmpty()){
//            toast("Genre tidak boleh kosong")
//            return false
//        }
//
//        if (publisher.isEmpty()){
//            state.value = CreateUpdateBookState.Validate(publisher = "Penerbit tidak boleh kosong")
//            return false
//        }
//
//        if (number_of_pages.isEmpty()){
//            state.value = CreateUpdateBookState.Validate(number_of_pages = "Ketebalan tidak boleh kosong")
//            return false
//        }
//
//        if (description.isEmpty()){
//            state.value = CreateUpdateBookState.Validate(description = "Deskripsi tidak boleh kosong")
//            return false
//        }
//
//        if (image.value.isNullOrBlank()) {
//            toast("Gambar tidak boleh kosong")
//        }
//        return true
//    }


    fun validate(book: NewBook) : Boolean{
        state.value = CreateUpdateBookState.Reset
        if (book.title.isNullOrEmpty()){
            state.value = CreateUpdateBookState.Validate(title = "Judul tidak boleh kosong")
            return false
        }
        if (book.writer.isNullOrEmpty()){
            state.value = CreateUpdateBookState.Validate(writer = "Penulis tidak boleh kosong")
            return false
        }

        if (book.year.isNullOrEmpty()){
            state.value = CreateUpdateBookState.Validate(year = "Tahun tidak boleh kosong")
            return false
        }

        if (book.category_id == null){
            toast("Genre tidak boleh kosong")
            return false
        }

        if (book.publisher.isNullOrEmpty()){
            state.value = CreateUpdateBookState.Validate(publisher = "Penerbit tidak boleh kosong")
            return false
        }

        if (book.number_of_pages.isNullOrEmpty()){
            state.value = CreateUpdateBookState.Validate(number_of_pages = "Ketebalan tidak boleh kosong")
            return false
        }

        if (book.description.isNullOrEmpty()){
            state.value = CreateUpdateBookState.Validate(description = "Deskripsi tidak boleh kosong")
            return false
        }

        if (image.value.isNullOrBlank()) {
            toast("Gambar tidak boleh kosong")
        }
        return true
    }

    fun createOrUpdate(token : String, newBook: NewBook){
        isLoading(true)
        val map = setHashMap(newBook)
        println(map)
        bookRepository.createOrUpdate(token, map, setFileImage(), object : SingleResponse<NewBook>{
            override fun onSuccess(data: NewBook?) {
                isLoading(false)
                data?.let { success() }
            }

            override fun onFailure(err: Error) {
                isLoading(false)
                println("create book error" + err.message.toString())
                toast(err.message.toString())
            }

        })
    }

    fun setImage(path : String){
        image.value = path
    }

    private fun setFileImage(): MultipartBody.Part {
        val file = File(image.value!!)
        val requestBodyForFile = RequestBody.create(MediaType.parse("image/*"), file)
        return MultipartBody.Part.createFormData("image", file.name, requestBodyForFile)
    }

    fun setCategoryId(id : String){
        categoryId.value = id
    }

    private fun setHashMap(newBook: NewBook): HashMap<String, RequestBody> {
        val map = HashMap<String, RequestBody>()
        map["category_id"] = createPartFromString(newBook.category_id.toString())
        map["title"] = createPartFromString(newBook.title!!)
        map["description"] = createPartFromString(newBook.description!!)
        map["writer"] = createPartFromString(newBook.writer!!)
        map["publisher"] = createPartFromString(newBook.publisher!!)
        map["year"] = createPartFromString(newBook.year!!)
        map["number_of_pages"] = createPartFromString(newBook.number_of_pages!!)
        return map
    }

    fun listenToState() = state
    fun listenToCategories() = categories
    fun getCategoryId() = categoryId
}

sealed class CreateUpdateBookState{
    data class Loading(var state : Boolean) : CreateUpdateBookState()
    data class ShowToast(var message : String) : CreateUpdateBookState()
    object Success : CreateUpdateBookState()
    object Reset : CreateUpdateBookState()
    data class Validate(
            var title : String? = null,
            var description : String? = null,
            var writer : String? = null,
            var publisher : String? = null,
            var year : String? = null,
            var number_of_pages : String? = null
    ) : CreateUpdateBookState()
}