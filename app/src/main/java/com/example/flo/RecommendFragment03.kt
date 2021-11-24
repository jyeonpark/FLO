package com.example.flo

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.flo.databinding.FragmentRecommend03Binding

class RecommendFragment03 : Fragment() {

    lateinit var binding : FragmentRecommend03Binding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentRecommend03Binding.inflate(inflater, container, false)

        return binding.root
    }
}