package com.alfardev.dipinjamin.ui.checkout

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.alfardev.dipinjamin.R
import com.alfardev.dipinjamin.models.CreateCheckout
import com.alfardev.dipinjamin.models.DeliveryAddress
import com.alfardev.dipinjamin.ui.management_address.ManagementAddressActivity
import com.alfardev.dipinjamin.utils.Constants
import com.alfardev.dipinjamin.utils.extensions.gone
import com.alfardev.dipinjamin.utils.extensions.showToast
import com.alfardev.dipinjamin.utils.extensions.visible
import gun0912.tedimagepicker.builder.TedImagePicker
import kotlinx.android.synthetic.main.activity_checkout.*
import kotlinx.android.synthetic.main.content_checkout.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class CheckoutActivity : AppCompatActivity() {

    private val checkoutViewModel : CheckoutViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_checkout)
        setSupportActionBar(findViewById(R.id.toolbar))
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        observe()
        chooseKtp()
        order()
        addAddress()
    }

    private fun observe() {
        observeState()
        observeDeliveryAddresses()
    }

    private fun observeState() = checkoutViewModel.listenToState().observer(this, Observer { handleUiState(it) })
    private fun observeDeliveryAddresses() = checkoutViewModel.listenToDeliveryAddresses().observe(this, Observer { handleDeliveryAddresses(it) })
    private fun setDeliveryAddressId(id : String) = checkoutViewModel.setDeliveryAddressId(id)

    private fun fetchDeliveryAddresses() = checkoutViewModel.fetchDeliveryAddresses(Constants.getToken(this@CheckoutActivity))

    private fun getDeliveryAddressId() = checkoutViewModel.getDeliveryAddressId()
    private fun getPassedBookId() = intent.getIntExtra("BOOK_ID", 0)
    private fun getPassedOwnerId() = intent.getIntExtra("OWNER_ID", 0)

    private fun handleDeliveryAddresses(list: List<DeliveryAddress>?) {
        list?.let {
            val addresses = mutableListOf(getString(R.string.choose_address))
            it.map { a -> addresses.add(a.address!!) }
            val adapter = ArrayAdapter(this@CheckoutActivity, android.R.layout.simple_spinner_item, addresses)
                    .apply {
                        setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                    }
            spinner_delivery_address.adapter = adapter
            spinner_delivery_address.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
                override fun onNothingSelected(parent: AdapterView<*>?) {
                    TODO("Not yet implemented")
                }

                override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                    if (addresses[position] != getString(R.string.choose_address)){
                        val addressSelected = it.find { a -> a.address == addresses[position] }
                        //println("selected address "+addressSelected)
                        setDeliveryAddressId(addressSelected!!.id.toString())
                    }
                }

            }
        }
    }

    private fun handleUiState(checkoutState: CheckoutState?) {
        checkoutState?.let {
            when(it){
                is CheckoutState.ShowToast -> showToast(it.message)
                is CheckoutState.Loading -> handleLoading(it.state)
                is CheckoutState.Success -> handleSuccess()
            }
        }
    }

    private fun handleSuccess() {
        showToast("berhasil order")
        finish()
    }

    private fun handleLoading(state: Boolean) {
        if (state) loading.visible() else loading.gone()
        btn_add_address.isEnabled = !state
        btn_add_ktp.isEnabled = !state
        btn_checkout.isEnabled = !state
    }

    private fun chooseKtp(){
        btn_add_ktp.setOnClickListener {
            TedImagePicker.with(this).image().title(getString(R.string.choose_image)).start { uri -> selectedImage(uri) }
        }
    }

    private fun selectedImage(uri: Uri) {
        val path = Constants.getRealPathFromURI(this@CheckoutActivity, uri)
        showToast("selected ktp")
        checkoutViewModel.setImage(path)
    }

    private fun order(){
        btn_checkout.setOnClickListener {
            val duration = spinner_duration.selectedItem.toString()

            if (checkoutViewModel.validate(duration)){
                val createCheckout = CreateCheckout(
                        book_id = getPassedBookId().toString(),
                        owner_id = getPassedOwnerId().toString(),
                        duration = duration,
                        delivery_address_id = getDeliveryAddressId()
                )
                checkoutViewModel.checkout(Constants.getToken(this@CheckoutActivity), createCheckout)
            }
        }
    }

    private fun addAddress() {
        btn_add_address.setOnClickListener {
            startActivity(Intent(this@CheckoutActivity, ManagementAddressActivity::class.java))
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
        fetchDeliveryAddresses()
    }
}