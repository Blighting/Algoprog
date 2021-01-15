package com.blighter.algoprog.data

import androidx.room.Dao
import androidx.room.Query

@Dao
interface AchievementDao {
    @Query("SELECT * FROM achievements WHERE _ID = :id")
    fun getById(id: String?): Achievement?
}