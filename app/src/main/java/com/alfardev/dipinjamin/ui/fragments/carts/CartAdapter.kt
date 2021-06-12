package com.alfardev.dipinjamin.ui.fragments.carts

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import coil.api.load
import com.alfardev.dipinjamin.R
import com.alfardev.dipinjamin.models.Cart
import com.alfardev.dipinjamin.utils.Constants
import kotlinx.android.synthetic.main.list_item_cart.view.*

class CartAdapter (private var carts : MutableList<Cart>,
                   private var cartListener: CartListener)
    : RecyclerView.Adapter<CartAdapter.ViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.list_item_cart, parent, false))
    }

    override fun getItemCount(): Int = carts.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind(carts[position], cartListener)

    inner class ViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){
        fun bind(cart: Cart, cartListener: CartListener){
            with(itemView){
                img_book.load(cart.book!!.image)
                txt_title.text = cart.book!!.title
                txt_category.text = cart.book!!.category!!.category
                btn_delete.setOnClickListener {
                    cartListener.delete(cart)
                }
            }
        }
    }

    fun updateList(c : List<Cart>){
        carts.clear()
        carts.addAll(c)
        notifyDataSetChanged()
    }
}