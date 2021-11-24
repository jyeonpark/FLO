package com.example.flo

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.flo.databinding.ItemAlbumBinding
import com.example.flo.databinding.ItemFramgmentSongBinding

class SideSongRVAdapter(private val songList: ArrayList<Song>) : RecyclerView.Adapter<SideSongRVAdapter.ViewHolder>() {

    // 클릭 인터페이스 정의
    interface MyItemClickListener{
        fun onItemClick(song: Song)
        fun onPlayAlbum(song: Song)
    }

    // 리스너 객체를 전달받는 함수랑 리스너 객체를 저장할 변수
    private lateinit var mItemClickListener: SideSongRVAdapter.MyItemClickListener

    fun setMyItemClickListener(itemClickListener: SideSongRVAdapter.MyItemClickListener){
        mItemClickListener = itemClickListener
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): SideSongRVAdapter.ViewHolder {
        val binding: ItemFramgmentSongBinding = ItemFramgmentSongBinding.inflate(LayoutInflater.from(viewGroup.context), viewGroup, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(songList[position], position)

        // 아이템뷰가 클릭됐을 때
        holder.itemView.setOnClickListener { mItemClickListener.onItemClick(songList[position]) }

        // 아이템뷰 재생 버튼 클릭시 재생하기
        holder.binding.songListPlayIv.setOnClickListener { mItemClickListener.onPlayAlbum(songList[position])}

    }

    override fun getItemCount(): Int = songList.size

    // 뷰홀더
    inner class ViewHolder(val binding: ItemFramgmentSongBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(song: Song, position: Int){
            if (position + 1 < 10) {
                binding.songListOrderTv.text = "0" + (position+1).toString()
            }else{
                binding.songListOrderTv.text = (position+1).toString()
            }
            binding.songListTitleTv.text = song.title
            binding.songListSingerTv.text = song.singer
        }
    }
}