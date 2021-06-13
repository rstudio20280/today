package com.study.today.feature.cos.areas

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.study.today.databinding.ItemRecyclerBinding

class MyAdapter(val listener : ((Map) -> Unit)? = null) : RecyclerView.Adapter<MyAdapter.Holder>(){

    var listData = mutableListOf<Map>()
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
        fun setData(member: Map){
            binding.textView.text = member.name
            binding.root.setOnClickListener {
                listener?.invoke(member)
            }
        }
    }
}


