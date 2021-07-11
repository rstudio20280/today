package com.study.today.feature.main.bookmark

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.google.gson.reflect.TypeToken
import com.study.today.R
import com.study.today.model.Tour
import com.study.today.model.TourResponse
import com.study.today.model.remote.ServiceGenerator
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.*
import timber.log.Timber

class BookmarkViewModel(application: Application) : AndroidViewModel(application) {

    var disposable: Disposable? = null
    val bookmarkList = MutableLiveData<List<Tour>>()
    val isLoading = MutableLiveData(false)
    val toastMsgResId = MutableLiveData<Int>()

    fun loadBookMarks(){
        GlobalScope.launch(Dispatchers.Default) {
            isLoading.postValue(true)
            delay(300)
            bookmarkList.postValue(Tour.getDummy())
            isLoading.postValue(false)
        }
    }

    fun change(tour: Tour, isBookmark: Boolean){
        Timber.i("${tour.title} -> $isBookmark")
    }




    override fun onCleared() {
        disposable?.dispose()
        super.onCleared()
    }
}