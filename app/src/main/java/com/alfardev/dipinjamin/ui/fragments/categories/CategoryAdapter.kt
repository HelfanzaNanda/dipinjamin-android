package com.alfardev.dipinjamin.ui.fragments.categories

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.alfardev.dipinjamin.R
import com.alfardev.dipinjamin.models.Category
import com.alfardev.dipinjamin.ui.books.BookActivity
import kotlinx.android.synthetic.main.list_item_category.view.*

class CategoryAdapter (private val categories : MutableList<Category>, private val context: Context)
    : RecyclerView.Adapter<CategoryAdapter.ViewHolder>(){


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.list_item_category, parent, false))
    }

    override fun getItemCount() = categories.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind(categories[position], context)

    fun updateList(c : List<Category>){
        categories.clear()
        categories.addAll(c)
        notifyDataSetChanged()
    }

    inner class ViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){
        fun  bind(category: Category, context: Context){
            with(itemView){
                category_name.text = category.category
                setOnClickListener {
                    context.startActivity(Intent(context, BookActivity::class.java).apply {
                        putExtra("BOOK", context.getString(R.string.book_by_category))
                        putExtra("CATEGORY_ID", category.id)
                    })
                }
            }
        }
    }
}