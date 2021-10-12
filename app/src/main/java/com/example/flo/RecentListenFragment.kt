package com.example.flo

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.flo.databinding.FragmentMylistBinding
import com.example.flo.databinding.FragmentRecentlistenBinding

class RecentListenFragment : Fragment() {

    lateinit var binding : FragmentRecentlistenBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding =FragmentRecentlistenBinding.inflate(inflater, container, false)
        return binding.root
    }
}