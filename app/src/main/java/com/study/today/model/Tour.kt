package com.study.today.model

import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "bookmarks")
data class Tour(
    var title: String,       //제목
    var addr1: String?,      //주소
    var addr2: String?,      //상세주소
    var areacode: String,    //지역코드
    var cat1: String,        //대분류
    var cat2: String,        //중분류
    var cat3: String,        //소분류
    @SerializedName("contentid")
    @PrimaryKey var id: Int,    //콘텐츠ID
    @SerializedName("contenttypeid") var typeId: Int,   //콘텐츠타입ID
    var createdtime: String, //등록일
    var firstimage: String?, //대표이미지(원본)
    var firstimage2: String?,    //대표이미지(썸네일)
    var mapx: Double,        //GPS X좌표 (경도)
    var mapy: Double,        //GPS Y좌표 (위도)
    var mlevel: Int,         //Map Level
    var modifiedtime: String,//수정일
    var readcount: String,   //조회수
    var sigungucode: String, //시군구코드
    var tel: String?,        //전화번호
    @Ignore var dist: Int?,  //위도경도값으로 검색했을 경우에만 값이 존재한다.
    var bookmark: Boolean    //북마크 여부
) {
    constructor() : this(
        "", null, null, "", "", "", "", 0, 0, "", null, null, 0.0, 0.0, 0, "",
        "", "", null, null, false
    )

    fun getAddress() = (addr1 ?: "") + if (addr2 != null) " $addr2" else ""

    companion object {
        fun getDummy(): MutableList<Tour> {
            val ret = mutableListOf<Tour>()
            repeat(10) {
                ret.add(
                    Tour(
                        "더미$it",
                        "주소$it",
                        null,
                        it.toString(),
                        "대분류$it",
                        "중분류$it",
                        "소분류$it",
                        it,
                        it,
                        "2021-07-11",
                        null,
                        null,
                        21.12312312,
                        123.12314132,
                        1,
                        "2021-07-11",
                        "3",
                        "1",
                        "010-1234-1234",
                        2000,
                        true
                    )
                )
            }
            return ret
        }
    }
}
