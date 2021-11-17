package com.example.flo

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat.getSystemService
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.flo.databinding.FragmentLikeBinding
import com.google.android.material.bottomsheet.BottomSheetDialog
import kotlinx.android.synthetic.main.bottomsheet_custom.view.*

class LikeFragment : Fragment() {

    lateinit var binding : FragmentLikeBinding
    lateinit var songDB: SongDatabase
    lateinit var sheetLayout: View

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLikeBinding.inflate(inflater, container, false)
        songDB = SongDatabase.getInstance(requireContext())!!

       binding.likedSelectAllTv.setOnClickListener {
           sheetLayout = LayoutInflater.from(requireContext()).inflate(R.layout.bottomsheet_custom, null, false)
           var sheetDialog = BottomSheetDialog(requireContext())
           sheetDialog.setContentView(sheetLayout)
           sheetDialog.show()
           sheetLayout.bottomsheet_unlike_cl.setOnClickListener {
               Log.d("싫어요","클릭")
               AllDeleteRecyclerview()
               sheetDialog.dismiss()
           }
       }

        return binding.root
    }

    override fun onStart() {
        super.onStart()
        initRecyclerview()
    }

    private fun initRecyclerview(){
        binding.likedAlbumRecyclerview.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)

        val songRVAdapter = SongRVAdapter()
        //리스너 객체 생성 및 전달

        songRVAdapter.setMyItemClickListener(object : SongRVAdapter.MyItemClickListener{
            override fun onRemoveSong(songId: Int) {
                songDB.songDao().updateIsLikeById(false,songId)
            }
        })

        binding.likedAlbumRecyclerview.adapter = songRVAdapter

        songRVAdapter.addSongs(songDB.songDao().getLikedSongs(true) as ArrayList)

    }

    private fun AllDeleteRecyclerview(){
        binding.likedAlbumRecyclerview.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)

        val songRVAdapter = SongRVAdapter()
        //리스너 객체 생성 및 전달

        songDB.songDao().updateIsLikeAll(false)

        binding.likedAlbumRecyclerview.adapter = songRVAdapter

        songRVAdapter.addSongs(songDB.songDao().getLikedSongs(true) as ArrayList)



    }
}