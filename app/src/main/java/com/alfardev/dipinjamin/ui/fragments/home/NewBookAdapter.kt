package com.alfardev.dipinjamin.ui.fragments.home

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.api.load
import com.alfardev.dipinjamin.R
import com.alfardev.dipinjamin.models.Book
import kotlinx.android.synthetic.main.list_item_home.view.*

class NewBookAdapter (private val books : MutableList<Book>, private val context: Context)
    : RecyclerView.Adapter<NewBookAdapter.ViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.list_item_home, parent, false))
    }

    override fun getItemCount() = books.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind(books[position], context)

    inner class ViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){
        fun bind(book: Book, context: Context){
            with(itemView){
                println(book.images[0].filename)
                book_image.load(book.images[0].filename)
                book_name.text = book.title
            }
        }
    }

    fun updateList(c : List<Book>){
        books.clear()
        books.addAll(c)
        notifyDataSetChanged()
    }
}