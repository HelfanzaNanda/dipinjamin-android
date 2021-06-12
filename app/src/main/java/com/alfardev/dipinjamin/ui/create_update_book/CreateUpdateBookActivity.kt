package com.alfardev.dipinjamin.ui.create_update_book

import android.net.Uri
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.alfardev.dipinjamin.R
import com.alfardev.dipinjamin.models.Book
import com.alfardev.dipinjamin.models.Category
import com.alfardev.dipinjamin.models.NewBook
import com.alfardev.dipinjamin.utils.Constants
import com.alfardev.dipinjamin.utils.extensions.gone
import com.alfardev.dipinjamin.utils.extensions.showToast
import com.alfardev.dipinjamin.utils.extensions.visible
import gun0912.tedimagepicker.builder.TedImagePicker
import kotlinx.android.synthetic.main.activity_create_update_book.*
import kotlinx.android.synthetic.main.content_create_update_book.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class CreateUpdateBookActivity : AppCompatActivity() {

    private val createUpdateBookViewModel : CreateUpdateBookViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_update_book)
        setSupportActionBar(findViewById(R.id.toolbar))
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        if (isUpdate()){
            supportActionBar?.title = getString(R.string.text_update_book)
        }else{
            supportActionBar?.title = getString(R.string.text_create_book)
        }
        observe()
        chooseImage()
        createOrUpdate()
    }

    private fun chooseImage() {
        btn_add_image.setOnClickListener {
            TedImagePicker.with(this).image().title(getString(R.string.choose_image)).start {uri -> selectedImage(uri) }
        }
    }

    private fun selectedImage(uri: Uri) {
        val path = Constants.getRealPathFromURI(this@CreateUpdateBookActivity, uri)
        showToast("selected image")
        createUpdateBookViewModel.setImage(path)
    }

    private fun observe() {
        observeState()
        observeCategories()
    }

    private fun observeState() = createUpdateBookViewModel.listenToState().observer(this, Observer { handleUiState(it) })
    private fun observeCategories() = createUpdateBookViewModel.listenToCategories().observe(this, Observer { handleCategories(it) })

    private fun handleCategories(list: List<Category>?) {
        list?.let {
            val categories = mutableListOf<String>()
            it.map { category -> categories.add(category.category!!)  }
            val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, categories ).apply {
                setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            }
            categories_spinner.adapter = adapter
            categories_spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onNothingSelected(parent: AdapterView<*>?) { }

                override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                    val category = it.find { c -> c.category == categories[position]  }
                    createUpdateBookViewModel.setCategoryId(category!!.id.toString())
                }

            }
        }
    }

    private fun createOrUpdate(){
        btn_save.setOnClickListener {
            if (isUpdate()){
                showToast("belum di bikin")
            }else{
                val book = setDataClassField()
                println("book" +book)
                if (createUpdateBookViewModel.validate(book)){
                    createUpdateBookViewModel.createOrUpdate(
                            Constants.getToken(this@CreateUpdateBookActivity),
                            book)
                }
            }
        }
    }

    private fun setDataClassField(): NewBook {
        val title = input_title.text.toString().trim()
        val writer = input_writer.text.toString().trim()
        val year = input_year.text.toString().trim()
        val publisher = input_publisher.text.toString().trim()
        val numberOfPage = input_number_of_page.text.toString().trim()
        val desc = input_desc.text.toString().trim()
        val categoryId = createUpdateBookViewModel.getCategoryId().value
        return NewBook(
                title = title,
                writer = writer,
                year = year,
                publisher = publisher,
                number_of_pages = numberOfPage,
                description = desc,
                category_id = categoryId!!.toInt()
        )
    }

    private fun handleUiState(bookState: CreateUpdateBookState?) {
        bookState?.let {
            when(it){
                is CreateUpdateBookState.Loading -> handleLoading(it.state)
                is CreateUpdateBookState.ShowToast -> showToast(it.message)
                is CreateUpdateBookState.Success -> handleSuccess()
                is CreateUpdateBookState.Reset -> handleReset()
                is CreateUpdateBookState.Validate -> handleValidate(it)
            }
        }
    }

    private fun handleValidate(validate: CreateUpdateBookState.Validate) {
        validate.title?.let { setErrorTitle(it) }
        validate.writer?.let { setErrorWriter(it) }
        validate.year?.let { setErrorYear(it) }
        validate.publisher?.let { setErrorPublisher(it) }
        validate.number_of_pages?.let { setErrorNumberOfPage(it) }
        validate.description?.let { setErrorDescription(it) }
    }

    private fun handleReset() {
        setErrorTitle(null)
        setErrorWriter(null)
        setErrorYear(null)
        setErrorPublisher(null)
        setErrorNumberOfPage(null)
        setErrorDescription(null)
    }

    private fun handleSuccess() {
        finish()
        if (isUpdate()){
            showToast(getString(R.string.message_success_updated_data))
        }else{
            showToast(getString(R.string.message_success_created_data))
        }
    }

    private fun handleLoading(state: Boolean) {
        if (state) loading.visible() else loading.gone()
        btn_save.isEnabled = !state
    }

    private fun setImage(path : String) = createUpdateBookViewModel.setImage(path)
    private fun fetchCategories() = createUpdateBookViewModel.fetchCategories()
    private fun isUpdate() = intent.getBooleanExtra("IS_UPDATE", false)

    private fun setErrorTitle(err : String?) { error_title.error = err }
    private fun setErrorWriter(err : String?) { error_writer.error = err }
    private fun setErrorYear(err : String?) { error_year.error = err }
    private fun setErrorPublisher(err : String?) { error_publisher.error = err }
    private fun setErrorNumberOfPage(err : String?) { error_number_of_page.error = err }
    private fun setErrorDescription(err : String?) { error_desc.error = err }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        if (id == android.R.id.home){
            finish()
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onResume() {
        super.onResume()
        fetchCategories()
    }
}