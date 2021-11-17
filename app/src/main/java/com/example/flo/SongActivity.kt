package com.example.flo

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.flo.databinding.ActivitySongBinding
import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.res.Resources
import android.media.MediaPlayer
import android.util.Log
import android.view.*
import android.widget.SeekBar
import androidx.databinding.DataBindingUtil
import com.example.flo.databinding.ToastCustomBinding
import kotlinx.android.synthetic.main.toast_custom.view.*
import java.util.concurrent.TimeUnit


class SongActivity : AppCompatActivity() {

    lateinit var binding: ActivitySongBinding

    private var oneRepeating: Boolean = false

    private var mediaPlayer: MediaPlayer? = null
    lateinit var timer: Timer

    private var songs = ArrayList<Song>()
    private var nowPos = 0
    private lateinit var songDB: SongDatabase


//    // Gson
//    private var gson: Gson = Gson()
//
//    private var songPosition : Int = 0
//    private var songList = ArrayList<Song>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySongBinding.inflate(layoutInflater)
        setContentView(binding.root)

        Log.d("oncreate", "song")

//        binding.songMiniplayerIv.setOnClickListener { // 일시정지 -> 재생
//            setPlayerstatus(true)
//
//            if (player.isAlive){
//                player.isPlaying = true
//            }
//            else{
//                player = Player(song.playTime, song.isPlaying)
//                player.start()
//            }
//            mediaPlayer?.start()
//        }
//
//        binding.songPauseIv.setOnClickListener { // 재생 -> 일시정지
//            setPlayerstatus(false)
//            mediaPlayer?.pause()
//        }

        binding.songPlayerSb.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
//                Log.d("seekbar","1")
           }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
//                Log.d("seekbar", "2")
                songs[nowPos].second = seekBar!!.progress * songs[nowPos].playTime / 1000
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
//                Log.d("seekbar", "3")
                songs[nowPos].second = seekBar!!.progress * songs[nowPos].playTime / 1000
                mediaPlayer?.seekTo(songs[nowPos].second * 1000)
                binding.songProgressTimeTv.text = String.format(
                    "%02d:%02d",
                    songs[nowPos].playTime / 60,
                    songs[nowPos].playTime % 60
                )
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
    }

//        binding.songMyLikeOff.setOnClickListener { // 좋아요 누르기
//            setLikestatus(false)
//        }
//
//        binding.songMyLikeOn.setOnClickListener { // 좋아요 끄기
//            setLikestatus(true)
//        }
//
//        binding.songUnlikeOff.setOnClickListener { // 싫어요 누르기
//            setUnlikestatus(true)
//        }
//
//        binding.songUnlikeOn.setOnClickListener { // 싫어요 끄기
//            setUnlikestatus(false)
//        }
//
//    }


//    private fun setMiniPlayer(song: Song) {
//        binding.songEndTimeTv.text = String.format("%02d:%02d", song.playTime/60, song.playTime % 60)
//        binding.songTitleTv.text = song.title
//        binding.songSingerTv.text = song.singer
//        binding.songPlayerSb.progress = (song.second*1000/song.playTime)
//        binding.songProgressTimeTv.text = String.format("%02d:%02d", song.second/60, song.second%60)
//
//
//        val music = resources.getIdentifier(song.music, "raw", this.packageName)
//        mediaPlayer = MediaPlayer.create(this, music)
//        mediaPlayer?.seekTo(song.second * 1000)
//
//    }

//    fun setPlayerstatus(isPlaying : Boolean){
//        if(isPlaying){
//            binding.songMiniplayerIv.visibility = View.GONE
//            binding.songPauseIv.visibility = View.VISIBLE
//            player.isPlaying = true
//            song.isPlaying = true
//
//        }
//        else{
//            binding.songMiniplayerIv.visibility = View.VISIBLE
//            binding.songPauseIv.visibility = View.GONE
//            player.isPlaying = false
//            song.isPlaying = false
//        }
//    }

    fun setRandomstatus(isRandom: Boolean) {
        if (isRandom) {
            binding.songRandomOffIv.visibility = View.GONE
            binding.songRandomOnIv.visibility = View.VISIBLE
            Toast.makeText(this, "재생목록을 랜덤으로 재생합니다.", Toast.LENGTH_SHORT).show()
        } else {
            binding.songRandomOffIv.visibility = View.VISIBLE
            binding.songRandomOnIv.visibility = View.GONE
            Toast.makeText(this, "재생목록을 순서대로 재생합니다.", Toast.LENGTH_SHORT).show()

        }
    }

    fun setRepeatstatus(Repeating: Int) {
        if (Repeating == 0) {
            binding.songRepeatOffIv.visibility = View.GONE
            binding.songRepeatOnIv.visibility = View.VISIBLE
            Toast.makeText(this, "전체 음악을 반복합니다.", Toast.LENGTH_SHORT).show()
        } else if (Repeating == 1) {
            binding.songRepeatOnIv.visibility = View.GONE
            binding.songRepeatOn1Iv.visibility = View.VISIBLE
            oneRepeating = true
            Toast.makeText(this, "현재 음악을 반복합니다.", Toast.LENGTH_SHORT).show()
        } else {
            binding.songRepeatOn1Iv.visibility = View.GONE
            binding.songRepeatOffIv.visibility = View.VISIBLE
            Toast.makeText(this, "반복을 사용하지 않습니다.", Toast.LENGTH_SHORT).show()
        }
    }

    fun setUnlikestatus(unlike: Boolean) {
        if (unlike) {
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
        } else {
            binding.songUnlikeOff.visibility = View.VISIBLE
            binding.songUnlikeOn.visibility = View.GONE
        }
    }

//    inner class Player(private val playTime:Int, var isPlaying: Boolean) : Thread(){
//
//        override fun run() {
//            try {
//                while(true){
//                    if(song.second>= playTime){
//                        if (oneRepeating) {
//                            song.second = 0
//                            runOnUiThread {
//                                binding.songPlayerSb.progress = song.second * 1000 / playTime
//                                binding.songProgressTimeTv.text =
//                                    String.format("%02d:%02d", song.second / 60, song.second % 60)
//                            }
//                        }
////                        else {
////                            song = songList[songPosition % songList.size]
////                            player.interrupt()
////                            mediaPlayer?.release()
////                            mediaPlayer = null
////                            runOnUiThread {
////                                initSong()
////                            }
////                            break
////                        }
//                    }
//
//                    if (isPlaying){
//                        sleep(1000) // 1초마다 더하기
//                        song.second++
////                  work thread 에서는 직접 뷰 렌더링 못함 -> 에러
////                  방법 1.
////                  handler.post {
////                  binding.songProgressTimeTv.text = String.format("%02d:%02d", second/60, second%60)
////                  }
////                  방법 2.
//                        runOnUiThread {
//                            binding.songPlayerSb.progress = song.second * 1000 / playTime
//                            binding.songProgressTimeTv.text = String.format("%02d:%02d", song.second/60, song.second%60)
//                        }
//                    }
//                }
//            }catch (e : InterruptedException){
//                Log.d("interrupt", "쓰레드가 종료되었습니다.")
//            }
//        }
//    }

    override fun onStart() {
        super.onStart()
        Log.d("onstart", "song")

        initPlayList()
        initSong()
        initClickListener()

    }


    override fun onPause() {
        super.onPause()
        Log.d("onPause", "song")

        songs[nowPos].second = (songs[nowPos].playTime * binding.songPlayerSb.progress) / 1000
//        songs[nowPos].isPlaying = false
//        setPlayerStatus(false)
        timer.isPlaying = false

        timer.interrupt() // 스레드를 해제함

        mediaPlayer?.release() // 미디어플레이어가 가지고 있던 리소스를 해방
        mediaPlayer = null // 미디어플레이어 해제

        val sharedPreferences = getSharedPreferences("song", MODE_PRIVATE)
        val editor = sharedPreferences.edit()

        songDB = SongDatabase.getInstance(this)!!
        songDB.songDao().update(songs[nowPos])

        editor.putInt("songId", songs[nowPos].id)
        editor.apply()

    }

    override fun onDestroy() { // 화면이 꺼질때 실행되는 함수
        super.onDestroy()

        Log.d("onDestroy", "song")

//        timer.interrupt() // 스레드를 해제함
//
//        mediaPlayer?.release() // 미디어플레이어가 가지고 있던 리소스를 해방
//        mediaPlayer = null // 미디어플레이어 해제

    }


    private fun initPlayList() {
        songDB = SongDatabase.getInstance(this)!!
        songs.addAll(songDB.songDao().getSongs())
    }


    private fun startTimer() {
        timer = Timer(songs[nowPos].playTime, songs[nowPos].isPlaying)
        timer.start()
    }

    private fun initSong() {
        val spf = getSharedPreferences("song", MODE_PRIVATE)
        val songId = spf.getInt("songId", 0)

        nowPos = getPlayingSongPosition(songId)

        Log.d("onstart : song second",songs[nowPos].second.toString())

        Log.d("now Song ID", songs[nowPos].id.toString())

        startTimer()
        setPlayer(songs[nowPos])
    }

    private fun getPlayingSongPosition(songId: Int): Int {
        for (i in 0 until songs.size) {
            if (songs[i].id == songId) {
                return i
            }
        }
        return 0
    }

    private fun setPlayer(song: Song) {
        val music = resources.getIdentifier(song.music, "raw", this.packageName)

        binding.songTitleTv.text = song.title
        binding.songSingerTv.text = song.singer
        binding.songProgressTimeTv.text =
            String.format("%02d:%02d", song.second / 60, song.second % 60)
        binding.songEndTimeTv.text =
            String.format("%02d:%02d", song.playTime / 60, song.playTime % 60)
        binding.songAlbumIv.setImageResource(song.coverImg!!)
        binding.songPlayerSb.progress = (song.second * 1000 / song.playTime)

        setPlayerStatus(song.isPlaying)

        if (song.isLike) {
            binding.songMyLikeOn.setImageResource(R.drawable.ic_my_like_on)
        } else {
            binding.songMyLikeOn.setImageResource(R.drawable.ic_my_like_off)
        }

        mediaPlayer = MediaPlayer.create(this, music)
        mediaPlayer?.seekTo(song.second * 1000)
        if (song.isPlaying)
            mediaPlayer?.start()
    }

    private fun initClickListener() {
        binding.songDownIb.setOnClickListener {
            finish()
        }

        binding.songMiniplayerIv.setOnClickListener {
            setPlayerStatus(true)
        }

        binding.songPauseIv.setOnClickListener {
            setPlayerStatus(false)
        }

        binding.songPreviousIv.setOnClickListener {
            moveSong(-1)
        }

        binding.songNextIv.setOnClickListener {
            moveSong(+1)
        }

        binding.songMyLikeOn.setOnClickListener {
            setLike(songs[nowPos].isLike)
        }

    }

    private fun moveSong(direct: Int) {

        if (nowPos + direct < 0) {
            nowPos = songs.size - 1
        } else if (nowPos + direct >= songs.size) {
            nowPos = 0
        } else {
            nowPos += direct
        }

        songs[nowPos].second = 0

        timer.interrupt()
        startTimer()

        mediaPlayer?.release() // 미디어플레이어가 가지고 있던 리소스를 해방
        mediaPlayer = null // 미디어플레이어 해제

        // 다음 곡의 second = 0, isplaying 은 true 값으로 default 설정
        songs[nowPos].second = 0
        songs[nowPos].isPlaying = true

        setPlayer(songs[nowPos])
    }

    private fun setLike(isLike: Boolean) {
        songs[nowPos].isLike = !isLike
        songDB.songDao().updateIsLikeById(!isLike, songs[nowPos].id)

        if (isLike) {
            binding.songMyLikeOn.setImageResource(R.drawable.ic_my_like_off)
            Log.d("좋아요 취소","클릭")
            ToastCustom.createToast(context = this, "좋아요 한 곡이 취소되었습니다.")?.show()

        } else {
            binding.songMyLikeOn.setImageResource(R.drawable.ic_my_like_on)
            Log.d("좋아요","클릭")
            ToastCustom.createToast(context = this, "좋아요 한 곡에 담겼습니다.")?.show()
        }
    }

    object ToastCustom {

        fun createToast(context: Context, message: String): Toast? {
            val toastinflater = LayoutInflater.from(context)
            val toastbinding: ToastCustomBinding =
                DataBindingUtil.inflate(toastinflater, R.layout.toast_custom, null, false)

            toastbinding.toastTextTv.text = message

            return Toast(context).apply {
//                setGravity(Gravity.BOTTOM or Gravity.CENTER, 0, 16.toPx())
                setGravity(Gravity.BOTTOM or Gravity.FILL_HORIZONTAL, 0, 120.toPx())
                duration = Toast.LENGTH_SHORT
                view = toastbinding.root
            }
        }

        private fun Int.toPx(): Int = (this * Resources.getSystem().displayMetrics.density).toInt()
    }

    private fun setPlayerStatus(isPlaying: Boolean) {
        timer.isPlaying = isPlaying
        songs[nowPos].isPlaying = isPlaying

        if (isPlaying) {
            binding.songMiniplayerIv.visibility = View.GONE
            binding.songPauseIv.visibility = View.VISIBLE

            mediaPlayer?.start()
        } else {
            binding.songMiniplayerIv.visibility = View.VISIBLE
            binding.songPauseIv.visibility = View.GONE

            mediaPlayer?.pause()
        }
    }

    inner class Timer(private val playTime: Int = 0, var isPlaying: Boolean = false) : Thread() {
//        private var second: Long = songs[nowPos].second.toLong()

        @SuppressLint("SetTextI18n")
        override fun run() {
            try {
                while (true) {
                    if (songs[nowPos].second >= playTime) {
                        if (oneRepeating) {
                            songs[nowPos].second = 0
                            runOnUiThread {
                                binding.songPlayerSb.progress =
                                    songs[nowPos].second * 1000 / playTime
                                binding.songProgressTimeTv.text =
                                    String.format(
                                        "%02d:%02d",
                                        songs[nowPos].second / 60,
                                        songs[nowPos].second % 60
                                    )
                            }
                        } else {
                            songs[nowPos].second = 0
                            timer.interrupt()
                            mediaPlayer?.release()
                            mediaPlayer = null
                            runOnUiThread {
                                moveSong(+1)
                            }
                            break
                        }
                    }

                    if (isPlaying) {
                        sleep(1000)
                        songs[nowPos].second++

                        runOnUiThread {
                            binding.songPlayerSb.progress =
                                (songs[nowPos].second * 1000 / playTime).toInt()
                            binding.songProgressTimeTv.text = String.format(
                                "%02d:%02d",
                                TimeUnit.SECONDS.toMinutes(songs[nowPos].second.toLong()),
                                songs[nowPos].second % 60
                            )
                        }
                    }
                }
            } catch (e: InterruptedException) {
                Log.d("SONG", "쓰레드가 죽었습니다. ${e.message}")
            }
        }
    }
}
