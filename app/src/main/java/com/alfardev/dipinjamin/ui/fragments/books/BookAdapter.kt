package com.alfardev.dipinjamin.ui.fragments.books

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.api.load
import com.alfardev.dipinjamin.R
import com.alfardev.dipinjamin.models.Book
import kotlinx.android.synthetic.main.list_item_my_book.view.*

class BookAdapter (private val books : MutableList<Book>, private val bookListener: BookListener)
    : RecyclerView.Adapter<BookAdapter.ViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.list_item_my_book, parent, false))
    }

    override fun getItemCount() = books.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind(books[position], bookListener)

    inner class ViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){
        fun bind(book: Book, bookListener: BookListener){
            with(itemView){
                book_image.load(book.image)
                book_name.text = book.title
                btn_book_delete.setOnClickListener {
                    bookListener.delete(book)
                }
            }
        }
    }

    fun updateList(c : List<Book>){
        books.clear()
        books.addAll(c)
        notifyDataSetChanged()
    }
}