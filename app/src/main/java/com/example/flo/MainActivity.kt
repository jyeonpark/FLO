package com.example.flo

import android.content.Intent
import android.media.MediaPlayer
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.View.VISIBLE
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.example.flo.databinding.ActivityMainBinding
import com.google.gson.Gson
import java.util.*


class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding
    private lateinit var player : Player
    var second: Int = 0
    private lateinit var song: Song
    // 미디어 플레이어
    private var mediaPlayer : MediaPlayer? = null
    private var gson: Gson = Gson()

    var startCounter : Int = 0 // 0 이면 oncreate->onstart , 0보다 크면 바로 onstart 로 온 것
    var pauseCounter : Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        Log.d("oncreate","hi")
        startCounter = 0
//        song = Song("Antifreeze", "백예린 (Yerin Baek)", 0, 254, false, "music_antifreeze")
//        setMiniPlayer(song)
//        player = Player(song.playTime, song.isPlaying)
//        player.start()


        binding.mainMiniplayerBtnIv.setOnClickListener { // 재생 thread 실행
            setPlayerstatus(true)

            if (player.isAlive){
                player.isPlaying = true
            }
            else{
                player = Player(song.playTime, song.isPlaying)
                player.start()
            }
            mediaPlayer?.start()

        }

        binding.mainPauseBtnIv.setOnClickListener { // 일시정지
            pauseCounter += 1
            setPlayerstatus(false)
            mediaPlayer?.pause()
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
            intent.putExtra("second",song.second)
            intent.putExtra("playTime", song.playTime)
            intent.putExtra("isPlaying", song.isPlaying)
            intent.putExtra("music",song.music)

            startActivity(intent)
        }
    }

    private fun setMiniPlayer(song: Song) {
        binding.mainMiniplayerTitleTv.text = song.title
        binding.mainMiniplayerSingerTv.text = song.singer
        setPlayerstatus(song.isPlaying)
        binding.mainPlayerSb.progress = (song.second*1000/song.playTime)

        val music = resources.getIdentifier(song.music, "raw", this.packageName)
        mediaPlayer = MediaPlayer.create(this,music)
        second = song.second
        mediaPlayer?.seekTo(second * 1000)

    }

    fun setPlayerstatus(isPlaying : Boolean){
        if(isPlaying){
            binding.mainMiniplayerBtnIv.visibility = View.GONE
            binding.mainPauseBtnIv.visibility = View.VISIBLE
            player.isPlaying = true
            song.isPlaying = true
//            if (startCounter > 1) {
//                mediaPlayer?.setOnPreparedListener {
//                    mediaPlayer?.start()
//                }
//            }
//            else {
//                mediaPlayer?.start()
//            }

        }
        else{
            binding.mainMiniplayerBtnIv.visibility = View.VISIBLE
            binding.mainPauseBtnIv.visibility = View.GONE
            song.isPlaying = false
            player.isPlaying = false
//            if (pauseCounter != 0) {
//                if (startCounter > 1) {
//                    mediaPlayer?.setOnPreparedListener {
//                        mediaPlayer?.pause()
//                    }
//                } else {
//                    mediaPlayer?.pause()
//                }
//            }
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

    // 액티비티가 사용자에게 보여지기 직전에 호출된다.
    // onResume() 는 액티비티가 시작되고 사용자와 상호작용하기 직전에 호출된다.
    override fun onStart() {
        super.onStart()
        Log.d("onstart","hi")

        // start 한번 할 때마다 1씩 증가
        startCounter += 1

        // MODE_PRIVATE -> 이 앱에서만 sharedpreference 에 접근할 수 있음
        val sharedPreferences = getSharedPreferences("song", MODE_PRIVATE)
        val jsonSong = sharedPreferences.getString("song", null)
        song = if(jsonSong == null){
            Song("Antifreeze", "백예린 (Yerin Baek)", 0, 254, false, "music_antifreeze")
        } else{
            gson.fromJson(jsonSong, Song::class.java)
        }

        player = Player(song.playTime, song.isPlaying)
        player.start()
        setMiniPlayer(song)
        setPlayerstatus(song.isPlaying)

        if (song.isPlaying)
            mediaPlayer?.start()

    }

    override fun onPause() {
        super.onPause()
        Log.d("onPause","hi")

        player.isPlaying = false // thread 중지

        if (binding.mainPauseBtnIv.isVisible)
            mediaPlayer?.pause()

        song.second = (binding.mainPlayerSb.progress*song.playTime) / 1000

        val sharedPreferences = getSharedPreferences("song", MODE_PRIVATE)
        val editor = sharedPreferences.edit()

        val json = gson.toJson(song)
        editor.putString("song", json)
        editor.apply()
    }

    override fun onDestroy() { // 화면이 꺼질때 실행되는 함수
        Log.d("onDestroy","hi")
        super.onDestroy()
        player.interrupt() // thread 해제
        mediaPlayer?.release() // 미디어 플레이어가 갖고 있던 리소스 해제
        mediaPlayer = null // 미디어플레이어 해제
    }


}

