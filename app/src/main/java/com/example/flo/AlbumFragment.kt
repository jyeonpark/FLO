package com.example.flo

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.flo.databinding.FragmentAlbumBinding
import com.google.android.material.tabs.TabLayoutMediator

class AlbumFragment : Fragment() {

    lateinit var binding: FragmentAlbumBinding

    val information = arrayListOf("수룩곡", "상세정보", "영상")


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAlbumBinding.inflate(inflater, container, false)

        binding.albumBackIv.setOnClickListener {
            (context as MainActivity).supportFragmentManager.beginTransaction()
                .replace(R.id.main_frm, HomeFragment())
                .commitAllowingStateLoss()
        }

        binding.albumLikeOffIv.setOnClickListener {
            setalbumLike(false)
        }

        binding.albumLikeOnIv.setOnClickListener {
            setalbumLike(true)
        }

        val albumAdapter = AlbumViewpagAdapter(this)
        binding.albumContentVp.adapter = albumAdapter
        TabLayoutMediator(binding.albumContentTb, binding.albumContentVp){
                tab, position -> tab.text = information[position]
        }.attach()

        return binding.root
    }



    fun setalbumLike(like: Boolean){
        if(like){
            binding.albumLikeOffIv.visibility = View.VISIBLE
            binding.albumLikeOnIv.visibility = View.GONE
        }
        else{
            binding.albumLikeOffIv.visibility = View.GONE
            binding.albumLikeOnIv.visibility = View.VISIBLE
        }
    }
}
