package com.alfardev.dipinjamin.ui.fragments.books

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import com.alfardev.dipinjamin.R
import com.alfardev.dipinjamin.models.Book
import com.alfardev.dipinjamin.utils.Constants
import com.alfardev.dipinjamin.utils.extensions.alertNotLogin
import com.alfardev.dipinjamin.utils.extensions.gone
import com.alfardev.dipinjamin.utils.extensions.showToast
import com.alfardev.dipinjamin.utils.extensions.visible
import kotlinx.android.synthetic.main.fragment_book.view.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class BookFragment : Fragment(R.layout.fragment_book){

    private val bookViewModel : BookViewModel by viewModel()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (isLoggedIn()){
            setUpRecycler()
            observe()
        }else{
            requireActivity().alertNotLogin(requireActivity(), getString(R.string.message_not_login))
        }
    }

    private fun setUpRecycler(){
        requireView().recycler_view.apply {
            adapter = BookAdapter(mutableListOf(), requireActivity())
            layoutManager = GridLayoutManager(requireActivity(), 2)
        }
    }

    private fun observe() {
        observeState()
        observeBooks()
    }

    private fun isLoggedIn() = !Constants.getToken(requireActivity()).equals("UNDEFINED")
    private fun observeState() = bookViewModel.listenToState().observer(viewLifecycleOwner, Observer { handleUiState(it) })
    private fun observeBooks() = bookViewModel.listenToMyBooks().observe(viewLifecycleOwner, Observer { handleMyBooks(it) })

    private fun handleMyBooks(list: List<Book>?) {
        list?.let {
            requireView().recycler_view.adapter?.let { adapter ->
                if (adapter is BookAdapter) adapter.updateList(it)
            }
        }
    }

    private fun handleUiState(bookState: BookState?) {
        bookState?.let {
            when(it){
                is BookState.Loading -> handleLoading(it.state)
                is BookState.ShowToast -> requireActivity().showToast(it.message)
            }
        }
    }

    private fun handleLoading(state: Boolean) {
        if (state) requireView().loading.visible() else requireView().loading.gone()
    }

    private fun fetchMyBooks() = bookViewModel.fetchMyBooks(Constants.getToken(requireActivity()))

    override fun onResume() {
        super.onResume()
        fetchMyBooks()
    }
}