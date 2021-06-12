package com.alfardev.dipinjamin.ui.fragments.home

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.view.inputmethod.EditorInfo
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.alfardev.dipinjamin.R
import com.alfardev.dipinjamin.models.Book
import com.alfardev.dipinjamin.models.Image
import com.alfardev.dipinjamin.ui.books.BookActivity
import com.alfardev.dipinjamin.ui.search.SearchActivity
import com.alfardev.dipinjamin.utils.Constants
import com.alfardev.dipinjamin.utils.extensions.alertInfo
import com.alfardev.dipinjamin.utils.extensions.gone
import com.alfardev.dipinjamin.utils.extensions.showToast
import com.alfardev.dipinjamin.utils.extensions.visible
import com.smarteist.autoimageslider.IndicatorView.animation.type.IndicatorAnimationType
import com.smarteist.autoimageslider.SliderAnimations
import com.smarteist.autoimageslider.SliderView
import kotlinx.android.synthetic.main.fragment_home.view.*
import kotlinx.android.synthetic.main.toolbar_search.view.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class HomeFragment : Fragment(R.layout.fragment_home) {

    private val homeViewModel: HomeViewModel by viewModel()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        requireActivity().actionBar?.hide()
        setUpImageSlider()
        setUpRecyclerView()
        observe()
        gotoAll()
        search()
    }

    private fun setUpImageSlider() {
        requireView().image_slider.apply {
            setSliderAdapter(ImageSliderAdapter(requireActivity(), mutableListOf()))
            setIndicatorAnimation(IndicatorAnimationType.WORM)
            setSliderTransformAnimation(SliderAnimations.SIMPLETRANSFORMATION)
            autoCycleDirection = SliderView.AUTO_CYCLE_DIRECTION_BACK_AND_FORTH
            indicatorSelectedColor = Color.WHITE
            indicatorUnselectedColor = Color.GRAY
            scrollTimeInSec = 4
        }.startAutoCycle()
    }

    private fun setUpRecyclerView() {
        requireView().recycler_book_new.apply {
            adapter = NewBookAdapter(mutableListOf(), requireActivity())
            layoutManager = LinearLayoutManager(requireActivity(), LinearLayoutManager.HORIZONTAL, false)
        }
        requireView().recycler_book_most.apply {
            adapter = MostBookAdapter(mutableListOf(), requireActivity())
            layoutManager = LinearLayoutManager(requireActivity(), LinearLayoutManager.HORIZONTAL, false)
        }
        requireView().recycler_book_recommended.apply {
            adapter = RecommendedBookAdapter(mutableListOf(), requireActivity())
            layoutManager = LinearLayoutManager(requireActivity(), LinearLayoutManager.HORIZONTAL, false)
        }
    }

    private fun observe() {
        observeState()
        observeBanners()
        observeNewBooks()
        observeMostBooks()
        observeRecommendedBooks()
    }

    private fun observeState() = homeViewModel.listenToState().observer(viewLifecycleOwner, Observer { handleUiState(it) })
    private fun observeBanners() = homeViewModel.listenToBanners().observe(viewLifecycleOwner, Observer { handleBanners(it) })
    private fun observeNewBooks() = homeViewModel.listenToNewBooks().observe(viewLifecycleOwner, Observer { handleNewBooks(it) })
    private fun observeMostBooks() = homeViewModel.listenToMostBooks().observe(viewLifecycleOwner, Observer { handleMostBooks(it) })
    private fun observeRecommendedBooks() = homeViewModel.listenToRecommendedBooks().observe(viewLifecycleOwner, Observer { handleRecommendedBooks(it) })

    private fun handleBanners(list: List<Image>?) {
        list?.let {
            requireView().image_slider.sliderAdapter?.let { pagerAdapter ->
                if (pagerAdapter is ImageSliderAdapter) pagerAdapter.changelist(it)
            }
        }
    }

    private fun handleRecommendedBooks(list: List<Book>?) {
        list?.let {
            val books = it.take(3)
            requireView().recycler_book_recommended.adapter?.let {adapter ->
                if (adapter is RecommendedBookAdapter) adapter.updateList(books)
            }
        }
    }

    private fun handleMostBooks(list: List<Book>?) {
        list?.let {
            val books = it.take(3)
            requireView().recycler_book_most.adapter?.let {adapter ->
                if (adapter is MostBookAdapter) adapter.updateList(books)
            }
        }
    }

    private fun handleNewBooks(list: List<Book>?) {
        list?.let {
            val books = it.take(3)
            requireView().recycler_book_new.adapter?.let {adapter ->
                if (adapter is NewBookAdapter) adapter.updateList(books)
            }
        }
    }

    private fun handleUiState(homeState: HomeState?) {
        homeState?.let {
            when(it){
                is HomeState.Loading -> handleLoading(it.state)
                is HomeState.ShowToast -> requireActivity().showToast(it.message)
                is HomeState.ShowAlert -> requireActivity().alertInfo(it.message)
            }
        }
    }

    private fun handleLoading(state: Boolean) {
        if (state) requireView().loading.visible() else requireView().loading.gone()
    }

    private fun gotoAll(){
        requireView().btn_view_all_book_new.setOnClickListener {
            startActivity(Intent(requireActivity(), BookActivity::class.java).apply {
                putExtra("BOOK", getString(R.string.key_new_book))
            })
        }

        requireView().btn_view_all_book_most.setOnClickListener {
            startActivity(Intent(requireActivity(), BookActivity::class.java).apply {
                putExtra("BOOK", getString(R.string.key_most_book))
            })
        }


        requireView().btn_view_all_book_recommended.setOnClickListener {
            startActivity(Intent(requireActivity(), BookActivity::class.java).apply {
                putExtra("BOOK", getString(R.string.key_recommended_book))
            })
        }
    }

    private fun search(){
        requireView().et_search.setOnEditorActionListener { v, actionId, event ->
            if (requireView().et_search.text.isNullOrEmpty()){
                requireActivity().showToast("harus di isi")
                false
            }else{
                if (actionId == EditorInfo.IME_ACTION_SEARCH){
                    startActivity(Intent(requireActivity(), SearchActivity::class.java)
                        .putExtra("KEY", requireView().et_search.text.toString().trim())
                    )
                }
                true
            }

        }
    }

    private fun fetchNewBooks() = homeViewModel.new(Constants.getToken(requireActivity()))
    private fun fetchMostBooks() = homeViewModel.most(Constants.getToken(requireActivity()))
    private fun fetchRecommendedBooks() = homeViewModel.recommended(Constants.getToken(requireActivity()))
    private fun fetchBanners() = homeViewModel.banners()

    override fun onResume() {
        super.onResume()
        fetchMostBooks()
        fetchNewBooks()
        fetchRecommendedBooks()
        fetchBanners()
    }
}