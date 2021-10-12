package com.example.flo

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter

class LockerViewpagAdapter(fragment : Fragment) : FragmentStateAdapter(fragment){


    override fun getItemCount(): Int = 7

    override fun createFragment(position: Int): Fragment {
        return when(position){
            0 -> MyListFragment()
            1 -> LikeFragment()
            2 -> StoredFragment()
            3 -> ListenMuchFragment()
            4 -> FollowingFragment()
            5 -> RecentListenFragment()
            else -> IpodMusicFragment()
        }
    }

}