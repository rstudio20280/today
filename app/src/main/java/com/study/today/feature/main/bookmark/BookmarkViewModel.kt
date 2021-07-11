package com.study.today.feature.main.bookmark

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asLiveData
import com.study.today.model.Tour
import io.reactivex.disposables.Disposable

class BookmarkViewModel(application: Application) : AndroidViewModel(application) {

    private val repo = BookmarkRepo.getInstance(application)
    var disposable: Disposable? = null

    val bookmarkList = repo.allBookMarks.asLiveData()
    //    val bookmarkList = MutableLiveData<List<Tour>>()
    val isLoading = MutableLiveData(false)
    val toastMsgResId = MutableLiveData<Int>()

//    fun loadBookMarks() {
//        GlobalScope.launch(Dispatchers.IO) {
//            isLoading.postValue(true)
//            bookmarkList.postValue(repo.getBookmarks())
//            isLoading.postValue(false)
//        }
//    }

    fun change(tour: Tour, isBookmark: Boolean) {
        repo.change(tour, isBookmark)
    }


    override fun onCleared() {
        disposable?.dispose()
        super.onCleared()
    }
}