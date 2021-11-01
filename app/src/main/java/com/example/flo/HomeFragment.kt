package com.example.flo

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.ViewPager
import androidx.viewpager2.widget.ViewPager2
import com.example.flo.databinding.FragmentHomeBinding
import java.util.*


class HomeFragment : Fragment() {
    lateinit var binding: FragmentHomeBinding
    var currentPage = 0
    lateinit var thread : Thread

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

        binding.homeTodayReleaseAlbum01Iv.setOnClickListener {
            (context as MainActivity).supportFragmentManager.beginTransaction()
                .replace(R.id.main_frm, AlbumFragment())
                .commitAllowingStateLoss()
        }

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