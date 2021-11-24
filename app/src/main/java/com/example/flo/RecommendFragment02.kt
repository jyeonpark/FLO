package com.example.flo

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.flo.databinding.FragmentRecommend02Binding
import com.example.flo.databinding.FragmentRecommendBinding

class RecommendFragment02 : Fragment() {

    lateinit var binding : FragmentRecommend02Binding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentRecommend02Binding.inflate(inflater, container, false)

        return binding.root
    }
}