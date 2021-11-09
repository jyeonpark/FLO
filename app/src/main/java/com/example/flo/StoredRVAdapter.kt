package com.example.flo

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.flo.databinding.ItemAlbumBinding
import com.example.flo.databinding.ItemStoredBinding

class StoredRVAdapter(private val albumList: ArrayList<Album>) : RecyclerView.Adapter<StoredRVAdapter.ViewHolder>()  {

    // 클릭 인터페이스 정의
    interface MyItemClickListener{
        fun onRemoveAlbum(position: Int)
    }

    // 리스너 객체를 전달받는 함수랑 리스너 객체를 저장할 변수
    private lateinit var mItemClickListener: StoredRVAdapter.MyItemClickListener

    fun setMyItemClickListener(itemClickListener: StoredRVAdapter.MyItemClickListener){
        mItemClickListener = itemClickListener
    }

    fun removeItem(position: Int){
        albumList.removeAt(position)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): StoredRVAdapter.ViewHolder {
        val binding: ItemStoredBinding = ItemStoredBinding.inflate(LayoutInflater.from(viewGroup.context), viewGroup, false)

        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: StoredRVAdapter.ViewHolder, position: Int) {
        holder.bind(albumList[position])

        // 아이템뷰 : 이미지 클릭시 삭제하기
        holder.binding.storedMoreIv.setOnClickListener { mItemClickListener.onRemoveAlbum(position) }
    }

    override fun getItemCount(): Int = albumList.size

    // 뷰홀더
    inner class ViewHolder(val binding: ItemStoredBinding): RecyclerView.ViewHolder(binding.root){

        fun bind(album: Album){
            binding.storedTitleTv.text = album.title
            binding.storedSingerTv.text = album.singer
            binding.storedAlbumIv.setImageResource(album.coverImg!!)
        }
    }
}