package com.alfardev.dipinjamin.ui.fragments.home

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import coil.api.load
import com.alfardev.dipinjamin.R
import com.alfardev.dipinjamin.models.Image
import com.smarteist.autoimageslider.SliderViewAdapter
import kotlinx.android.synthetic.main.list_item_image_slider.view.*

class ImageSliderAdapter(private val context: Context, private val images : MutableList<Image>) :
    SliderViewAdapter<ImageSliderAdapter.SliderAdapterVH>() {

    @SuppressLint("InflateParams")
    override fun onCreateViewHolder(parent: ViewGroup): SliderAdapterVH {
        return SliderAdapterVH(LayoutInflater.from(parent.context).inflate(R.layout.list_item_image_slider, null))
    }

    override fun getCount(): Int = images.size

    override fun onBindViewHolder(viewHolder: SliderAdapterVH?, position: Int) = viewHolder!!.bind(images[position], context)

    class SliderAdapterVH(itemView: View) : ViewHolder(itemView){
        fun bind(image: Image, context: Context){
            with(itemView){ image_banner.load(image.filename!!) }
        }
    }

    fun changelist(c : List<Image>){
        images.clear()
        images.addAll(c)
        notifyDataSetChanged()
    }
}