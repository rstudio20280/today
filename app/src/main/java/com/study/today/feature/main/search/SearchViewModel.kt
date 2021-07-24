package com.study.today.feature.main.search

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.google.gson.reflect.TypeToken
import com.study.today.R
import com.study.today.feature.main.bookmark.BookmarkRepo
import com.study.today.model.Tour
import com.study.today.model.TourResponse
import com.study.today.model.remote.ServiceGenerator
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import timber.log.Timber

class SearchViewModel(application: Application) : AndroidViewModel(application) {

    private val bookmarkIdSet = BookmarkRepo.getInstance(application).idSet
    var disposable: Disposable? = null
    val searchResult = MutableLiveData<List<Tour>>()
    val isLoading = MutableLiveData(false)
    var currentResult: TourResponse? = null
    val toastMsgResId = MutableLiveData<Int>()
    private val gson = Gson()

    fun search(word: String? = null, lat: Double? = null, lng: Double? = null, range: Int = 3000) {
        isLoading.value = true
        disposable = ServiceGenerator.createTour().let { api ->
            if (word != null) api.searchWithKeyWord(word)
            else api.searchWithLatLng(lat!!, lng!!, radius = range)
        }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .map {
                convert(it).let {
                    currentResult = it.first
                    Timber.i(currentResult.toString())
                    it.second
                }
            }
            .subscribe { tours, e ->
                if (e != null) {
                    toastMsgResId.postValue(R.string.error_load_tour)
                    Timber.e("관광정보 로딩 실패!")
                    Timber.e(e)
                } else {
                    if (tours.isEmpty()) {
                        toastMsgResId.postValue(R.string.msg_no_result)
                    } else searchResult.postValue(tours)
                    Timber.i(tours.toString())
                }
                isLoading.postValue(false)
            }
    }

    fun search(lat: Double, lng: Double, range: Int = 3000) {
        search(null, lat, lng, range)
    }

    fun searchNext(word: String) {
        if (isLoading.value == true) return
        val nextPage = (currentResult?.pageNo ?: 0) + 1
        val lastPage = (currentResult?.lastPage()) ?: 1
        if (nextPage > lastPage) return
        isLoading.value = true
        disposable = ServiceGenerator.createTour().searchWithKeyWord(word, nextPage)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .map {
                convert(it).let {
                    currentResult = it.first
                    Timber.i(currentResult.toString() + " lastPage:$lastPage")
                    val temp = searchResult.value?.let { ArrayList(it) } ?: arrayListOf()
                    temp.apply { addAll(it.second) }
                }
            }
            .subscribe { tours, e ->
                if (e != null) {
                    Timber.e(e)
                } else {
                    searchResult.postValue(tours)
                }
                isLoading.postValue(false)
            }
    }

    private fun convert(jsonObject: JsonObject): Pair<TourResponse, List<Tour>> {
        val body = jsonObject.get("response").asJsonObject.get("body")
        val tourResponse = gson.fromJson(body, TourResponse::class.java)
        val tours: List<Tour>
        if (tourResponse.totalCount > 0) {
            val items = body.asJsonObject.get("items").asJsonObject.get("item")
            if (tourResponse.totalCount == 1) {
                tours = listOf(gson.fromJson(items, object : TypeToken<Tour>() {}.type))
            } else {
                tours = gson.fromJson(items, object : TypeToken<List<Tour>>() {}.type)
            }
        } else {
            tours = emptyList()
        }
        tours.forEach {
            it.bookmark = bookmarkIdSet.contains(it.id)
        }
        return Pair(tourResponse, tours)
    }


    override fun onCleared() {
        disposable?.dispose()
        super.onCleared()
    }
}