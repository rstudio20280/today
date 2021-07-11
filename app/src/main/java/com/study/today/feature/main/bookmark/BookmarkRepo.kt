package com.study.today.feature.main.bookmark

import android.app.Application
import com.study.today.model.Tour
import com.study.today.model.local.AppDatabase
import kotlinx.coroutines.flow.Flow

class BookmarkRepo private constructor(application: Application) {
    companion object {
        @Volatile private var instance: BookmarkRepo? = null
        fun getInstance(application: Application): BookmarkRepo =
            instance ?: synchronized(this) {
                instance ?: BookmarkRepo(application).also {
                    instance = it
                }
            }
    }

    private val db : AppDatabase = AppDatabase.getDatabase(application)
    private val bookmarkDao = db.bookmarkDao()

    val allBookMarks : Flow<List<Tour>> = bookmarkDao.getAllWithFlow()

    fun getBookmarks():List<Tour>{
        return bookmarkDao.getAll()
    }

    fun addBookmark(tour: Tour){
        tour.bookmark = true
        bookmarkDao.insert(tour)
    }

    fun removeBookmark(tour: Tour){
        tour.bookmark = false
        bookmarkDao.delete(tour)
    }

}