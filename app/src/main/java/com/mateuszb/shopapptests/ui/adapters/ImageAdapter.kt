package com.mateuszb.shopapptests.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.RequestManager
import com.mateuszb.shopapptests.R
import kotlinx.android.synthetic.main.item_image.view.*
import javax.inject.Inject

class ImageAdapter @Inject constructor(
    private val glide: RequestManager
) : RecyclerView.Adapter<ImageAdapter.ImageViewHolder>(){
    class ImageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    private val diffCallback = object : DiffUtil.ItemCallback<String>() {
        override fun areItemsTheSame(oldItem: String, newItem: String): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: String, newItem: String): Boolean {
            return oldItem == newItem
        }
    }

    private val differ = AsyncListDiffer(this, diffCallback)

    var images: List<String>
        get() = differ.currentList
        set(value) = differ.submitList(value)

    private var onItemClickListener: ((String) -> Unit)? = null

    fun setOnItemClickListener(listener: (String) -> Unit){
        onItemClickListener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
        return ImageViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_image,
            parent,
            false)
        )
    }

    override fun getItemCount(): Int {
        return images.size
    }

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        val url = images[position]
        holder.itemView.apply {
            glide.load(url).into(ivShoppingImage)

            setOnClickListener{
                onItemClickListener?.let {
                    it(url)
                }
            }

        }
    }
}