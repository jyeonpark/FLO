package com.example.flo

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.preference.PreferenceManager
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.flo.databinding.ActivityMainBinding
import java.util.*


class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding
    private lateinit var player : Player
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var editor: SharedPreferences.Editor
    private var second :Int = 0
    private lateinit var song: Song


    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sharedPreferences = getSharedPreferences("songShared",0)
        editor = sharedPreferences.edit()

        song = Song(binding.mainMiniplayerTitleTv.text.toString(), binding.mainMiniplayerSingerTv.text.toString(), 5, false)

        player = Player(song.playTime, song.isPlaying)
        player.start()

        binding.mainMiniplayerBtnIv.setOnClickListener { // 재생 thread 실행
            setPlayerstatus(true)
            song.isPlaying = true

            if (player.isAlive){
                player.isPlaying = true
            }

            else{
                player = Player(song.playTime, song.isPlaying)
                player.start()
            }

        }

        binding.mainPauseBtnIv.setOnClickListener { // 일시정지
            setPlayerstatus(false)
            song.isPlaying = false
            player.isPlaying = false
            Log.d("playing", "정지")
        }

        initNavigation()

        binding.mainBnv.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.homeFragment -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.main_frm, HomeFragment())
                        .commitAllowingStateLoss()
                    return@setOnItemSelectedListener true
                }

                R.id.lookFragment -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.main_frm, LookFragment())
                        .commitAllowingStateLoss()
                    return@setOnItemSelectedListener true
                }

                R.id.searchFragment -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.main_frm, SearchFragment())
                        .commitAllowingStateLoss()
                    return@setOnItemSelectedListener true
                }

                R.id.lockerFragment -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.main_frm, LockerFragment())
                        .commitAllowingStateLoss()
                    return@setOnItemSelectedListener true
                }

            }
            false
        }

        binding.mainPlayerLayout.setOnClickListener {
            //startActivity(Intent(this, SongActivity::class.java))

            val intent = Intent(this, SongActivity::class.java)
            intent.putExtra("title", song.title)
            intent.putExtra("singer", song.singer)
            intent.putExtra("playTime", song.playTime)
            intent.putExtra("isPlaying", song.isPlaying)

            Log.d("main second", second.toString())
            editor.putInt("second", second)
            editor.apply()

            startActivity(intent)
        }
    }

    fun setPlayerstatus(isPlaying : Boolean){
        if(isPlaying){
            binding.mainMiniplayerBtnIv.visibility = View.GONE
            binding.mainPauseBtnIv.visibility = View.VISIBLE
        }
        else{
            binding.mainMiniplayerBtnIv.visibility = View.VISIBLE
            binding.mainPauseBtnIv.visibility = View.GONE
        }
    }

    private fun initNavigation() {
        supportFragmentManager.beginTransaction().replace(R.id.main_frm, HomeFragment())
            .commitAllowingStateLoss()

    }

    inner class Player(private val playTime:Int, var isPlaying: Boolean) : Thread(){

        override fun run() {
            try {
                while(true){
                    if(second>= playTime){
                        second = 0
                        song.isPlaying = false
                        runOnUiThread {
                            binding.mainPlayerSb.progress = second * 1000 / playTime
                            setPlayerstatus(song.isPlaying)
                        }
                        player.interrupt();
                        break
                    }

                    if (isPlaying){
                        sleep(1000) // 1초마다 더하기
                        second++

                        runOnUiThread {
                            binding.mainPlayerSb.progress = second * 1000 / playTime
                        }
                    }
                }
            }catch (e : InterruptedException){
                Log.d("interrupt", "쓰레드가 종료되었습니다.")
            }
        }
    }


}

