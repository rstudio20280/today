package com.study.today.feature.main.bookmark

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asLiveData
import com.study.today.model.Tour
import io.reactivex.disposables.Disposable
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import timber.log.Timber

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
        GlobalScope.launch(Dispatchers.IO) {
            Timber.i("${tour.title} -> $isBookmark")
            if (isBookmark) repo.addBookmark(tour)
            else repo.removeBookmark(tour)
        }
    }


    override fun onCleared() {
        disposable?.dispose()
        super.onCleared()
    }
}