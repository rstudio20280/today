package com.study.today.model.remote

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.study.today.BuildConfig
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

class ServiceGenerator {
    companion object {
        val gson: Gson = GsonBuilder()
            .setLenient()
            .create()

        val logging = HttpLoggingInterceptor().also {
            if (BuildConfig.DEBUG) {
                it.level = HttpLoggingInterceptor.Level.BODY
            } else {
                it.level = HttpLoggingInterceptor.Level.NONE
            }
        }

        fun createTour(): TourApi {
            val httpClient = OkHttpClient.Builder()
            httpClient.addInterceptor(logging)

            val retorfit: Retrofit = Retrofit.Builder()
                .baseUrl(TourApi.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(httpClient.build())
                .build()

            return retorfit.create(TourApi::class.java)
        }
    }
}