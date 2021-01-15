package com.blighter.algoprog.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "achievements")
class Achievement(@PrimaryKey val _ID: String,
                  val text: String?,
                  val title: String?,
                  val color: String?,
                  val score: Int?)