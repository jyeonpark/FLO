package com.example.flo

import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.flo.databinding.ActivitySongBinding
import android.R

import android.view.ViewGroup




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

        binding.songRepeatOffIv.setOnClickListener { // 전체반복재생 켜기
            setRepeatstatus(0)
        }

        binding.songRepeatOnIv.setOnClickListener { // 현재반복재생 켜기
            setRepeatstatus(1)
        }

        binding.songRepeatOn1Iv.setOnClickListener { // 반복재생 끄기
            setRepeatstatus(2)
        }

        binding.songRandomOffIv.setOnClickListener { // 랜덤재생 켜기
            setRandomstatus(false)
        }

        binding.songRandomOnIv.setOnClickListener { // 랜덤재생 끄기
            setRandomstatus(true)
        }

        binding.songMyLikeOff.setOnClickListener { // 좋아요 누르기
            setLikestatus(false)
        }

        binding.songMyLikeOn.setOnClickListener { // 좋아요 끄기
            setLikestatus(true)
        }

        binding.songUnlikeOff.setOnClickListener { // 싫어요 누르기
            setUnlikestatus(false)
        }

        binding.songUnlikeOn.setOnClickListener { // 싫어요 끄기
            setUnlikestatus(true)
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
            Toast.makeText(this, "재생목록을 랜덤으로 재생합니다.", Toast.LENGTH_SHORT).show()
        }
        else{
            binding.songRandomOffIv.visibility = View.GONE
            binding.songRandomOnIv.visibility = View.VISIBLE
            Toast.makeText(this, "재생목록을 순서대로 재생합니다.", Toast.LENGTH_SHORT).show()
        }
    }

    fun setRepeatstatus(Repeating : Int){
        if(Repeating == 0){
            binding.songRepeatOffIv.visibility = View.GONE
            binding.songRepeatOnIv.visibility = View.VISIBLE
            Toast.makeText(this, "전체 음악을 반복합니다.", Toast.LENGTH_SHORT).show()
        }
        else if (Repeating == 1){
            binding.songRepeatOnIv.visibility = View.GONE
            binding.songRepeatOn1Iv.visibility = View.VISIBLE
            Toast.makeText(this, "현재 음악을 반복합니다.", Toast.LENGTH_SHORT).show()
        }
        else{
            binding.songRepeatOn1Iv.visibility = View.GONE
            binding.songRepeatOffIv.visibility = View.VISIBLE
            Toast.makeText(this, "반복을 사용하지 않습니다.", Toast.LENGTH_SHORT).show()
        }
    }

    fun setLikestatus(like : Boolean){
        if(like){
            binding.songMyLikeOff.visibility = View.VISIBLE
            binding.songMyLikeOn.visibility = View.GONE
            Toast.makeText(this, "좋아요 한 곡에 담겼습니다.", Toast.LENGTH_SHORT).show()
        }
        else{
            binding.songMyLikeOff.visibility = View.GONE
            binding.songMyLikeOn.visibility = View.VISIBLE
            Toast.makeText(this, "좋아요 한 곡이 취소되었습니다.", Toast.LENGTH_SHORT).show()
        }
    }

    fun setUnlikestatus(like : Boolean){
        if(like){
            binding.songUnlikeOff.visibility = View.VISIBLE
            binding.songUnlikeOn.visibility = View.GONE
        }
        else{
            binding.songUnlikeOff.visibility = View.GONE
            binding.songUnlikeOn.visibility = View.VISIBLE
        }
    }
}
