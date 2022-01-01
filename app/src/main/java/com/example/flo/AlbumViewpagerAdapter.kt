package com.example.flo

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter

class AlbumViewpagerAdapter(fragment : Fragment, songs: ArrayList<Song>, var mContext: MainActivity) : FragmentStateAdapter(fragment) {
    private val songs: ArrayList<Song> = songs

    override fun getItemCount(): Int = 3

    override fun createFragment(position: Int): Fragment {
        return when(position){
            0 -> SongFragment(songs,mContext)
            1 -> DetailFragment()
            else -> VideoFragment()
        }
    }
}