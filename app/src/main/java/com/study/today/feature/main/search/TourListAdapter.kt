package com.study.today.feature.main.search

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.study.today.databinding.ItemTourBinding
import com.study.today.model.Tour

class TourListAdapter(
    var clickListener: ((Int, Tour) -> Unit)? = null,
    var longClickListener: ((Int, Tour) -> Boolean)? = null,
    var bookmarkClickListener: ((Int, Tour, Boolean) -> Unit)? = null
) : ListAdapter<Tour, TourListAdapter.TourViewHolder>(object : DiffUtil.ItemCallback<Tour>() {
    override fun areItemsTheSame(oldItem: Tour, newItem: Tour): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Tour, newItem: Tour): Boolean {
        return oldItem.modifiedtime == newItem.modifiedtime
    }

}) {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TourViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ItemTourBinding.inflate(layoutInflater, parent, false)
        val vh = TourViewHolder(binding)
        binding.root.setOnClickListener {
            clickListener?.invoke(vh.adapterPosition, getItem(vh.adapterPosition))
        }
        binding.root.setOnLongClickListener {
            longClickListener?.invoke(vh.adapterPosition, getItem(vh.adapterPosition)) ?: false
        }
        binding.bookmarkBtn.setOnClickListener { bookmarkClickListener?.invoke(vh.adapterPosition, getItem(vh.adapterPosition), binding.bookmarkBtn.isChecked) }
        return vh
    }

    override fun onBindViewHolder(holder: TourViewHolder, position: Int) {
        val tour = getItem(position)
        holder.onBind(tour)
    }

    inner class TourViewHolder(
        val binding: ItemTourBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun onBind(model: Tour) {
            binding.tour = model
            binding.position = "${adapterPosition + 1} "
            Glide.with(binding.root.context)
                .load(model.firstimage2)
//                .placeholder(R.drawable.ic_waiting)
                .optionalCircleCrop()

                .into(binding.imageView)
        }

    }

}