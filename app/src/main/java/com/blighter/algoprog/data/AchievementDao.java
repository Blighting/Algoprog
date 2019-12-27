package com.blighter.algoprog.data;

import androidx.room.Dao;
import androidx.room.Query;

import io.reactivex.Flowable;
import io.reactivex.Single;

@Dao
public interface AchievementDao {
    @Query("SELECT * FROM achievements WHERE _ID = :id")
    Achievement getById(String id);
}
