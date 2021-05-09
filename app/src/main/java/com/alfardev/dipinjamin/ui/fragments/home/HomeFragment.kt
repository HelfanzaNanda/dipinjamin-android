package com.alfardev.dipinjamin.ui.fragments.home

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.alfardev.dipinjamin.R
import com.alfardev.dipinjamin.models.Book
import com.alfardev.dipinjamin.ui.books.BookActivity
import com.alfardev.dipinjamin.utils.extensions.gone
import com.alfardev.dipinjamin.utils.extensions.showToast
import com.alfardev.dipinjamin.utils.extensions.visible
import kotlinx.android.synthetic.main.fragment_home.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class HomeFragment : Fragment(R.layout.fragment_home) {

    private val homeViewModel: HomeViewModel by viewModel()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        requireActivity().actionBar?.hide()
        setUpRecyclerView()
        observe()
        gotoAll()
    }

    private fun setUpRecyclerView() {
        recycler_book_new.apply {
            adapter = NewBookAdapter(mutableListOf(), requireActivity())
            layoutManager = LinearLayoutManager(requireActivity(), LinearLayoutManager.HORIZONTAL, false)
        }
        recycler_book_most.apply {
            adapter = MostBookAdapter(mutableListOf(), requireActivity())
            layoutManager = LinearLayoutManager(requireActivity(), LinearLayoutManager.HORIZONTAL, false)
        }
        recycler_book_recommended.apply {
            adapter = RecommendedBookAdapter(mutableListOf(), requireActivity())
            layoutManager = LinearLayoutManager(requireActivity(), LinearLayoutManager.HORIZONTAL, false)
        }
    }

    private fun observe() {
        observeState()
        observeNewBooks()
        observeMostBooks()
        observeRecommendedBooks()
    }

    private fun observeState() = homeViewModel.listenToState().observer(viewLifecycleOwner, Observer { handleUiState(it) })
    private fun observeNewBooks() = homeViewModel.listenToNewBooks().observe(viewLifecycleOwner, Observer { handleNewBooks(it) })
    private fun observeMostBooks() = homeViewModel.listenToMostBooks().observe(viewLifecycleOwner, Observer { handleMostBooks(it) })
    private fun observeRecommendedBooks() = homeViewModel.listenToRecommendedBooks().observe(viewLifecycleOwner, Observer { handleRecommendedBooks(it) })

    private fun handleRecommendedBooks(list: List<Book>?) {
        list?.let {
            val books = it.take(3)
            recycler_book_recommended.adapter?.let {adapter ->
                if (adapter is RecommendedBookAdapter) adapter.updateList(books)
            }
        }
    }

    private fun handleMostBooks(list: List<Book>?) {
        list?.let {
            val books = it.take(3)
            recycler_book_most.adapter?.let {adapter ->
                if (adapter is MostBookAdapter) adapter.updateList(books)
            }
        }
    }

    private fun handleNewBooks(list: List<Book>?) {
        list?.let {
            val books = it.take(3)
            recycler_book_new.adapter?.let {adapter ->
                if (adapter is NewBookAdapter) adapter.updateList(books)
            }
        }
    }

    private fun handleUiState(homeState: HomeState?) {
        homeState?.let {
            when(it){
                is HomeState.Loading -> handleLoading(it.state)
                is HomeState.ShowToast -> requireActivity().showToast(it.message)
            }
        }
    }

    private fun handleLoading(state: Boolean) {
        if (state) loading.visible() else loading.gone()
    }

    private fun gotoAll(){
        btn_view_all_book_new.setOnClickListener {
            startActivity(Intent(requireActivity(), BookActivity::class.java).apply {
                putExtra("BOOK", getString(R.string.key_new_book))
            })
        }

        btn_view_all_book_most.setOnClickListener {
            startActivity(Intent(requireActivity(), BookActivity::class.java).apply {
                putExtra("BOOK", getString(R.string.key_most_book))
            })
        }


        btn_view_all_book_recommended.setOnClickListener {
            startActivity(Intent(requireActivity(), BookActivity::class.java).apply {
                putExtra("BOOK", getString(R.string.key_recommended_book))
            })
        }
    }

    private fun fetchNewBooks() = homeViewModel.new()
    private fun fetchMostBooks() = homeViewModel.most()
    private fun fetchRecommendedBooks() = homeViewModel.recommended()

    override fun onResume() {
        super.onResume()
        fetchMostBooks()
        fetchNewBooks()
        fetchRecommendedBooks()
    }
}