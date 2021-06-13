package com.study.today.feature.cos.areas

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.study.today.databinding.ActivitySelectSecBinding

class SelectSec : AppCompatActivity() {
    // ViewBinding
    private lateinit var binding : ActivitySelectSecBinding
    // RecyclerView 가 불러올 목록
    private val data:MutableList<MapName> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySelectSecBinding.inflate(layoutInflater)
        val view = binding.root
        val member = intent.getSerializableExtra("name") as MapName


        Area.cat2[member.name]?.forEach {
            data.add(MapName(it))
        }

        val adapter = MyAdapter()
        adapter.listData = data
        binding.recyclerView.adapter = adapter


        setContentView(view)
    }
}