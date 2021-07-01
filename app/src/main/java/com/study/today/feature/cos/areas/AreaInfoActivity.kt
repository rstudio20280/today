package com.study.today.feature.cos.areas

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.study.today.R
import com.study.today.databinding.ActivityAreaInfoBinding

class AreaInfoActivity : AppCompatActivity() {
    lateinit var binding : ActivityAreaInfoBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this,R.layout.activity_area_info)
        binding.areatext.text = (intent.getSerializableExtra("name") as MapName).name
    }
}