package com.alfardev.dipinjamin.ui.books

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.api.load
import com.alfardev.dipinjamin.R
import com.alfardev.dipinjamin.models.Book
import com.alfardev.dipinjamin.ui.detail_book.DetailBookActivity
import kotlinx.android.synthetic.main.list_item_home.view.*

class BookAdapter (private val books : MutableList<Book>, private val context: Context)
    : RecyclerView.Adapter<BookAdapter.ViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.list_item_book, parent, false))
    }

    override fun getItemCount() = books.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind(books[position], context)

    inner class ViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){
        fun bind(book: Book, context: Context){
            with(itemView){
                book_image.load(book.image)
                book_name.text = book.title
                setOnClickListener {
                    context.startActivity(Intent(context, DetailBookActivity::class.java).apply {
                        putExtra("BOOK_ID", book.id)
                    })
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