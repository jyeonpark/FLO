package com.example.flo

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.flo.databinding.FragmentStoredBinding

class StoredFragment: Fragment() {

    lateinit var binding : FragmentStoredBinding
    private var albumDatas = ArrayList<Album>();

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentStoredBinding.inflate(inflater, container, false)

        // 데이터 리스트 생성
        albumDatas.apply {
            add(Album("Butter", "방탄소년단 (BTS)",R.drawable.img_album_exp ))
            add(Album("Lilac","아이유 (IU)", R.drawable.img_album_exp2))
            add(Album("지켜줄게", "백예린 (Yerin Baek)", R.drawable.img_album_exp3))
            add(Album("운전만해 (We Ride)", "브레이브걸스", R.drawable.img_album_exp4))
            add(Album("소나기", "빈첸", R.drawable.img_album_exp5))
            add(Album("초대장", "오마이걸 (OH MY GIRL)", R.drawable.img_album_exp6))
            add(Album("Butter", "방탄소년단 (BTS)",R.drawable.img_album_exp ))
            add(Album("Lilac","아이유 (IU)", R.drawable.img_album_exp2))
            add(Album("지켜줄게", "백예린 (Yerin Baek)", R.drawable.img_album_exp3))
            add(Album("운전만해 (We Ride)", "브레이브걸스", R.drawable.img_album_exp4))
            add(Album("소나기", "빈첸", R.drawable.img_album_exp5))
            add(Album("초대장", "오마이걸 (OH MY GIRL)", R.drawable.img_album_exp6))
        }

        // 더미데이터랑 Adapter 연결
        val storedRVAdapter = StoredRVAdapter(albumDatas)
        // 리사이클러뷰에 어댑터를 연결
        binding.storedAlbumRecyclerview.adapter = storedRVAdapter

        storedRVAdapter.setMyItemClickListener(object : StoredRVAdapter.MyItemClickListener{
            override fun onRemoveAlbum(position: Int) {
                storedRVAdapter.removeItem(position)
            }

        })



        return binding.root
    }
}