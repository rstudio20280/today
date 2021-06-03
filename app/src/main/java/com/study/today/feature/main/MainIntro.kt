package com.study.today.feature.main

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.SystemClock
import com.study.today.R
import com.study.today.feature.main.cos.Drive

class MainIntro : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_intro)

        SystemClock.sleep(3000)
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }
}