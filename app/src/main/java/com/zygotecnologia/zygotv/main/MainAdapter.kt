package com.zygotecnologia.zygotv.main

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.squareup.picasso.Picasso
import com.zygotecnologia.zygotv.R
import com.zygotecnologia.zygotv.R.id.iv_show_poster
import com.zygotecnologia.zygotv.R.id.tv_show_title
import com.zygotecnologia.zygotv.model.Show
import com.zygotecnologia.zygotv.utils.ImageUrlBuilder

class MainAdapter(var mItemClickListener: OnClickDetailsInterface, private var shows: List<Show>) :
    RecyclerView.Adapter<MainAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.show_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(shows[position])
    }

    interface OnClickDetailsInterface {
        fun onItemClick(clickedItem: Int, shows: List<Show>)
    }

    override fun getItemCount() = shows.size

    fun setElementList(show: List<Show>) {
        shows = show
        this.notifyDataSetChanged()
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView),
        View.OnClickListener {

        init {
            this.itemView.setOnClickListener(this)
        }

        fun bind(show: Show) {
            val textView: TextView = itemView.findViewById(tv_show_title)
            textView.text = show.name
            val imageView: ImageView = itemView.findViewById(iv_show_poster)

            Picasso.get().load(show.posterPath?.let { ImageUrlBuilder.buildBackdropUrl(it) })
                .apply { RequestOptions.placeholderOf(R.drawable.image_placeholder) }
                .into(imageView)
        }

        override fun onClick(v: View?) {
            mItemClickListener.onItemClick(adapterPosition, shows)
        }
    }
}