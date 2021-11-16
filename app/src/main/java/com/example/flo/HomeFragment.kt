package com.example.flo

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.ViewPager
import androidx.viewpager2.widget.ViewPager2
import com.example.flo.databinding.FragmentHomeBinding
import com.google.gson.Gson
import java.util.*
import kotlin.collections.ArrayList


class HomeFragment(var mContext: MainActivity) : Fragment() {
    lateinit var binding: FragmentHomeBinding
    var currentPage = 0
    lateinit var thread : Thread
    private var albumDatas = ArrayList<Album>();
    private var songList = ArrayList<Song>();
    private lateinit var mAlbumClickListener: OnAlbumClickListener

    val handler=Handler(Looper.getMainLooper()){
        setPage()
        true
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        mAlbumClickListener = mContext

//        binding.homeTodayReleaseAlbum01Iv.setOnClickListener {
//            (context as MainActivity).supportFragmentManager.beginTransaction()
//                .replace(R.id.main_frm, AlbumFragment())
//                .commitAllowingStateLoss()
//        }

        songList.apply {
            add(Song("라일락", "아이유 (IU)", 0, 244, true, "music_lilac"))
            add(Song("Flu", "아이유 (IU)", 0, 188, true, "music_flu"))
            add(Song("Coin", "아이유 (IU)", 0, 223, true, "music_coin"))
            add(Song("봄 안녕 봄", "아이유 (IU)", 0, 325, true, "music_hispringbye"))
            add(Song("Celebrity", "아이유 (IU)", 0, 195, true, "music_celebrity"))
            add(Song("돌림노래 (Feat. DEAN)", "아이유 (IU)", 0, 189, true, "music_troll"))
        }

        // 데이터 리스트 생성
        albumDatas.apply {
            add(Album(1,"Butter", "방탄소년단 (BTS)",R.drawable.img_album_exp ))
            add(Album(2,"Lilac","아이유 (IU)", R.drawable.img_album_exp2))
            add(Album(3,"지켜줄게", "백예린 (Yerin Baek)", R.drawable.img_album_exp3))
            add(Album(4,"운전만해 (We Ride)", "브레이브걸스", R.drawable.img_album_exp4))
            add(Album(5,"소나기", "빈첸", R.drawable.img_album_exp5))
            add(Album(6,"초대장", "오마이걸 (OH MY GIRL)", R.drawable.img_album_exp6))
        }

        // 더미데이터랑 Adapter 연결
        val albumRVAdapter = AlbumRVAdapter(albumDatas)
        // 리사이클러뷰에 어댑터를 연결
        binding.homeTodayMusicAlbumRecyclerview.adapter = albumRVAdapter

        albumRVAdapter.setMyItemClickListener(object : AlbumRVAdapter.MyItemClickListener{

            override fun onItemClick(album: Album) {
                changeAlbumFragment(album)
            }

            // MainActivity 의 onAlbumClick 함수를 콜백으로 실행하기
            override fun onPlayAlbum(album: Album) {
                mAlbumClickListener.onAlbumClick(album)
            }

        })

        // 레이아웃 매니저 설정
        binding.homeTodayMusicAlbumRecyclerview.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)


        val recommendAdapter = RecommendViewpagerAdapter(this)
        recommendAdapter.addFragment(RecommendFragment())
        recommendAdapter.addFragment(RecommendFragment2())
        recommendAdapter.addFragment(RecommendFragment())
        recommendAdapter.addFragment(RecommendFragment2())
        recommendAdapter.addFragment(RecommendFragment())

        val viewpager: ViewPager2 = binding.homeRecommendVp
        binding.homeRecommendVp.adapter = recommendAdapter
        binding.homeRecommendVp.orientation = ViewPager2.ORIENTATION_HORIZONTAL
        val child = binding.homeRecommendVp.getChildAt(0)
        (child as? RecyclerView)?.overScrollMode = View.OVER_SCROLL_NEVER

        val bannerAdapter = BannerViewpagerAdapter(this)
        bannerAdapter.addFragment(BannerFragment(R.drawable.img_home_viewpager_exp))
        bannerAdapter.addFragment(BannerFragment(R.drawable.img_home_viewpager_exp2))

        binding.homeBannerVp.adapter = bannerAdapter
        binding.homeBannerVp.orientation = ViewPager2.ORIENTATION_HORIZONTAL
        binding.homeCircleindicatorCi.setViewPager(binding.homeRecommendVp)

        thread = Thread(PagerRunnable())
        thread.start()

        return binding.root
    }

    private fun changeAlbumFragment(album: Album) {
        (context as MainActivity).supportFragmentManager.beginTransaction()
            .replace(R.id.main_frm, AlbumFragment().apply {
                arguments = Bundle().apply {
                    val gson = Gson()
                    val albumJson = gson.toJson(album)
                    putString("album", albumJson)
                }
            })
            .commitAllowingStateLoss()
    }

    //페이지 변경하기
    fun setPage(){
        if(currentPage == 5)
            currentPage = 0
        binding.homeRecommendVp.setCurrentItem(currentPage, true)
        currentPage+=1
    }

    //2초 마다 페이지 넘기기
    inner class PagerRunnable:Runnable{
        override fun run() {
            while(true){
                try {
                    Thread.sleep(2000)
                    handler.sendEmptyMessage(0)
                } catch (e : InterruptedException){
                    Log.d("interupt", "interupt발생")
                }
            }
        }
    }

    override fun onStop() {
        thread.interrupt()
        super.onStop()
    }
}