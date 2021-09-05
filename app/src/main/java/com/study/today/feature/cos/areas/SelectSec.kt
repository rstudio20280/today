package com.study.today.feature.cos.areas

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
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
        setContentView(view)
        refreshRecyclerView() // recyclerView 데이터 바인딩

        Area.cat2[member.name]?.forEach {
            data.add(MapName(it))
        }
    }

    private fun refreshRecyclerView() {
        val adapter = AreaListAdapter(listener = { member ->
            val intent = Intent(this, AreaInfoActivity::class.java)
            intent.putExtra("name", member)
            startActivity(intent)
        })

        adapter.listData = data
        binding.recyclerView.adapter = adapter
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
    }
}