package com.alfardev.dipinjamin.ui.my_orders

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.alfardev.dipinjamin.R
import com.alfardev.dipinjamin.models.Checkout
import com.alfardev.dipinjamin.ui.detail_my_order.DetailMyOrderActivity
import com.alfardev.dipinjamin.utils.Constants
import com.alfardev.dipinjamin.utils.extensions.gone
import com.alfardev.dipinjamin.utils.extensions.showToast
import com.alfardev.dipinjamin.utils.extensions.visible
import kotlinx.android.synthetic.main.activity_my_order.*
import kotlinx.android.synthetic.main.content_my_order.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class MyOrderActivity : AppCompatActivity(), MyOrderListener {

    private val myOrderViewModel : MyOrderViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_order)
        setSupportActionBar(findViewById(R.id.toolbar))
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        if (getPassedState()!! == "BORROWER"){
            supportActionBar?.title = "Buku Yang Saya Pinjam"
        }else{
            supportActionBar?.title = "Buku Yang Saya Pinjamkan"
        }

        setUpRecyclerView()
        observe()

    }

    private fun setUpRecyclerView() {
        recycler_view.apply {
            adapter = MyOrderAdapter(mutableListOf(), this@MyOrderActivity)
            layoutManager = LinearLayoutManager(this@MyOrderActivity)
        }
    }

    private fun observe() {
        observeState()
        observeOrders()
    }

    private fun observeState() = myOrderViewModel.listenToState().observer(this, Observer { handleUiState(it) })
    private fun observeOrders() = myOrderViewModel.listenToOrders().observe(this, Observer { handleOrders(it) })

    private fun fetchOrdersByBorrower() = myOrderViewModel.fetchOrdersByBorrower(Constants.getToken(this@MyOrderActivity))
    private fun fetchOrdersByOwner() = myOrderViewModel.fetchOrdersByOwner(Constants.getToken(this@MyOrderActivity))

    private fun getPassedState() = intent.getStringExtra("STATE")

    private fun handleOrders(list: List<Checkout>?) {
        list?.let {
            if (it.isNullOrEmpty()){
                layout_not_found.visible()
                recycler_view.gone()
            }else{
                layout_not_found.gone()
                recycler_view.visible()
                recycler_view.adapter?.let { adapter ->
                    if (adapter is MyOrderAdapter) adapter.updateList(it)
                }
            }
        }
    }

    private fun handleUiState(myOrderState: MyOrderState?) {
        myOrderState?.let {
            when(it){
                is MyOrderState.Loading -> handleLoading(it.state)
                is MyOrderState.ShowToast -> showToast(it.message)
                is MyOrderState.Success -> handleSuccess()
            }
        }
    }

    private fun handleSuccess() {

    }

    private fun handleLoading(state: Boolean) {
        if (state) loading.visible() else loading.gone()
    }

    override fun delete(checkout: Checkout) {
        TODO("Not yet implemented")
    }

    override fun detail(checkout: Checkout) {
        startActivity(Intent(this@MyOrderActivity, DetailMyOrderActivity::class.java). apply {
            if (getPassedState()!! == "BORROWER"){
                putExtra("IS_OWNER", false)
            }else{
                putExtra("IS_OWNER", true)
            }
            putExtra("CHECKOUT", checkout)
        })
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
        if (getPassedState()!! == "BORROWER"){
            fetchOrdersByBorrower()
        }else{
            fetchOrdersByOwner()
        }
    }
}