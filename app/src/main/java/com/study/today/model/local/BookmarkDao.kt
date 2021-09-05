package com.study.today.model.local

import androidx.room.*
import com.study.today.model.Tour
import kotlinx.coroutines.flow.Flow

@Dao
interface BookmarkDao {
    @Query("SELECT * FROM bookmarks")
    fun getAllWithFlow() : Flow<List<Tour>>

    @Query("SELECT * FROM bookmarks")
    fun getAll() : List<Tour>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(tour: Tour)

    @Delete
    fun delete(tour: Tour)

    @Query("DELETE FROM bookmarks WHERE id = :id")
    fun delete(id: Int)
}