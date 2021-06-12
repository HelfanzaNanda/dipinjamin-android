package com.alfardev.dipinjamin.ui.detail_book

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import com.google.android.material.appbar.CollapsingToolbarLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import coil.api.load
import com.alfardev.dipinjamin.R
import com.alfardev.dipinjamin.models.Book
import com.alfardev.dipinjamin.ui.checkout.CheckoutActivity
import com.alfardev.dipinjamin.ui.login.LoginActivity
import com.alfardev.dipinjamin.utils.Constants
import com.alfardev.dipinjamin.utils.extensions.gone
import com.alfardev.dipinjamin.utils.extensions.showToast
import com.alfardev.dipinjamin.utils.extensions.visible
import kotlinx.android.synthetic.main.activity_detail_book.*
import kotlinx.android.synthetic.main.content_detail_book.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class DetailBookActivity : AppCompatActivity() {

    private val detailBookViewModel : DetailBookViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_book)
        setSupportActionBar(findViewById(R.id.toolbar))
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        observe()
    }

    private fun observe() {
        observeState()
        observeBook()
    }

    private fun observeState() = detailBookViewModel.listenToState().observer(this, Observer { handleUiState(it) })
    private fun observeBook() = detailBookViewModel.listenToBook().observe(this, Observer { handleBook(it) })

    private fun handleBook(book: Book?) {
        book?.let {
            book_image.load(it.image)
            text_title_book.text = it.title
            text_available.text = if (it.isAvailable) getString(R.string.available) else getString(R.string.isnt_available)
            txt_writer.text = it.writer
            txt_year.text = it.year
            txt_category.text = it.category!!.category
            txt_publisher.text = it.publisher
            text_thickness.text = it.number_of_pages + getString(R.string.pages)
            text_viewer.text = it.viewer.toString()
            txt_sinopsis.text = it.description

            btn_checkout.isEnabled = it.isAvailable
            btn_add_to_cart.isEnabled = it.isAvailable

            println("btn"+btn_checkout.isEnabled)
            println("btn"+btn_add_to_cart.isEnabled)

            if (it.isAvailable){
                addToCart(it.id!!, it.owner!!.id!!)
                btn_add_to_cart.isEnabled = isUserAddedCart()!!
                checkout(it.id!!, it.owner!!.id!!)
            }

        }

    }

    private fun handleUiState(state: DetailBookState?) {
        state?.let {
            when(it) {
                is DetailBookState.Loading -> handleLoading(it.state)
                is DetailBookState.ShowToast -> showToast(it.message)
                is DetailBookState.SuccessAddToCart -> handleSuccessAddToCart()
            }
        }
    }

    private fun handleSuccessAddToCart() {
        btn_add_to_cart.isEnabled = false
        showToast("Berhasil Menambahkan Ke Cart")
    }

    private fun handleLoading(state: Boolean) {
        btn_add_to_cart.isEnabled = !state
        btn_checkout.isEnabled = !state
        if (state) loading.visible() else loading.gone()
    }

    private fun getPassedBook() = intent.getIntExtra("BOOK_ID", 0)
    private fun getBook() = detailBookViewModel.getBook(getPassedBook())
    private fun checkUserIsAdded(bookId : Int) = detailBookViewModel.checkUserIsAdded(Constants.getToken(this@DetailBookActivity), bookId)
    private fun isUserAddedCart() = detailBookViewModel.isUserAddedCart()

    private fun addToCart(bookId: Int, ownerId : Int){
        btn_add_to_cart.setOnClickListener {
            if (isLoggedIn()){
                detailBookViewModel.addToCart(Constants.getToken(this), bookId, ownerId)
            }else{
                alertNotLogin()
            }
        }
    }

    private fun checkout(bookId: Int, ownerId: Int){
        btn_checkout.setOnClickListener {
            if (isLoggedIn()){
                startActivity(Intent(this, CheckoutActivity::class.java).apply {
                    putExtra("BOOK_ID", bookId)
                    putExtra("OWNER_ID", ownerId)
                })
            }else{
                alertNotLogin()
            }
        }
    }

    private fun alertNotLogin(){
        AlertDialog.Builder(this).apply {
            setMessage("silahkan login dulu")
            setPositiveButton("Ya"){dialog, _ ->
                dialog.dismiss()
                startActivity(Intent(this@DetailBookActivity, LoginActivity::class.java)
                        .putExtra("EXPECT_RESULT", true))
            }
        }.show()
    }

    private fun isLoggedIn() = !Constants.getToken(this@DetailBookActivity).equals(getString(R.string.undefined))
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        if (id == android.R.id.home){
            finish()
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onResume() {
        super.onResume()
        getBook()
        checkUserIsAdded(getPassedBook())
    }
}