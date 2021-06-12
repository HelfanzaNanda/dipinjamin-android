package com.alfardev.dipinjamin.ui.search

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import com.alfardev.dipinjamin.R
import com.alfardev.dipinjamin.models.Book
import com.alfardev.dipinjamin.ui.books.BookAdapter
import com.alfardev.dipinjamin.utils.extensions.gone
import com.alfardev.dipinjamin.utils.extensions.showToast
import com.alfardev.dipinjamin.utils.extensions.visible
import kotlinx.android.synthetic.main.activity_search.*
import kotlinx.android.synthetic.main.content_search.*
import kotlinx.android.synthetic.main.toolbar_search.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class SearchActivity : AppCompatActivity() {

    private val searchViewModel : SearchViewModel by viewModel()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)
        setSupportActionBar(findViewById(R.id.toolbar))
        setUpRecycler()
        observe()
    }

    private fun observe() {
        observeState()
        observeBooks()
    }

    private fun setUpRecycler(){
        recycler_view.apply {
            adapter = SearchAdapter(mutableListOf(), this@SearchActivity)
            layoutManager = GridLayoutManager(this@SearchActivity, 2)
        }
    }

    private fun observeState() = searchViewModel.listenToState().observer(this, Observer { handleUiState(it) })
    private fun observeBooks() = searchViewModel.listenToBooks().observe(this, Observer { handleBooks(it) })
    private fun searchBooks(title : String) = searchViewModel.search(title)

    private fun handleBooks(list: List<Book>?) {
        list?.let {
            if (it.isNullOrEmpty()){
                layout_not_found.visible()
                recycler_view.gone()
            }else{
                layout_not_found.gone()
                recycler_view.visible()
                recycler_view.adapter?.let { adapter ->
                    if (adapter is SearchAdapter) adapter.updateList(it)
                }
            }

        }
    }

    private fun handleUiState(searchState: SearchState?) {
        searchState?.let {
            when(it){
                is SearchState.Loading -> handleLoading(it.state)
                is SearchState.ShowToast -> showToast(it.message)
            }
        }
    }

    private fun handleLoading(state: Boolean) {
        if (state) loading.visible() else loading.gone()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        if (id == android.R.id.home){
            finish()
        }
        return super.onOptionsItemSelected(item)
    }



    private fun getPassedKey() = intent.getStringExtra("KEY")
    private fun setFieldSearch(key : String) = et_search.setText(key)

    override fun onResume() {
        super.onResume()
        println("key"+getPassedKey())
        setFieldSearch(getPassedKey()!!)
        searchBooks(getPassedKey()!!)
    }
}