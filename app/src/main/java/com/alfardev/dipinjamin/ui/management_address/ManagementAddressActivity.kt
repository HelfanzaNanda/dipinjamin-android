package com.alfardev.dipinjamin.ui.management_address

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.alfardev.dipinjamin.R
import com.alfardev.dipinjamin.models.DeliveryAddress
import com.alfardev.dipinjamin.models.Kabupaten
import com.alfardev.dipinjamin.models.Kecamatan
import com.alfardev.dipinjamin.models.Provinsi
import com.alfardev.dipinjamin.utils.Constants
import com.alfardev.dipinjamin.utils.extensions.gone
import com.alfardev.dipinjamin.utils.extensions.showToast
import com.alfardev.dipinjamin.utils.extensions.visible
import com.google.android.material.bottomsheet.BottomSheetBehavior
import kotlinx.android.synthetic.main.activity_management_address.*
import kotlinx.android.synthetic.main.bottom_sheet_management_address.*
import kotlinx.android.synthetic.main.content_management_address.*
import org.koin.androidx.viewmodel.ext.android.viewModel


class ManagementAddressActivity : AppCompatActivity(), ManagementAddressListener {

    private val managementAddressViewModel : ManagementAddressViewModel by viewModel()
    private var bottomSheetBehavior: BottomSheetBehavior<*>? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_management_address)
        setSupportActionBar(findViewById(R.id.toolbar))
        supportActionBar?.title = getString(R.string.text_management_address)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        setUpBottomSheet()
        setUpRecyclerView()
        observe()
        addAddress()
        createOrUpdate()
    }

    private fun setUpBottomSheet() {
        bottomSheetBehavior = BottomSheetBehavior.from(bottom_sheet)
        bottomSheetBehavior!!.addBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback(){
            override fun onSlide(bottomSheet: View, slideOffset: Float) { }

            override fun onStateChanged(bottomSheet: View, newState: Int) {
                when (newState) {
                    BottomSheetBehavior.STATE_EXPANDED -> {
                        fab_add_address.gone()
                    }
                    BottomSheetBehavior.STATE_COLLAPSED -> {
                        fab_add_address.visible()
                    }
                }
            }

        })
    }

    private fun observe() {
        observeState()
        observeDeliveryAddresses()
        observeProvinsi()
    }

    private fun observeState() = managementAddressViewModel.listenToState().observer(this, Observer { handleUiState(it) })
    private fun observeDeliveryAddresses() = managementAddressViewModel.listenToDeliveryAddresses().observe(this, Observer { handleDeliveryAddresses(it) })
    private fun observeProvinsi() = managementAddressViewModel.listenToProvinsi().observe(this, Observer { handleProvinsi(it) })
    private fun observeKabupaten() = managementAddressViewModel.listenTokabupaten().observe(this, Observer { handleKabupaten(it) })
    private fun observeKecamatan() = managementAddressViewModel.listenToKecamatan().observe(this, Observer { handleKecamatan(it) })
    private fun observeAddress() = managementAddressViewModel.listenToDeliveryAddress().observe(this, Observer { handleDeliveryAddress(it) })

    private fun fetchDeliveryAddresses() = managementAddressViewModel.fetchDeliveryAddresses(Constants.getToken(this@ManagementAddressActivity))
    private fun fetchProvinsi() = managementAddressViewModel.fetchProvinsi()
    private fun fetchKabupaten(provinsi_id : String) = managementAddressViewModel.fetchKabupaten(provinsi_id)
    private fun fetchKecamatan(kota_id : String) = managementAddressViewModel.fetchKecamatan(kota_id)

    private fun setProvinsi(id : String, name : String) = managementAddressViewModel.setProvinsi(id, name)
    private fun setKabupaten(id : String, name : String) = managementAddressViewModel.setKabupaten(id, name)
    private fun setKecamatan(id : String, name : String) = managementAddressViewModel.setKecamatan(id, name)

    private fun setProvinsiNames(names: List<String>) = managementAddressViewModel.setProvinsiNames(names)
    private fun setKabupatenNames(names: List<String>) = managementAddressViewModel.setKabupatenNames(names)
    private fun setKecamatanNames(names: List<String>) = managementAddressViewModel.setKecamatanNames(names)

    private fun setDeliveryAddressId(id : Int) = managementAddressViewModel.setDeliveryAddressId(id)

    private fun getProvinsiId() = managementAddressViewModel.getProvinsiId()
    private fun getKabupatenId() = managementAddressViewModel.getKabupatenId()
    private fun getKecamatanId() = managementAddressViewModel.getKecamatanId()
    private fun getDeliveryAddress(id : String) = managementAddressViewModel.getDeliveryAddress(Constants.getToken(this@ManagementAddressActivity), id)

    private fun getProvinsiName() = managementAddressViewModel.getProvinsiName()
    private fun getKabupatenName() = managementAddressViewModel.getKabupatenName()
    private fun getKecamatanName() = managementAddressViewModel.getKecamatanName()

    private fun getProvinsiNames() = managementAddressViewModel.getProvinsiNames()
    private fun getKabupatenNames() = managementAddressViewModel.getKabupatenNames()
    private fun getKecamatanNames() = managementAddressViewModel.getKecamatanNames()

    private fun getDeliveryAddressId() = managementAddressViewModel.getDeliveryAddressId()


    private fun handleProvinsi(list: List<Provinsi>?) {
        list?.let {
            val provinsi = mutableListOf(getString(R.string.choose_province))
            it.map { p -> provinsi.add(p.name!!) }
            setProvinsiNames(provinsi)

            val adapter = ArrayAdapter(this@ManagementAddressActivity, android.R.layout.simple_spinner_item, provinsi )
                    .apply { setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item) }
            provinsi_spinner.adapter = adapter
            provinsi_spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onNothingSelected(parent: AdapterView<*>?) { }

                override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                    if (provinsi[position] != getString(R.string.choose_province)){
                        val selected_provinsi = it.find { p -> p.name == provinsi[position]  }
                        setProvinsi(selected_provinsi!!.id.toString(), provinsi[position])
                        fetchKabupaten(selected_provinsi.id.toString())
                        observeKabupaten()
                    }
                }
            }
        }
    }

    private fun handleKabupaten(list: List<Kabupaten>?) {
        list?.let {
            val kabupaten = mutableListOf(getString(R.string.choose_kab))
            it.map { k -> kabupaten.add(k.name!!)  }
            setKabupatenNames(kabupaten)
            val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, kabupaten )
                    .apply { setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item) }
            kabupaten_spinner.adapter = adapter

            kabupaten_spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onNothingSelected(parent: AdapterView<*>?) { }

                override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                    if (kabupaten[position] != getString(R.string.choose_kab)){
                        val selected_kabupaten = it.find { k -> k.name == kabupaten[position]  }
                        setKabupaten(selected_kabupaten!!.id.toString(), kabupaten[position])
                        fetchKecamatan(selected_kabupaten.id.toString())
                        observeKecamatan()
                    }
                }
            }
        }
    }

    private fun handleKecamatan(list: List<Kecamatan>?) {
        list?.let {
            val kecamatan = mutableListOf(getString(R.string.choose_kec))
            it.map { k -> kecamatan.add(k.name!!)  }
            setKecamatanNames(kecamatan)
            val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, kecamatan )
                    .apply { setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item) }
            kecamatan_spinner.adapter = adapter
            kecamatan_spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onNothingSelected(parent: AdapterView<*>?) { }

                override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                    if (kecamatan[position] != getString(R.string.choose_kec)){
                        val selected_kecamatan = it.find { k -> k.name == kecamatan[position]  }
                        setKecamatan(selected_kecamatan!!.id.toString(), kecamatan[position])
                    }
                }

            }
        }
    }

    private fun handleDeliveryAddresses(list: List<DeliveryAddress>?) {
        list?.let {
            if (it.isNotEmpty()){
                layout_not_found.gone()
                recycler_view.adapter?.let { adapter ->
                    if (adapter is ManagementAddressAdapter) adapter.updateList(it)
                }
            }else{
                layout_not_found.visible()
            }
        }
    }

    private fun handleUiState(addressState: ManagementAddressState?) {
        addressState?.let {
            when(it){
                is ManagementAddressState.Loading -> handleLoading(it.state)
                is ManagementAddressState.ShowToast -> showToast(it.message)
                is ManagementAddressState.Success -> handleSuccess()
            }
        }
    }

    private fun handleSuccess() {
        if (bottomSheetBehavior!!.state == BottomSheetBehavior.STATE_EXPANDED)
            bottomSheetBehavior!!.state = BottomSheetBehavior.STATE_COLLAPSED
        fetchDeliveryAddresses()
        fetchProvinsi()
        showToast(getString(R.string.message_change_data))
    }

    private fun handleLoading(state: Boolean) {
        if (state) loading.visible() else loading.gone()
        fab_add_address.isEnabled = !state
        btn_save.isEnabled = !state
    }

    private fun setUpRecyclerView() {
        recycler_view.apply {
            adapter = ManagementAddressAdapter(mutableListOf(), managementAddressViewModel, this@ManagementAddressActivity)
            layoutManager = LinearLayoutManager(this@ManagementAddressActivity)
        }
    }

    private fun handleDeliveryAddress(deliveryAddress: DeliveryAddress?) {
        deliveryAddress?.let {
            input_name.setText(it.name)
            input_phone.setText(it.phone)
            input_address.setText(it.address)
            input_kode_pos.setText(it.kode_pos)
            val province = getProvinsiNames()?.filter { s -> s == it.provinsi }?.get(0)
            val kabupaten = getKabupatenNames()?.filter { s -> s == it.kabupaten }?.get(0)
            val kecamatan = getKecamatanNames()?.filter { s -> s == it.kecamatan }?.get(0)
            println(kecamatan)
//            provinsi_spinner.setSelection(getProvinsiNames()!!.indexOf(province))
//            kabupaten_spinner.setSelection(getKabupatenNames()!!.indexOf(kabupaten))
//            kecamatan_spinner.setSelection(getKecamatanNames()!!.indexOf(kecamatan))
        }
    }

    private fun createOrUpdate() {
        btn_save.setOnClickListener {
            if (isUpdate()){
                val deliveryAddress = setUpField()
                deliveryAddress.id = getDeliveryAddressId()
                managementAddressViewModel.createUpdate(Constants.getToken(this@ManagementAddressActivity), deliveryAddress)
            }else{
                val deliveryAddress = setUpField()
                managementAddressViewModel.createUpdate(Constants.getToken(this@ManagementAddressActivity), deliveryAddress)
            }
        }
    }

    private fun setUpField(): DeliveryAddress {
        val name = input_name.text.toString().trim()
        val phone = input_phone.text.toString().trim()
        val provinsi_id = getProvinsiId()
        val provinsi_name = getProvinsiName()
        val kabupaten_id = getKabupatenId()
        val kabupaten_name = getKabupatenName()
        val kecamatan_id = getKecamatanId()
        val kecamatan_name = getKecamatanName()
        val address = input_address.text.toString().trim()
        val kode_pos = input_kode_pos.text.toString().trim()
        val deliveryAddress = DeliveryAddress(
                name = name, phone = phone, provinsi_id = provinsi_id, provinsi = provinsi_name,
                kabupaten_id = kabupaten_id, kabupaten = kabupaten_name, kecamatan_id =  kecamatan_id,
                kecamatan = kecamatan_name, address = address, kode_pos = kode_pos
        )
        return deliveryAddress
    }

    private fun resetField() {
        input_name.text = null
        input_phone.text = null
        input_address.text = null
        input_kode_pos.text = null

    }

    private fun isUpdate() = intent.getBooleanExtra("IS_UPDATE", false)

    private fun addAddress() {
        fab_add_address.setOnClickListener {
            resetField()
            if (bottomSheetBehavior!!.state == BottomSheetBehavior.STATE_COLLAPSED) {
                bottomSheetBehavior!!.state = BottomSheetBehavior.STATE_EXPANDED
            } else {
                bottomSheetBehavior!!.state = BottomSheetBehavior.STATE_COLLAPSED
            }
        }
    }

    override fun edit(deliveryAddress: DeliveryAddress) {
        resetField()
        setDeliveryAddressId(deliveryAddress.id!!)
        fetchKabupaten(deliveryAddress.provinsi_id!!)
        observeKabupaten()
        fetchKecamatan(deliveryAddress.kabupaten_id!!)
        observeKecamatan()
        getDeliveryAddress(deliveryAddress.id.toString())
        observeAddress()

        if (bottomSheetBehavior!!.state == BottomSheetBehavior.STATE_COLLAPSED) {
            bottomSheetBehavior!!.state = BottomSheetBehavior.STATE_EXPANDED
        } else {
            bottomSheetBehavior!!.state = BottomSheetBehavior.STATE_COLLAPSED
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
        fetchProvinsi()
    }


}