package com.alfardev.dipinjamin.ui.my_orders

import android.content.Intent
import com.alfardev.dipinjamin.models.Checkout
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.api.load
import com.alfardev.dipinjamin.R
import com.alfardev.dipinjamin.ui.detail_my_order.DetailMyOrderActivity
import kotlinx.android.synthetic.main.list_item_my_orders.view.*

class MyOrderAdapter (private var orders : MutableList<Checkout>, private var myOrderAdapter: MyOrderListener)
    : RecyclerView.Adapter<MyOrderAdapter.ViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.list_item_my_orders, parent, false))
    }

    override fun getItemCount(): Int = orders.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind(orders[position], myOrderAdapter)

    inner class ViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){
        fun bind(checkout: Checkout, myOrderListener: MyOrderListener){
            with(itemView){
                img_book.load(checkout.book!!.image)
                txt_title.text = checkout.book!!.title
                txt_category.text = checkout.book!!.category!!.category
                txt_first_date.text = checkout.first_day_borrow
                txt_last_date.text = checkout.last_day_borrow
                setOnClickListener {
                    myOrderListener.detail(checkout)

                }
//                btn_delete.setOnClickListener {
//                    myOrderListener.delete(checkout)
//                }
            }
        }
    }

    fun updateList(c : List<Checkout>){
        orders.clear()
        orders.addAll(c)
        notifyDataSetChanged()
    }
}