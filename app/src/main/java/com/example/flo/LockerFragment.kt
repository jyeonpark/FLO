package com.example.flo

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.example.flo.databinding.FragmentLockerBinding
import com.google.android.material.tabs.TabLayoutMediator


class LockerFragment : Fragment() {

    lateinit var binding: FragmentLockerBinding

    val information = arrayListOf("내 리스트", "♡ 좋아요", "저장앨범", "많이 들은", "팔로잉", "최근 감상", "iPod 음악")


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

    override fun onStart() {
        super.onStart()

        initView()
    }

    private fun initView(){
        val userIdx = getUserIdx(requireContext())

        if (userIdx == 0){
            // 로그인이 안된 상태
            binding.lockLoginTv.text = "로그인"

            // 사용자 이름을 게스트로 설정
            binding.lockUsernameTv.text = "Guest"

            binding.lockLoginTv.setOnClickListener {
                startActivity(Intent(activity, LoginActivity::class.java))
            }
        } else{
            binding.lockLoginTv.text = "로그아웃"

            // 사용자 이름 설정
            binding.lockUsernameTv.text = getUser(userIdx).name

            binding.lockLoginTv.setOnClickListener {
                // 로그아웃을 시켜주는 함수
                logout()
                startActivity(Intent(activity, MainActivity::class.java))
            }
        }
    }

    private fun getUser(userIdx: Int): User{
        val songDB = SongDatabase.getInstance(requireContext())!!
        val user = songDB.userDao().getUserByIdx(userIdx)

        return user!!
    }


    private fun logout(){
        val spf = activity?.getSharedPreferences("auth", AppCompatActivity.MODE_PRIVATE)
        val editor = spf!!.edit()

        // jwt 없애주기
        editor.remove("userIdx")
        editor.apply()
    }
}