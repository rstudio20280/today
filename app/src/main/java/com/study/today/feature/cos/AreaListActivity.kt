package com.study.today.feature.cos

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.study.today.databinding.ActivityAreaListBinding
import com.study.today.feature.cos.areas.Area
import com.study.today.feature.cos.areas.MapName
import com.study.today.feature.cos.areas.MyAdapter
import com.study.today.feature.cos.areas.SelectSec

class AreaListActivity : AppCompatActivity() {
    // ViewBinding
    private lateinit var binding: ActivityAreaListBinding

    // RecyclerView 가 불러올 목록
    private val data: MutableList<MapName> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAreaListBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        initialize() // data 값 초기화
        refreshRecyclerView() // recyclerView 데이터 바인딩


    }

    private fun initialize() {
        Area.cat1.forEach {
            data.add(MapName(it))
        }
    }

    private fun refreshRecyclerView() {
        val adapter = MyAdapter(listener = { member ->
            val intent = Intent(this, SelectSec::class.java)
            intent.putExtra("name", member)
            startActivity(intent)
        })
        adapter.listData = data
        binding.recyclerView.adapter = adapter
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
    }
}

