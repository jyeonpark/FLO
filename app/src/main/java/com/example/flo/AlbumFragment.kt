package com.example.flo

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.example.flo.databinding.FragmentAlbumBinding
import com.google.android.material.tabs.TabLayoutMediator
import com.google.gson.Gson

class AlbumFragment : Fragment() {

    lateinit var binding: FragmentAlbumBinding
    private var gson: Gson = Gson()

    val information = arrayListOf("수룩곡", "상세정보", "영상")

    private var isLiked: Boolean = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAlbumBinding.inflate(inflater, container, false)

        // Home 에서 넘어온 데이터 받아오기
        val albumData = arguments?.getString("album")
        val album = gson.fromJson(albumData, Album::class.java)

        isLiked = isLikedAlbum(album.id)

        // Home 에서 넘어온 데이터를 반영
        setInit(album)
        setClickListeners(album)

        // RoomDB
        val songs = getSongs(album.id) // 앨범안에 있는 수록곡들 불러오기

        binding.albumBackIv.setOnClickListener {
            (context as MainActivity).supportFragmentManager.beginTransaction()
                .replace(R.id.main_frm, HomeFragment(MainActivity()))
                .commitAllowingStateLoss()
        }


        val albumAdapter = AlbumViewpagerAdapter(this, songs)
        binding.albumContentVp.adapter = albumAdapter

        val child = binding.albumContentVp.getChildAt(0)
        (child as? RecyclerView)?.overScrollMode = View.OVER_SCROLL_NEVER

        TabLayoutMediator(binding.albumContentTb, binding.albumContentVp){
                tab, position -> tab.text = information[position]
        }.attach()

        return binding.root
    }


    private fun setInit(album: Album) {
        binding.albumAlbumIv.setImageResource(album.coverImg!!)
        binding.albumMusicTitleTv.text = album.title.toString()
        binding.albumMusicSingerTv.text = album.singer.toString()

        if(isLiked){
            binding.albumLikeOnIv.setImageResource(R.drawable.ic_my_like_on)
        }else{
            binding.albumLikeOnIv.setImageResource(R.drawable.ic_my_like_off)
        }
    }

    private fun setClickListeners(album: Album){
        val userId: Int = getJwt()

        binding.albumLikeOnIv.setOnClickListener {
            if(isLiked){
                binding.albumLikeOnIv.setImageResource(R.drawable.ic_my_like_off)
                disLikedAlbum(userId, album.id)
            }else{
                binding.albumLikeOnIv.setImageResource(R.drawable.ic_my_like_on)
                likeAlbum(userId, album.id)
            }
        }
    }

    private fun likeAlbum(userId: Int, albumId: Int) {
        val songDB = SongDatabase.getInstance(requireContext())!!
        val like = Like(userId, albumId)

        songDB.albumDao().likeAlbum(like)
    }

    private fun isLikedAlbum(albumId: Int): Boolean {
        val songDB = SongDatabase.getInstance(requireContext())!!
        val userId = getJwt()

        val likeId: Int? = songDB.albumDao().isLikeAlbum(userId, albumId)

        // likeId 가 null 이 아니면 true, 맞다면 false 반환
        return likeId != null
    }

    private fun disLikedAlbum(userId: Int, albumId: Int) {
        val songDB = SongDatabase.getInstance(requireContext())!!
        songDB.albumDao().disLikeAlbum(userId, albumId)
    }

    private fun getJwt(): Int{
        // fragment 에서 sharedpreference 사용하는 법
        val spf = activity?.getSharedPreferences("auth", AppCompatActivity.MODE_PRIVATE)

        return spf!!.getInt("jwt", 0)
    }

    private fun getSongs(albumIdx: Int): ArrayList<Song>{
        val songDB = SongDatabase.getInstance(requireContext())!!
        val songs = songDB.songDao().getSongsInAlbum(albumIdx) as ArrayList

        return songs
    }
}
