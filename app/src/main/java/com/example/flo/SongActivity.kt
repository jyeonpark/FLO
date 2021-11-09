package com.example.flo

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.flo.databinding.ActivitySongBinding
import android.app.AlertDialog
import android.content.DialogInterface
import android.media.MediaPlayer
import android.util.Log
import android.view.*
import android.widget.SeekBar
import androidx.core.view.isVisible
import com.google.gson.Gson


class SongActivity : AppCompatActivity() {

    lateinit var binding : ActivitySongBinding

    private var oneRepeating : Boolean = false

    private var song: Song = Song()
    private lateinit var player : Player

    // 미디어 플레이어
    private var mediaPlayer : MediaPlayer? = null
    // Gson
    private var gson: Gson = Gson()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySongBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initSong()

        binding.songDownIb.setOnClickListener { // 화면 내리기
            finish()
        }

        binding.songMiniplayerIv.setOnClickListener { // 일시정지 -> 재생
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

        binding.songPauseIv.setOnClickListener { // 재생 -> 일시정지
            setPlayerstatus(false)
            mediaPlayer?.pause()
        }

        binding.songPlayerSb.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
//                Log.d("seekbar","1")
//                binding.songProgressTimeTv.text = String.format("%02d:%02d", song.playTime/60, song.playTime % 60)
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
                Log.d("seekbar","2")
                song.second = seekBar!!.progress * song.playTime / 1000
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                Log.d("seekbar","3")
                song.second = seekBar!!.progress * song.playTime / 1000
                mediaPlayer?.seekTo(song.second * 1000)
                binding.songProgressTimeTv.text = String.format("%02d:%02d", song.playTime/60, song.playTime % 60)
            }
        })

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
        if (intent.hasExtra("title") && intent.hasExtra("singer") && intent.hasExtra("second") && intent.hasExtra("playTime") && intent.hasExtra("isPlaying") && intent.hasExtra("music")) {
            song.title = intent.getStringExtra("title")!!
            song.singer = intent.getStringExtra("singer")!!
            song.second = intent.getIntExtra("second", 0)
            song.playTime = intent.getIntExtra("playTime", 0)
            song.isPlaying = intent.getBooleanExtra("isPlaying", false)
            song.music = intent.getStringExtra("music")!!

        }
    }

    private fun setMiniPlayer(song: Song) {
        binding.songEndTimeTv.text = String.format("%02d:%02d", song.playTime/60, song.playTime % 60)
        binding.songTitleTv.text = song.title
        binding.songSingerTv.text = song.singer
        binding.songPlayerSb.progress = (song.second*1000/song.playTime)
        binding.songProgressTimeTv.text = String.format("%02d:%02d", song.second/60, song.second%60)


        val music = resources.getIdentifier(song.music, "raw", this.packageName)
        mediaPlayer = MediaPlayer.create(this, music)
        mediaPlayer?.seekTo(song.second * 1000)

    }

    fun setPlayerstatus(isPlaying : Boolean){
        if(isPlaying){
            binding.songMiniplayerIv.visibility = View.GONE
            binding.songPauseIv.visibility = View.VISIBLE
            player.isPlaying = true
            song.isPlaying = true

        }
        else{
            binding.songMiniplayerIv.visibility = View.VISIBLE
            binding.songPauseIv.visibility = View.GONE
            player.isPlaying = false
            song.isPlaying = false
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
                    if(song.second>= playTime){
                        song.second = 0
                        runOnUiThread {
                            binding.songPlayerSb.progress = song.second * 1000 / playTime
                            binding.songProgressTimeTv.text = String.format("%02d:%02d", song.second/60, song.second%60)
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
                        sleep(1000) // 1초마다 더하기
                        song.second++
//                  work thread 에서는 직접 뷰 렌더링 못함 -> 에러
//                  방법 1.
//                  handler.post {
//                  binding.songProgressTimeTv.text = String.format("%02d:%02d", second/60, second%60)
//                  }
//                  방법 2.
                        runOnUiThread {
                            binding.songPlayerSb.progress = song.second * 1000 / playTime
                            binding.songProgressTimeTv.text = String.format("%02d:%02d", song.second/60, song.second%60)
                        }
                    }
                }
            }catch (e : InterruptedException){
                Log.d("interrupt", "쓰레드가 종료되었습니다.")
            }
        }
    }

    override fun onStart() {
        super.onStart()
        Log.d("song_onstart","hi")
        // start 한번 할 때마다 1씩 증가


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
        Log.d("song_onpause","hi")

        player.isPlaying = false // thread 중지

        if (binding.songPauseIv.isVisible)
            mediaPlayer?.pause()

        song.second = (binding.songPlayerSb.progress*song.playTime) / 1000

        val sharedPreferences = getSharedPreferences("song", MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        // Gson
        // 객체->json, json->객체로 변환해주는 중간다리 역할
        val json = gson.toJson(song)
        editor.putString("song", json)

        editor.apply()
    }

    override fun onDestroy() { // 화면이 꺼질때 실행되는 함수
        Log.d("song_ondestroy","hi")
        super.onDestroy()

        player.interrupt() // thread 해제
        mediaPlayer?.release() // 미디어 플레이어가 갖고 있던 리소스 해제
        mediaPlayer = null // 미디어플레이어 해제
    }
}
