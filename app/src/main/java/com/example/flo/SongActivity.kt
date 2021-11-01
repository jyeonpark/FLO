package com.example.flo

import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.flo.databinding.ActivitySongBinding
import android.R
import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.content.SharedPreferences
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.*


class SongActivity : AppCompatActivity() {

    lateinit var binding : ActivitySongBinding
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var editor: SharedPreferences.Editor
    private var second: Int = 0
    private var oneRepeating : Boolean = false

    private val song: Song = Song()
    private lateinit var player : Player
    // private val handler = Handler(Looper.getMainLooper())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySongBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sharedPreferences = getSharedPreferences("songShared",0)
        editor = sharedPreferences.edit()

        initSong()

        player = Player(song.playTime, song.isPlaying)
        player.start()

        binding.songDownIb.setOnClickListener { // 화면 내리기
            finish()
        }

        binding.songMiniplayerIv.setOnClickListener { // 일시정지 -> 재생
            player.isPlaying = true
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

        binding.songPauseIv.setOnClickListener { // 재생 -> 일시정지
            player.isPlaying = false
            setPlayerstatus(false)
        }

        binding.songRepeatOffIv.setOnClickListener { // 전체반복재생 켜기
            setRepeatstatus(0)
        }

        binding.songRepeatOnIv.setOnClickListener { // 현재반복재생 켜기
            setRepeatstatus(1)
        }

        binding.songRepeatOn1Iv.setOnClickListener { // 반복재생 끄기
            setRepeatstatus(2)
        }

        binding.songRandomOffIv.setOnClickListener { // 랜덤재생 켜기
            setRandomstatus(true)
        }

        binding.songRandomOnIv.setOnClickListener { // 랜덤재생 끄기
            setRandomstatus(false)
        }

        binding.songMyLikeOff.setOnClickListener { // 좋아요 누르기
            setLikestatus(false)
        }

        binding.songMyLikeOn.setOnClickListener { // 좋아요 끄기
            setLikestatus(true)
        }

        binding.songUnlikeOff.setOnClickListener { // 싫어요 누르기
            setUnlikestatus(true)
        }

        binding.songUnlikeOn.setOnClickListener { // 싫어요 끄기
            setUnlikestatus(false)
        }

    }

    private fun initSong(){
        if (intent.hasExtra("title") && intent.hasExtra("singer") && intent.hasExtra("playTime") && intent.hasExtra("isPlaying")) {
            song.title = intent.getStringExtra("title")!!
            song.singer = intent.getStringExtra("singer")!!
            song.playTime = intent.getIntExtra("playTime", 0)
            song.isPlaying = intent.getBooleanExtra("isPlaying", false)
            binding.songEndTimeTv.text = String.format("%02d:%02d", song.playTime/60, song.playTime % 60)
            binding.songTitleTv.text = song.title
            binding.songSingerTv.text = song.singer
            setPlayerstatus(song.isPlaying)

            second = sharedPreferences.getInt("second", 0)
            Log.d("second",second.toString())
            binding.songPlayerSb.progress = second * 1000 / song.playTime
            binding.songProgressTimeTv.text = String.format("%02d:%02d", second/60, second%60)
        }
    }

    fun setPlayerstatus(isPlaying : Boolean){
        if(isPlaying){
            binding.songMiniplayerIv.visibility = View.GONE
            binding.songPauseIv.visibility = View.VISIBLE
        }
        else{
            binding.songMiniplayerIv.visibility = View.VISIBLE
            binding.songPauseIv.visibility = View.GONE
        }
    }

    fun setRandomstatus(isRandom : Boolean) {
        if(isRandom){
            binding.songRandomOffIv.visibility = View.GONE
            binding.songRandomOnIv.visibility = View.VISIBLE
            Toast.makeText(this, "재생목록을 랜덤으로 재생합니다.", Toast.LENGTH_SHORT).show()
        }
        else{
            binding.songRandomOffIv.visibility = View.VISIBLE
            binding.songRandomOnIv.visibility = View.GONE
            Toast.makeText(this, "재생목록을 순서대로 재생합니다.", Toast.LENGTH_SHORT).show()

        }
    }

    fun setRepeatstatus(Repeating : Int){
        if(Repeating == 0){
            binding.songRepeatOffIv.visibility = View.GONE
            binding.songRepeatOnIv.visibility = View.VISIBLE
            Toast.makeText(this, "전체 음악을 반복합니다.", Toast.LENGTH_SHORT).show()
        }
        else if (Repeating == 1){
            binding.songRepeatOnIv.visibility = View.GONE
            binding.songRepeatOn1Iv.visibility = View.VISIBLE
            oneRepeating = true
            Toast.makeText(this, "현재 음악을 반복합니다.", Toast.LENGTH_SHORT).show()
        }
        else{
            binding.songRepeatOn1Iv.visibility = View.GONE
            binding.songRepeatOffIv.visibility = View.VISIBLE
            Toast.makeText(this, "반복을 사용하지 않습니다.", Toast.LENGTH_SHORT).show()
        }
    }

    fun setLikestatus(like : Boolean){
        if(like){
            binding.songMyLikeOff.visibility = View.VISIBLE
            binding.songMyLikeOn.visibility = View.GONE
            Toast.makeText(this, "좋아요 한 곡에 담겼습니다.", Toast.LENGTH_SHORT).show()
        }
        else{
            binding.songMyLikeOff.visibility = View.GONE
            binding.songMyLikeOn.visibility = View.VISIBLE
            Toast.makeText(this, "좋아요 한 곡이 취소되었습니다.", Toast.LENGTH_SHORT).show()
        }
    }

    fun setUnlikestatus(unlike : Boolean){
        if(unlike) {
            AlertDialog.Builder(this)
                .setMessage("이제부터 추천에서 제외되며 재생하지 않습니다.")
                .setPositiveButton("확인", object : DialogInterface.OnClickListener {
                    override fun onClick(dialog: DialogInterface, which: Int) {
                        binding.songUnlikeOff.visibility = View.GONE
                        binding.songUnlikeOn.visibility = View.VISIBLE
                    }
                })
                .setNegativeButton("취소", object : DialogInterface.OnClickListener {
                    override fun onClick(dialog: DialogInterface, which: Int) {}
                })
                .create()
                .show()
        }
        else{
            binding.songUnlikeOff.visibility = View.VISIBLE
            binding.songUnlikeOn.visibility = View.GONE
        }
    }

    inner class Player(private val playTime:Int, var isPlaying: Boolean) : Thread(){

        override fun run() {
            try {
                while(true){
                    if(second>= playTime){
                        second = 0
                        runOnUiThread {
                            binding.songPlayerSb.progress = second * 1000 / playTime
                            binding.songProgressTimeTv.text = String.format("%02d:%02d", second/60, second%60)
                        }
                        if (!oneRepeating){
                            song.isPlaying = false
                            runOnUiThread {
                                setPlayerstatus(song.isPlaying)
                            }
                            player.interrupt()
                            break
                        }
                    }

                    if (isPlaying){
                        Log.d("if문", "진입")
                        sleep(1000) // 1초마다 더하기
                        second++
//                  work thread 에서는 직접 뷰 렌더링 못함 -> 에러
//                  방법 1.
//                  handler.post {
//                  binding.songProgressTimeTv.text = String.format("%02d:%02d", second/60, second%60)
//                  }
//                  방법 2.
                        runOnUiThread {
                            binding.songPlayerSb.progress = second * 1000 / playTime
                            binding.songProgressTimeTv.text = String.format("%02d:%02d", second/60, second%60)
                        }
                    }
                }
            }catch (e : InterruptedException){
                Log.d("interrupt", "쓰레드가 종료되었습니다.")
            }
        }
    }

    override fun onDestroy() { // 화면이 꺼질때 실행되는 함수
        player.interrupt()

        Log.d("song second", second.toString())
        editor.putBoolean("isPlaying", song.isPlaying)
        editor.putInt("second", second)
        editor.apply()

        super.onDestroy()
    }
}
