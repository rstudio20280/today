package com.study.today.feature.main.bookmark

import android.app.Application
import com.study.today.model.Tour
import com.study.today.model.local.AppDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

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

    val allBookMarks : Flow<List<Tour>> = bookmarkDao.getAllWithFlow().also {
        GlobalScope.launch {
            it.collect {
                idSet.clear()
                idSet.addAll(it.map { it.id })
            }
        }
    }
    val idSet = hashSetOf<Int>()

    fun getBookmarks():List<Tour>{
        return bookmarkDao.getAll().also {
            idSet.clear()
            idSet.addAll(it.map { it.id })
        }
    }

    fun change(tour: Tour, isMarked:Boolean){
        GlobalScope.launch(Dispatchers.IO) {
            if (isMarked) addBookmark(tour)
            else removeBookmark(tour)
        }
    }

    fun addBookmark(tour: Tour){
        idSet.add(tour.id)
        tour.bookmark = true
        bookmarkDao.insert(tour)
    }

    fun removeBookmark(tour: Tour){
        idSet.remove(tour.id)
        tour.bookmark = false
        bookmarkDao.delete(tour)
    }

}