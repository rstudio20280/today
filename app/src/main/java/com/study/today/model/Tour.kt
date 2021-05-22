package com.study.today.model

import com.google.gson.annotations.SerializedName

data class Tour(
    val title:String,       //제목
    val addr1:String?,      //주소
    val addr2:String?,      //상세주소
    val areacode:String,    //지역코드
    val cat1:String,        //대분류
    val cat2:String,        //중분류
    val cat3:String,        //소분류
    @SerializedName("contentid") val id:Int,    //콘텐츠ID
    @SerializedName("contenttypeid") val typeId:Int,   //콘텐츠타입ID
    val createdtime:String, //등록일
    val firstimage:String?, //대표이미지(원본)
    val firstimage2:String?,    //대표이미지(썸네일)
    val mapx:Double,        //GPS X좌표 (경도)
    val mapy:Double,        //GPS Y좌표 (위도)
    val mlevel:Int,         //Map Level
    val modifiedtime:String,//수정일
    val readcount:String,   //조회수
    val sigungucode:String, //시군구코드
    val tel:String?,        //전화번호
    val dist:Int?,  //위도경도값으로 검색했을 경우에만 값이 존재한다.
) {
    fun getAddress() = (addr1?:"") + if(addr2 != null) " $addr2" else ""
}
