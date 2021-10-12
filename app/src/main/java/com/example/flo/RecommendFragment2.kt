package com.example.flo

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.flo.databinding.FragmentRecommend2Binding
import com.example.flo.databinding.FragmentRecommendBinding

class RecommendFragment2 : Fragment() {

    lateinit var binding : FragmentRecommend2Binding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentRecommend2Binding.inflate(inflater, container, false)

        return binding.root
    }
}