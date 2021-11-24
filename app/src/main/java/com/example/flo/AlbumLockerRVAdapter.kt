package com.example.flo

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.flo.databinding.ItemLockerAlbumBinding
import com.example.flo.databinding.ItemStoredBinding

class AlbumLockerRVAdapter() :
    RecyclerView.Adapter<AlbumLockerRVAdapter.ViewHolder>() {
    private val albums = ArrayList<Album>()

    // 클릭 인터페이스 정의
    interface MyItemClickListener {
        fun onRemoveAlbum(albumId: Int)
    }

    // 리스너 객체를 전달받는 함수랑 리스너 객체를 저장할 변수
    private lateinit var mItemClickListener: MyItemClickListener

    fun setMyItemClickListener(itemClickListener: MyItemClickListener) {
        mItemClickListener = itemClickListener
    }

    override fun onCreateViewHolder(
        viewGroup: ViewGroup,
        viewType: Int
    ): AlbumLockerRVAdapter.ViewHolder {
        val binding: ItemLockerAlbumBinding =
            ItemLockerAlbumBinding.inflate(LayoutInflater.from(viewGroup.context), viewGroup, false)

        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: AlbumLockerRVAdapter.ViewHolder, position: Int) {
        holder.bind(albums[position])

        // 아이템뷰 : 이미지 클릭시 삭제하기
        holder.binding.lockeralbumMoreIv.setOnClickListener {
            mItemClickListener.onRemoveAlbum(albums[position].id)
            removeAlbum(position)
        }
    }

    override fun getItemCount(): Int = albums.size

    private fun removeAlbum(position: Int) {
        albums.removeAt(position)
        notifyDataSetChanged()
    }

    @SuppressLint("NotifyDataSetChanged")
    fun addAlbums(albums: ArrayList<Album>) {
        this.albums.clear()
        this.albums.addAll(albums)

        notifyDataSetChanged()
    }

    // 뷰홀더
    inner class ViewHolder(val binding: ItemLockerAlbumBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(album: Album) {
            binding.lockeralbumAlbumIv.setImageResource(album.coverImg!!)
            binding.lockeralbumTitleTv.text = album.title
            binding.lockeralbumSingerTv.text = album.singer
        }
    }
}