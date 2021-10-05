package com.example.flo

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.flo.databinding.ActivitySongBinding

class SongActivity : AppCompatActivity() {

    lateinit var binding : ActivitySongBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySongBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (intent.hasExtra("title") && intent.hasExtra("singer")) {
            binding.songTitleTv.text = intent.getStringExtra("title")
            binding.songSingerTv.text = intent.getStringExtra("singer")
        }

        binding.songDownIb.setOnClickListener { // 화면 내리기
            finish()
        }

        binding.songMiniplayerIv.setOnClickListener { // 일시정지 -> 재생
            setPlayerstatus(false)
        }

        binding.songPauseIv.setOnClickListener { // 재생 -> 일시정지
            setPlayerstatus(true)
        }

        binding.songRepeatOffIv.setOnClickListener { // 반복재생 켜기
            setRepeatstatus(false)
        }

        binding.songRepeatOnIv.setOnClickListener { // 반복재생 끄기
            setRepeatstatus(true)
        }

        binding.songRandomOffIv.setOnClickListener { // 랜덤재생 켜기
            setRandomstatus(false)
        }

        binding.songRandomOnIv.setOnClickListener { // 랜덤재생 끄기
            setRandomstatus(true)
        }
    }

    fun setPlayerstatus(isPlaying : Boolean){
        if(isPlaying){
            binding.songMiniplayerIv.visibility = View.VISIBLE
            binding.songPauseIv.visibility = View.GONE
        }
        else{
            binding.songMiniplayerIv.visibility = View.GONE
            binding.songPauseIv.visibility = View.VISIBLE
        }
    }

    fun setRandomstatus(isRandom : Boolean) {
        if(isRandom){
            binding.songRandomOffIv.visibility = View.VISIBLE
            binding.songRandomOnIv.visibility = View.GONE
        }
        else{
            binding.songRandomOffIv.visibility = View.GONE
            binding.songRandomOnIv.visibility = View.VISIBLE
        }
    }

    fun setRepeatstatus(isRepeating : Boolean){
        if(isRepeating){
            binding.songRepeatOffIv.visibility = View.VISIBLE
            binding.songRepeatOnIv.visibility = View.GONE
        }
        else{
            binding.songRepeatOffIv.visibility = View.GONE
            binding.songRepeatOnIv.visibility = View.VISIBLE
        }
    }
}
