package com.study.today.model.remote

import com.google.gson.JsonObject
import io.reactivex.Single
import retrofit2.http.*

interface TourApi {
    companion object {
        const val BASE_URL = "http://api.visitkorea.or.kr/"
        const val BASE_ENDPOINT = "openapi/service/rest/KorService/"
        const val KEY1 =
            "JtaAnoVKjZcwOIfcC0aP6KmkPE7wV2/waeJIbpIYKMhBsPSGBKZiq0VaZEix2tVgaqwMAjGCtn85eHjZbnhrDA=="
        //Decoding 된 인증키
        const val KEY2 =
            "JtaAnoVKjZcwOIfcC0aP6KmkPE7wV2%2FwaeJIbpIYKMhBsPSGBKZiq0VaZEix2tVgaqwMAjGCtn85eHjZbnhrDA%3D%3D"
        //Encoding 된 인증키
    }

    @GET(BASE_ENDPOINT +"searchKeyword")
    fun searchWithKeyWord(
        @Query("keyword") word: String,
        @Query("pageNo") pageNum: Int = 1,
        @Query("numOfRows") numOfRows: Int = 10, //한 페이지 결과 수
        @Query("ServiceKey") key: String = KEY1,
        @Query("MobileApp") appName: String = "DevyStudy",
        @Query("MobileOS") os: String = "AND",
        @Query("listYN") listYN: String = "Y",
        @Query("_type") type: String = "json"
    ): Single<JsonObject>

    @GET(BASE_ENDPOINT +"locationBasedList")
    fun searchWithLatLng(
        @Query("mapY") lat: Double,
        @Query("mapX") lng: Double,
        @Query("radius") radius: Int = 1000, //단위(m), Max = 20000 (20km)
        @Query("pageNo") pageNum: Int = 1,
        @Query("numOfRows") numOfRows: Int = 20, //한 페이지 결과 수
        @Query("ServiceKey") key: String = KEY1,
        @Query("MobileApp") appName: String = "DevyStudy",
        @Query("MobileOS") os: String = "AND",
        @Query("listYN") listYN: String = "Y",
        @Query("_type") type: String = "json"
    ): Single<JsonObject>


    @FormUrlEncoded
    @POST("api url")
    fun test(
        @Field("access_token") token: String?,
        @Field("code") authorization_code: String?
    ): Single<HashMap<String, Any>>

}