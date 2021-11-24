package com.example.flo

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.flo.databinding.ItemStoredBinding

class SongRVAdapter() :
    RecyclerView.Adapter<SongRVAdapter.ViewHolder>()  {
    private val songs = ArrayList<Song>()

    // 클릭 인터페이스 정의
    interface MyItemClickListener{
        fun onRemoveSong(songId: Int)
    }

    // 리스너 객체를 전달받는 함수랑 리스너 객체를 저장할 변수
    private lateinit var mItemClickListener: MyItemClickListener

    fun setMyItemClickListener(itemClickListener: MyItemClickListener){
        mItemClickListener = itemClickListener
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): SongRVAdapter.ViewHolder {
        val binding: ItemStoredBinding = ItemStoredBinding.inflate(LayoutInflater.from(viewGroup.context), viewGroup, false)

        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SongRVAdapter.ViewHolder, position: Int) {
        holder.bind(songs[position])

        // 아이템뷰 : 이미지 클릭시 삭제하기
        holder.binding.storedMoreIv.setOnClickListener {
            mItemClickListener.onRemoveSong(songs[position].id)
            removeSong(position)
        }
    }

    override fun getItemCount(): Int = songs.size

    fun removeSong(position: Int){
        songs.removeAt(position)
        notifyDataSetChanged()
    }

    @SuppressLint("NotifyDataSetChanged")
    fun addSongs(songs: ArrayList<Song>) {
        this.songs.clear()
        this.songs.addAll(songs)

        notifyDataSetChanged()
    }

    // 뷰홀더
    inner class ViewHolder(val binding: ItemStoredBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(song: Song){
            binding.storedAlbumIv.setImageResource(song.coverImg!!)
            binding.storedTitleTv.text = song.title
            binding.storedSingerTv.text = song.singer
        }
    }
}