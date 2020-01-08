package com.blighter.algoprog.data;


import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "achievements")
public class Achievement {
    @NonNull
    @PrimaryKey
    public String _ID;
    public String text;
    public String title;
    public String color;
    public Integer score;

    public String get_ID() {
        return _ID;
    }

    public String getText() {
        return text;
    }

    public String getTitle() {
        return title;
    }

    public String getColor() {
        return color;
    }

    public Integer getScore() {
        return score;
    }
}
