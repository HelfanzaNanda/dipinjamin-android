package com.alfardev.dipinjamin.ui.fragments.books

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import com.alfardev.dipinjamin.R
import com.alfardev.dipinjamin.models.Book
import com.alfardev.dipinjamin.ui.create_update_book.CreateUpdateBookActivity
import com.alfardev.dipinjamin.ui.login.LoginActivity
import com.alfardev.dipinjamin.utils.Constants
import com.alfardev.dipinjamin.utils.extensions.gone
import com.alfardev.dipinjamin.utils.extensions.showToast
import com.alfardev.dipinjamin.utils.extensions.visible
import kotlinx.android.synthetic.main.fragment_book.view.*
import kotlinx.android.synthetic.main.unauthorized.view.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class BookFragment : Fragment(), BookListener{

    private val bookViewModel : BookViewModel by viewModel()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        if (isLoggedIn()){
            return inflater.inflate(R.layout.fragment_book, container, false)
        }
        return inflater.inflate(R.layout.unauthorized, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (isLoggedIn()){
            setUpRecycler()
            observe()
            addBook()
        }else{
            requireView().btn_login.setOnClickListener {
                startActivity(Intent(requireActivity(), LoginActivity::class.java).putExtra("EXPECT_RESULT", false))
            }
        }
    }

    private fun addBook(){
        requireView().fab_add_book.setOnClickListener {
            startActivity(Intent(requireActivity(), CreateUpdateBookActivity::class.java))
        }
    }

    private fun setUpRecycler(){
        requireView().recycler_view.apply {
            adapter = BookAdapter(mutableListOf(), this@BookFragment)
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
            if (it.isNotEmpty()){
                requireView().layout_not_found.gone()
                requireView().recycler_view.visible()
                requireView().recycler_view.adapter?.let { adapter ->
                    if (adapter is BookAdapter) adapter.updateList(it)
                }
            }else{
                requireView().recycler_view.gone()
                requireView().layout_not_found.visible()
            }
        }
    }

    private fun handleUiState(bookState: BookState?) {
        bookState?.let {
            when(it){
                is BookState.Loading -> handleLoading(it.state)
                is BookState.ShowToast -> requireActivity().showToast(it.message)
                is BookState.SuccessDelete -> handleSuccessDelete()
            }
        }
    }

    private fun handleSuccessDelete() {
        requireActivity().showToast("berhasil delete buku")
        fetchMyBooks()
    }

    private fun handleLoading(state: Boolean) {
        if (state) requireView().loading.visible() else requireView().loading.gone()
    }

    private fun fetchMyBooks() = bookViewModel.fetchMyBooks(Constants.getToken(requireActivity()))
    private fun sizeBooks() = bookViewModel.listenToMyBooks().value?.size

    override fun onResume() {
        super.onResume()
        if (isLoggedIn()){
            fetchMyBooks()
        }
    }

    override fun delete(book: Book) {
        AlertDialog.Builder(requireActivity()).apply {
            setMessage("apakah anda yakin?")
            setNegativeButton("Tidak"){dialog, _ ->
                dialog.dismiss()
            }
            setPositiveButton("Ya"){dialog, _ ->
                bookViewModel.deleteBook(Constants.getToken(requireActivity()), book.id!!)
                dialog.dismiss()
            }
        }.show()
    }
}