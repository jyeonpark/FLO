package com.example.flo

data class Song(
    var title : String = "",
    var singer : String = "",
    var playTime : Int = 0, // 총 재생시간
    var isPlaying : Boolean = false
)
