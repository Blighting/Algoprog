package com.blighter.algoprog.data;

import androidx.room.RoomDatabase;

@androidx.room.Database(entities = {Achievement.class}, version = 1, exportSchema = false)
public abstract class Database extends RoomDatabase {
    public abstract AchievementDao getDao();
}
