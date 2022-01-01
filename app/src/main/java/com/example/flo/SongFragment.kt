package com.example.flo

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.flo.databinding.FragmentSongBinding

class SongFragment(songs: ArrayList<Song>, var mContext: MainActivity) : Fragment() {

    lateinit var binding : FragmentSongBinding
    private var songs: ArrayList<Song> = songs
    private lateinit var mSongClickListener: OnAlbumSongClickListner
    var selectAll : Boolean = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentSongBinding.inflate(inflater, container, false)
        mSongClickListener = mContext

        return binding.root
    }

    override fun onStart() {
        super.onStart()
        Log.d("songs : ", songs.toString())
        initRecyclerview()
    }

    private fun initRecyclerview(){
        binding.songAlbumRecyclerview.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)

        val sidesongRVAdapter = SideSongRVAdapter(songs)
        //리스너 객체 생성 및 전달

        sidesongRVAdapter.setMyItemClickListener(object : SideSongRVAdapter.MyItemClickListener {
            override fun onPlaySong(song: Song) {
                mSongClickListener.onSongClick(song)
            }
        })

        binding.songAlbumRecyclerview.adapter = sidesongRVAdapter

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
}