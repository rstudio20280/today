package com.study.today.model

import com.google.gson.annotations.SerializedName

data class TourResponse(
    @SerializedName("numOfRows") val numOfRows:Int,
    @SerializedName("pageNo") val pageNo:Int,
    @SerializedName("totalCount") val totalCount:Int,
) {
    //by lazy를 사용해서 하려고 했지만 Gson으로 생성된 객체는 lazy를 사용할 수 없다.
    private var lastPage : Int? = null
    fun lastPage(): Int {
        if (lastPage == null)
            lastPage = (totalCount / numOfRows)  + (if (totalCount%numOfRows != 0) 1 else 0)
        return lastPage!!
    }
}
