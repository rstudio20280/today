package com.study.today.feature.main

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.study.today.R
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import timber.log.Timber
import java.util.concurrent.TimeUnit

class MainIntro : AppCompatActivity() {
    @SuppressLint("CheckResult")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_intro)

        Single.just(Unit).subscribeOn(Schedulers.io())
            .delay(2500, TimeUnit.MILLISECONDS)
            .map {
                Intent(this, MainActivity::class.java)
            }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { intent, t ->
                t?.let { Timber.e(it) } ?: kotlin.run {
                    startActivity(intent)
                    finish()
                }
            }
    }
}