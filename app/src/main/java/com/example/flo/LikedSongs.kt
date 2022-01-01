package com.example.flo

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "LikeSongTable")
data class LikedSongs(var userId: Int, var songId: Int){
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
}
