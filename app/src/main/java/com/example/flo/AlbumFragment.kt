package com.example.flo

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.flo.databinding.FragmentAlbumBinding
import com.example.flo.databinding.FragmentHomeBinding

class AlbumFragment : Fragment() {

    lateinit var binding: FragmentAlbumBinding

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

        binding.song01Layout.setOnClickListener {
            Toast.makeText(activity, "라일락", Toast.LENGTH_SHORT).show()
        }

        binding.song02Layout.setOnClickListener {
            Toast.makeText(activity, "Flu", Toast.LENGTH_SHORT).show()
        }

        binding.song03Layout.setOnClickListener {
            Toast.makeText(activity, "Coin", Toast.LENGTH_SHORT).show()
        }

        binding.songMixOffIv.setOnClickListener { // mix 켜기
            setMixstatus(false)
        }

        binding.songMixOnIv.setOnClickListener { // mix 끄기
            setMixstatus(true)
        }

        return binding.root
    }

    fun setMixstatus(isMix: Boolean) {
        if (isMix) {
            binding.songMixOffIv.visibility = View.VISIBLE
            binding.songMixOnIv.visibility = View.GONE
        } else {
            binding.songMixOffIv.visibility = View.GONE
            binding.songMixOnIv.visibility = View.VISIBLE
        }
    }
}
