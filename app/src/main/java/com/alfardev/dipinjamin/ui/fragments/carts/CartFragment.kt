package com.alfardev.dipinjamin.ui.fragments.carts

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.alfardev.dipinjamin.R
import com.alfardev.dipinjamin.models.Cart
import com.alfardev.dipinjamin.ui.login.LoginActivity
import com.alfardev.dipinjamin.utils.Constants
import com.alfardev.dipinjamin.utils.extensions.gone
import com.alfardev.dipinjamin.utils.extensions.showToast
import com.alfardev.dipinjamin.utils.extensions.visible
import kotlinx.android.synthetic.main.fragment_cart.view.*
import kotlinx.android.synthetic.main.unauthorized.view.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class CartFragment : Fragment(), CartListener{

    private val cartViewModel : CartViewModel by viewModel()

    override fun onCreateView(inflater: LayoutInflater,container: ViewGroup?,savedInstanceState: Bundle?): View? {
        if (isLoggedIn()){
            return inflater.inflate(R.layout.fragment_cart, container, false)
        }
        return inflater.inflate(R.layout.unauthorized, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (isLoggedIn()){
            setUpRecyclerView()
            observe()
        }else{
            requireView().btn_login.setOnClickListener {
                startActivity(
                    Intent(requireActivity(), LoginActivity::class.java)
                    .putExtra("EXPECT_RESULT", false))
            }
        }

    }

    private fun setUpRecyclerView() {
        requireView().recycler_view.apply {
            adapter = CartAdapter(mutableListOf(), this@CartFragment)
            layoutManager = LinearLayoutManager(requireActivity())
        }
    }

    private fun observe(){
        observeState()
        observeCarts()
    }

    private fun observeState() = cartViewModel.listenToState().observer(viewLifecycleOwner, Observer { handleUiState(it) })
    private fun observeCarts() = cartViewModel.listenToCarts().observe(viewLifecycleOwner, Observer { handleCarts(it) })
    private fun fetchCarts() = cartViewModel.getCarts(Constants.getToken(requireActivity()))

    private fun handleCarts(list: List<Cart>?) {
        list?.let {
            if (it.isNotEmpty()){
                requireView().layout_not_found.gone()
                requireView().recycler_view.visible()
                requireView().recycler_view.adapter?.let { adapter ->
                    if (adapter is CartAdapter) adapter.updateList(it)
                }
            }else{
                requireView().recycler_view.gone()
                requireView().layout_not_found.visible()
            }
        }
    }

    private fun handleUiState(cartState: CartState?) {
        cartState?.let {
            when(it){
                is CartState.Loading -> handleLoading(it.state)
                is CartState.ShowToast -> requireActivity().showToast(it.message)
                is CartState.Success -> handleSuccess()
            }
        }
    }

    private fun handleSuccess() {
        fetchCarts()
        requireActivity().showToast(getString(R.string.success_delete))
    }

    private fun handleLoading(b: Boolean) {
        if (b) requireView().loading.visible() else requireView().loading.gone()
    }

    private fun isLoggedIn() = !Constants.getToken(requireActivity()).equals(getString(R.string.undefined))

    override fun onResume() {
        super.onResume()
        if (isLoggedIn()) fetchCarts()
    }

    override fun delete(cart: Cart) {
        AlertDialog.Builder(requireActivity()).apply {
            setMessage(context.getString(R.string.are_you_sure))
            setPositiveButton("Ya"){dialog, _ ->
                dialog.dismiss()
                cartViewModel.deleteCart(Constants.getToken(requireActivity()), cart.id!!)
            }
            setNegativeButton("Tidak"){dialog, _ ->
                dialog.dismiss()
            }
        }.show()
    }
}