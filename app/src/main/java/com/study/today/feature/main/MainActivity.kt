package com.study.today.feature.main

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.study.today.R
import com.study.today.feature.tour_test.TourFragment

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, TourFragment.newInstance())
                .commitNow()
        }
    }
}