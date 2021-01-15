package com.blighter.algoprog.data

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [Achievement::class], version = 1, exportSchema = false)
abstract class Database : RoomDatabase() {
    abstract fun getDao(): AchievementDao
}