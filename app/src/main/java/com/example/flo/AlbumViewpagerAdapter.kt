package com.example.flo

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter

class AlbumViewpagerAdapter(fragment : Fragment, songs: ArrayList<Song>) : FragmentStateAdapter(fragment) {
    private val songs: ArrayList<Song> = songs

    override fun getItemCount(): Int = 3

    override fun createFragment(position: Int): Fragment {
        return when(position){
            0 -> SongFragment(songs)
            1 -> DetailFragment()
            else -> VideoFragment()
        }
    }
}