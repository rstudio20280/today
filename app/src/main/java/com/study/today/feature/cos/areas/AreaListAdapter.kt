package com.study.today.feature.cos.areas

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.study.today.databinding.ItemRecyclerBinding

class AreaListAdapter(val listener : ((MapName) -> Unit)? = null) : RecyclerView.Adapter<AreaListAdapter.Holder>(){

    var listData = mutableListOf<MapName>()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val binding = ItemRecyclerBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return Holder(binding)

    }
    override fun onBindViewHolder(holder: Holder, position: Int) {
        val member = listData[position]
        holder.setData(member)
    }
    override fun getItemCount(): Int {
        return listData.size
    }

    inner class Holder(val binding: ItemRecyclerBinding) : RecyclerView.ViewHolder(binding.root){
        fun setData(member: MapName){
            binding.textView.text = member.name
            binding.root.setOnClickListener {
                listener?.invoke(member)
            }
        }
    }
}


