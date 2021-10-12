package com.example.flo

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.flo.databinding.FragmentDetailBinding
import com.example.flo.databinding.FragmentSongBinding

class SongFragment: Fragment() {

    lateinit var binding : FragmentSongBinding

    var selectAll : Boolean = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentSongBinding.inflate(inflater, container, false)

        binding.song01Layout.setOnClickListener {
            Toast.makeText(activity, "라일락", Toast.LENGTH_SHORT).show()
        }

        binding.song02Layout.setOnClickListener {
            Toast.makeText(activity, "Flu", Toast.LENGTH_SHORT).show()
        }

        binding.song03Layout.setOnClickListener {
            Toast.makeText(activity, "Coin", Toast.LENGTH_SHORT).show()
        }

        binding.songMixOffIv.setOnClickListener { // mix 켜기
            setMixstatus(false)
        }

        binding.songMixOnIv.setOnClickListener { // mix 끄기
            setMixstatus(true)
        }

        binding.songSelectAllTv.setOnClickListener {
            selectAll(selectAll)
        }


        return binding.root
    }

    fun setMixstatus(isMix: Boolean) {
        if (isMix) {
            binding.songMixOffIv.visibility = View.VISIBLE
            binding.songMixOnIv.visibility = View.GONE
            Toast.makeText(activity, "일반 곡 순서로 변경했습니다.", Toast.LENGTH_SHORT).show()
        } else {
            binding.songMixOffIv.visibility = View.GONE
            binding.songMixOnIv.visibility = View.VISIBLE
            Toast.makeText(activity, "내 취향 순서로 변경했습니다.", Toast.LENGTH_SHORT).show()
        }
    }

    fun selectAll(select : Boolean){
        if(select){
            binding.songPlaylistSelectOffIv.visibility = View.VISIBLE
            binding.songPlaylistSelectOnIv.visibility = View.GONE
            binding.songSelectAllTv.text = "전체선택"
            binding.songSelectAllTv.setTextColor(Color.parseColor("#000000"))
            binding.songMusicListLayout.setBackgroundColor(Color.parseColor("#FFFFFF"))
            selectAll = false
        }
        else{
            binding.songPlaylistSelectOffIv.visibility = View.GONE
            binding.songPlaylistSelectOnIv.visibility = View.VISIBLE
            binding.songSelectAllTv.text = "선택해제"
            binding.songSelectAllTv.setTextColor(Color.parseColor("#3f3fff"))
            binding.songMusicListLayout.setBackgroundColor(Color.parseColor("#EAEAEA"))
            selectAll = true
        }
    }
}