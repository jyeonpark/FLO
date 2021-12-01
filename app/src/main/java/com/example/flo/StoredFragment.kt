package com.example.flo

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.flo.databinding.FragmentStoredBinding
import kotlinx.android.synthetic.main.bottomsheet_custom.view.*

class StoredFragment: Fragment() {

    lateinit var binding: FragmentStoredBinding
    lateinit var songDB: SongDatabase

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentStoredBinding.inflate(inflater, container, false)
        songDB = SongDatabase.getInstance(requireContext())!!

        return binding.root
    }

    override fun onStart() {
        super.onStart()
        initRecyclerview()
    }

    private fun initRecyclerview() {
        binding.storedAlbumRecyclerview.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)

        val albumlockerRVAdapter = AlbumLockerRVAdapter()
        //리스너 객체 생성 및 전달

        albumlockerRVAdapter.setMyItemClickListener(object : AlbumLockerRVAdapter.MyItemClickListener {
            override fun onRemoveAlbum(albumId: Int) {
                songDB.albumDao().disLikeAlbum(getUserIdx(requireContext()), albumId)
            }
        })

        binding.storedAlbumRecyclerview.adapter = albumlockerRVAdapter

        albumlockerRVAdapter.addAlbums(songDB.albumDao().getLikedAlbums(getUserIdx(requireContext())) as ArrayList)
    }

}