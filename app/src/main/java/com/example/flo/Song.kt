package com.example.flo

data class Song(
    var title : String = "",
    var singer : String = "",
    var second : Int = 0,
    var playTime : Int = 0, // 총 재생시간
    var isPlaying : Boolean = false,
    var music : String = ""
)
