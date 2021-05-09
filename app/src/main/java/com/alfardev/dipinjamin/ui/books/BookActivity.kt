package com.alfardev.dipinjamin.ui.books

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import com.alfardev.dipinjamin.R
import com.alfardev.dipinjamin.models.Book
import com.alfardev.dipinjamin.utils.extensions.gone
import com.alfardev.dipinjamin.utils.extensions.showToast
import com.alfardev.dipinjamin.utils.extensions.visible
import kotlinx.android.synthetic.main.content_book.*
import org.koin.androidx.viewmodel.ext.android.viewModel


class BookActivity : AppCompatActivity() {

    private val bookViewModel : BookViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_book)
        setSupportActionBar(findViewById(R.id.toolbar))
        supportActionBar?.title = getPassedKeyBook()
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        setUpRecycler()
        observe()
    }

    private fun setUpRecycler(){
        recycler_view.apply {
            adapter = BookAdapter(mutableListOf(), this@BookActivity)
            layoutManager = GridLayoutManager(this@BookActivity, 2)
        }
    }

    private fun observe() {
        observeState()
        observeBooks()
    }

    private fun observeState() = bookViewModel.listenToState().observer(this, Observer { handleUiState(it) })
    private fun observeBooks() = bookViewModel.listenToBooks().observe(this, Observer { handleBooks(it) })

    private fun handleBooks(list: List<Book>?) {
        list?.let {
            recycler_view.adapter?.let { adapter ->
                if (adapter is BookAdapter) adapter.updateList(it)
            }
        }
    }

    private fun handleUiState(bookState: BookState?) {
        bookState?.let {
            when(it){
                is BookState.Loading -> handleLoading(it.state)
                is BookState.ShowToast -> showToast(it.message)
            }
        }
    }

    private fun handleLoading(state: Boolean) {
        if (state) loading.visible() else loading.gone()
    }

    private fun getPassedKeyBook() = intent.getStringExtra("BOOK")
    private fun fetchBooks() {
        println(getPassedKeyBook())
        when {
            getPassedKeyBook() == getString(R.string.key_new_book) -> {
                bookViewModel.new()
            }
            getPassedKeyBook() == getString(R.string.key_most_book) -> {
                bookViewModel.most()
            }
            getPassedKeyBook() == getString(R.string.key_recommended_book) -> {
                bookViewModel.recommended()
            }
        }
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        if (id == android.R.id.home){
            finish()
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onResume() {
        super.onResume()
        fetchBooks()
    }
}