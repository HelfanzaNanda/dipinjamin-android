package com.alfardev.dipinjamin.ui.fragments.categories

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import com.alfardev.dipinjamin.R
import com.alfardev.dipinjamin.models.Category
import com.alfardev.dipinjamin.utils.extensions.gone
import com.alfardev.dipinjamin.utils.extensions.showToast
import com.alfardev.dipinjamin.utils.extensions.visible
import kotlinx.android.synthetic.main.fragment_category.view.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class CategoryFragment : Fragment(R.layout.fragment_category){

    private val categoryViewModel : CategoryViewModel by viewModel()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpRecyclerView()
        observe()
    }

    private fun setUpRecyclerView() {
        requireView().recycler_view.apply {
            adapter = CategoryAdapter(mutableListOf(), requireActivity())
            layoutManager = GridLayoutManager(requireActivity(), 2)
        }
    }

    private fun observe() {
        observeState()
        observeCategories()
    }

    private fun observeState() = categoryViewModel.listenToState().observer(viewLifecycleOwner, Observer { handleUiState(it) })
    private fun observeCategories() = categoryViewModel.listenToCategories().observe(viewLifecycleOwner, Observer { handleCategories(it) })

    private fun handleUiState(categoryState: CategoryState?) {
        categoryState?.let {
            when(it){
                is CategoryState.ShowToast -> requireActivity().showToast(it.message)
                is CategoryState.Loading -> handleLoading(it.state)
            }
        }
    }

    private fun handleLoading(state: Boolean) {
        if (state) requireView().loading.visible() else requireView().loading.gone()
    }

    private fun handleCategories(list: List<Category>?) {
        list?.let {
            requireView().recycler_view.adapter?.let { adapter ->
                if (adapter is CategoryAdapter) adapter.updateList(it)
            }
        }
    }

    private fun fetchCategories() = categoryViewModel.fetchCategories()
    override fun onResume() {
        super.onResume()
        fetchCategories()
    }

}