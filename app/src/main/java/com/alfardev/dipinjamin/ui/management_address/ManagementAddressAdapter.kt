package com.alfardev.dipinjamin.ui.management_address

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.alfardev.dipinjamin.R
import com.alfardev.dipinjamin.models.DeliveryAddress
import com.alfardev.dipinjamin.utils.Constants
import kotlinx.android.synthetic.main.list_item_management_address.view.*

class ManagementAddressAdapter (private val deliveryAddresses : MutableList<DeliveryAddress>,
                                private val managementAddressViewModel: ManagementAddressViewModel,
                                private val managementAddressListener: ManagementAddressListener)
    : RecyclerView.Adapter<ManagementAddressAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.list_item_management_address, parent, false))
    }

    override fun getItemCount(): Int = deliveryAddresses.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind(deliveryAddresses[position], managementAddressViewModel, managementAddressListener)

    inner class ViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){
        @SuppressLint("SetTextI18n")
        fun bind(deliveryAddress: DeliveryAddress,
                 managementAddressViewModel: ManagementAddressViewModel,
                 managementAddressListener: ManagementAddressListener){
            with(itemView){
                txt_name.text = "Nama : "+deliveryAddress.name
                txt_phone.text = "Phone : "+deliveryAddress.phone
                txt_address.text = "Alamat : "+deliveryAddress.address
                txt_kecamatan.text = "Kecamatan : "+deliveryAddress.kecamatan
                txt_kabupaten.text = "Kabupaten : "+deliveryAddress.kabupaten
                txt_provinsi.text = "Provinsi : "+deliveryAddress.provinsi
                txt_kode_pos.text = "Kode Pos : "+deliveryAddress.kode_pos
                btn_delete.setOnClickListener {
                    AlertDialog.Builder(context).apply {
                        setMessage(context.getString(R.string.message_delete))
                        setPositiveButton("Ya"){dialog, _ ->
                            dialog.dismiss()
                            managementAddressViewModel.deleteDeliveryAddress(Constants.getToken(context), deliveryAddress.id.toString())
                        }
                        setNegativeButton("Tidak"){dialog, _ ->
                            dialog.dismiss()
                        }
                    }.show()
                }
                btn_edit.setOnClickListener {
                    managementAddressListener.edit(deliveryAddress)
                }
            }
        }
    }

    fun updateList(c : List<DeliveryAddress>){
        deliveryAddresses.clear()
        deliveryAddresses.addAll(c)
        notifyDataSetChanged()
    }

}