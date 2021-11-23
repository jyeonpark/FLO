package com.example.flo

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.example.flo.databinding.FragmentLockerBinding
import com.google.android.material.tabs.TabLayoutMediator


class LockerFragment : Fragment() {

    lateinit var binding: FragmentLockerBinding

    val information = arrayListOf("내 리스트", "♡ 좋아요", "저장한 곡", "많이 들은", "팔로잉", "최근 감상", "iPod 음악")


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentLockerBinding.inflate(inflater, container, false)

        val lockerAdapter =LockerViewpagAdapter(this)

        binding.lockContentVp.adapter = lockerAdapter

        var child = binding.lockContentVp.getChildAt(0)
        (child as? RecyclerView)?.overScrollMode = View.OVER_SCROLL_NEVER

        TabLayoutMediator(binding.lockContentTb, binding.lockContentVp){
                tab, position -> tab.text = information[position]
        }.attach()

        binding.lockLoginTv.setOnClickListener {
            startActivity(Intent(activity, LoginActivity::class.java))
        }

        return binding.root
    }


}