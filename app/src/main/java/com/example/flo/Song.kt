package com.example.flo

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "SongTable")
data class Song(
    var title : String = "",
    var singer : String = "",
    var second : Int = 0,
    var playTime : Int = 0, // 총 재생시간
    var isPlaying : Boolean = false,
    var music : String = "",
    var coverImg: Int? = null,
    var isLike: Boolean = false,
    var albumIdx: Int = 0 // 이 song이 어떤 앨범에 담겨 있는지 가리키는 변수 (foreign key 역할)
){
    @PrimaryKey(autoGenerate = true) var id: Int = 0
}
