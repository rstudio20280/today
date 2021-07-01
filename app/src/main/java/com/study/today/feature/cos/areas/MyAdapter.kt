package com.study.today.feature.cos.areas

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.study.today.databinding.ItemRecyclerBinding

class MyAdapter(val listener : ((MapName) -> Unit)? = null) : RecyclerView.Adapter<MyAdapter.Holder>(){

    var listData = mutableListOf<MapName>()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val binding = ItemRecyclerBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return Holder(binding)
    }
    override fun onBindViewHolder(holder: Holder, position: Int) {
        val member = listData[position]
        holder.setData(member)
//        holder.itemView.setOnClickListener{
//            var intent = Intent(holder.binding.root.context, AreaInfoActivity::class.java)
//            intent.putExtra("name",listData[position].name.toString())
//            holder.binding.root.context.startActivity(intent)
//        }
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


