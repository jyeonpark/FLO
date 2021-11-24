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
import androidx.viewpager2.widget.ViewPager2
import com.example.flo.databinding.FragmentHomeBinding
import com.google.gson.Gson
import kotlin.collections.ArrayList


class HomeFragment(var mContext: MainActivity) : Fragment() {
    lateinit var binding: FragmentHomeBinding
    var currentPage = 0
    lateinit var thread : Thread

    private var albums = ArrayList<Album>()
    private lateinit var songDB: SongDatabase
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

        //ROOM_DB
        songDB = SongDatabase.getInstance(requireContext())!!
        albums.addAll(songDB.albumDao().getAlbums()) // songDB에서 album list를 가져옵니다.

        //레이아웃 매니저 설정
        binding.homeTodayMusicAlbumRecyclerview.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)

        //더미데이터와 Adapter 연결
        val albumRecyclerViewAdapter = AlbumRVAdapter(albums)

        //리스너 객체 생성 및 전달
        albumRecyclerViewAdapter.setMyItemClickListener(object : AlbumRVAdapter.MyItemClickListener{
            override fun onItemClick(album: Album) {
                startAlbumFragment(album)
            }

            override fun onPlayAlbum(album: Album) {
                mAlbumClickListener.onAlbumClick(album)
            }
        })

        binding.homeTodayMusicAlbumRecyclerview.adapter = albumRecyclerViewAdapter

        val recommendAdapter = RecommendViewpagerAdapter(this)
        recommendAdapter.addFragment(RecommendFragment())
        recommendAdapter.addFragment(RecommendFragment02())
        recommendAdapter.addFragment(RecommendFragment03())
        recommendAdapter.addFragment(RecommendFragment())
        recommendAdapter.addFragment(RecommendFragment02())
        recommendAdapter.addFragment(RecommendFragment03())

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

    fun startAlbumFragment(album: Album) {
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
        if(currentPage == 6)
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