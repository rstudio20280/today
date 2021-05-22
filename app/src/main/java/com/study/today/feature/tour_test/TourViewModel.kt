package com.study.today.feature.tour_test

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.google.gson.reflect.TypeToken
import com.study.today.model.Tour
import com.study.today.model.TourResponse
import com.study.today.model.remote.ServiceGenerator
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import timber.log.Timber

class TourViewModel : ViewModel() {

    var disposable: Disposable? = null
    val searchResult = MutableLiveData<List<Tour>>()
    val isLoading = MutableLiveData(false)
    var currentResult: TourResponse? = null
    private val gson = Gson()

    fun search(word: String? = null, lat: Double? = null, lng: Double? = null) {
        isLoading.value = true
        disposable = ServiceGenerator.createTour().let { api ->
            if (word != null) api.searchWithKeyWord(word)
            else api.searchWithLatLng(lat!!, lng!!)
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
                    Timber.e(e)
                } else {
                    searchResult.postValue(tours)
                    Timber.i(tours.toString())
                }
                isLoading.postValue(false)
            }
    }

    fun search(lat: Double, lng: Double) {
        search(null, lat, lng)
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
        val items = body.asJsonObject.get("items").asJsonObject.get("item")
        val tourResponse = gson.fromJson(body, TourResponse::class.java)
        val tours = gson.fromJson<List<Tour>>(items, object : TypeToken<List<Tour>>() {}.type)
        return Pair(tourResponse, tours)
    }


    override fun onCleared() {
        disposable?.dispose()
        super.onCleared()
    }
}