package com.example.flo

import CustomDialog
import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.flo.databinding.ActivitySongBinding
import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Color
import android.graphics.Point
import android.media.MediaPlayer
import android.util.Log
import android.view.*
import android.widget.SeekBar
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.snackbar.Snackbar.SnackbarLayout
import kotlinx.android.synthetic.main.song_snackbar_custom.view.*
import java.util.concurrent.TimeUnit


class SongActivity : AppCompatActivity() {

    lateinit var binding: ActivitySongBinding

    private var oneRepeating: Boolean = false

    private var mediaPlayer: MediaPlayer? = null
    lateinit var timer: Timer

    private var songs = ArrayList<Song>()
    private var nowPos = 0
    private lateinit var songDB: SongDatabase

    private var snackbar: Snackbar? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySongBinding.inflate(layoutInflater)
        setContentView(binding.root)

        Log.d("oncreate", "song")

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
                    songs[nowPos].second / 60,
                    songs[nowPos].second % 60
                )
            }
        })

        binding.songRepeatOffIv.setOnClickListener { // ?????????????????? ??????
            setRepeatstatus(0)
        }

        binding.songRepeatOnIv.setOnClickListener { // ?????????????????? ??????
            setRepeatstatus(1)
        }

        binding.songRepeatOn1Iv.setOnClickListener { // ???????????? ??????
            setRepeatstatus(2)
        }

        binding.songRandomOffIv.setOnClickListener { // ???????????? ??????
            setRandomstatus(true)
        }

        binding.songRandomOnIv.setOnClickListener { // ???????????? ??????
            setRandomstatus(false)
        }
    }

    fun setRandomstatus(isRandom: Boolean) {
        if (isRandom) {
            binding.songRandomOffIv.visibility = View.GONE
            binding.songRandomOnIv.visibility = View.VISIBLE
            Toast.makeText(this, "??????????????? ???????????? ???????????????.", Toast.LENGTH_SHORT).show()
        } else {
            binding.songRandomOffIv.visibility = View.VISIBLE
            binding.songRandomOnIv.visibility = View.GONE
            Toast.makeText(this, "??????????????? ???????????? ???????????????.", Toast.LENGTH_SHORT).show()

        }
    }

    fun setRepeatstatus(Repeating: Int) {
        if (Repeating == 0) {
            binding.songRepeatOffIv.visibility = View.GONE
            binding.songRepeatOnIv.visibility = View.VISIBLE
            Toast.makeText(this, "?????? ????????? ???????????????.", Toast.LENGTH_SHORT).show()
        } else if (Repeating == 1) {
            binding.songRepeatOnIv.visibility = View.GONE
            binding.songRepeatOn1Iv.visibility = View.VISIBLE
            oneRepeating = true
            Toast.makeText(this, "?????? ????????? ???????????????.", Toast.LENGTH_SHORT).show()
        } else {
            binding.songRepeatOn1Iv.visibility = View.GONE
            binding.songRepeatOffIv.visibility = View.VISIBLE
            Toast.makeText(this, "????????? ???????????? ????????????.", Toast.LENGTH_SHORT).show()
        }
    }

    fun setUnlikestatus(unlike: Boolean) {
        if (unlike) {
            AlertDialog.Builder(this)
                .setMessage("???????????? ???????????? ???????????? ???????????? ????????????.")
                .setPositiveButton("??????", object : DialogInterface.OnClickListener {
                    override fun onClick(dialog: DialogInterface, which: Int) {
                        binding.songUnlikeOff.visibility = View.GONE
                        binding.songUnlikeOn.visibility = View.VISIBLE
                    }
                })
                .setNegativeButton("??????", object : DialogInterface.OnClickListener {
                    override fun onClick(dialog: DialogInterface, which: Int) {}
                })
                .create()
                .show()
        } else {
            binding.songUnlikeOff.visibility = View.VISIBLE
            binding.songUnlikeOn.visibility = View.GONE
        }
    }

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
        timer.isPlaying = false

        timer.interrupt() // ???????????? ?????????

        mediaPlayer?.release() // ???????????????????????? ????????? ?????? ???????????? ??????
        mediaPlayer = null // ????????????????????? ??????

        val sharedPreferences = getSharedPreferences("song", MODE_PRIVATE)
        val editor = sharedPreferences.edit()

        Log.d("onpause : song second", songs[nowPos].second.toString())
        songDB = SongDatabase.getInstance(this)!!
        songDB.songDao().update(songs[nowPos])
        editor.putInt("songId", songs[nowPos].id)
        editor.apply()

    }

    override fun onDestroy() { // ????????? ????????? ???????????? ??????
        super.onDestroy()

        Log.d("onDestroy", "song")

//        timer.interrupt() // ???????????? ?????????
//
//        mediaPlayer?.release() // ???????????????????????? ????????? ?????? ???????????? ??????
//        mediaPlayer = null // ????????????????????? ??????

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
        val songId = spf.getInt("songId", 1)

        nowPos = getPlayingSongPosition(songId)

        Log.d("onstart : song second", songs[nowPos].second.toString())

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

        val userId = getUserIdx(this)
        if (userId != 0) {
            if (isLikedSong(song.id)) {
                binding.songMyLikeOn.setImageResource(R.drawable.ic_my_like_on)
            } else {
                binding.songMyLikeOn.setImageResource(R.drawable.ic_my_like_off)
            }
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
            setLike(isLikedSong(songs[nowPos].id))
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

        mediaPlayer?.release() // ???????????????????????? ????????? ?????? ???????????? ??????
        mediaPlayer = null // ????????????????????? ??????

        // ?????? ?????? second = 0, isplaying ??? true ????????? default ??????
        songs[nowPos].second = 0
        songs[nowPos].isPlaying = true

        setPlayer(songs[nowPos])
    }

    private fun setLike(isLike: Boolean) {
        val userId = getUserIdx(this)
        if (userId != 0) {
            if (isLike) {
                binding.songMyLikeOn.setImageResource(R.drawable.ic_my_like_off)
                Log.d("????????? ??????", "??????")
                createSnackBar(binding.root, "????????? ??? ?????? ?????????????????????.")
                disLikeSong(userId, songs[nowPos].id)
            } else {
                binding.songMyLikeOn.setImageResource(R.drawable.ic_my_like_on)
                Log.d("?????????", "??????")
                createSnackBar(binding.root, "????????? ??? ?????? ???????????????.")
                likeSong(userId, songs[nowPos].id)
            }
        } else {
            // ???????????? ?????? ??????
            val dialog = CustomDialog(this)
            dialog.showDialog(this)

        }
    }

    private fun isLikedSong(songId: Int): Boolean {
        val songDB = SongDatabase.getInstance(this)!!
        val userId = getUserIdx(this)

        val likeId: Int? = songDB.songDao().isLikeSong(userId, songId)

        // likeId ??? null ??? ????????? true, ????????? false ??????
        return likeId != null
    }

    private fun likeSong(userId: Int, songId: Int) {
        val songDB = SongDatabase.getInstance(this)!!
        val like = LikedSongs(userId, songId)

        songDB.songDao().likeSong(like)
    }

    private fun disLikeSong(userId: Int, songId: Int) {
        val songDB = SongDatabase.getInstance(this)!!
        songDB.songDao().disLikeAlbum(userId, songId)
    }


    fun createSnackBar(view: View, message: String) {

        snackbar = Snackbar.make(view, "", Snackbar.LENGTH_SHORT)
        val customSnackView: View = layoutInflater.inflate(R.layout.song_snackbar_custom, null)
        snackbar!!.view.setBackgroundColor(Color.TRANSPARENT)
        snackbar!!.anchorView = binding.songMiniplayerIv
        customSnackView.snackbar_tv.text = message
        val snackbarLayout = snackbar!!.view as SnackbarLayout
        snackbarLayout.setPadding(20, 0, 20, 0)

        snackbarLayout.addView(customSnackView, 0)
        snackbar!!.show()

    }

    /**
     * On each touch event:
     * Check is [snackbar] present and displayed
     * and dismiss it if user touched anywhere outside it's bounds
     */
    override fun dispatchTouchEvent(ev: MotionEvent): Boolean {
        // ???????????? snackbar dismiss
        snackbar?.takeIf { it.isShown }?.run {
            snackbar?.view?.visibility = View.GONE;
            dismiss()
            snackbar = null
        }

        return super.dispatchTouchEvent(ev)
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
                Log.d("SONG", "???????????? ???????????????. ${e.message}")
            }
        }
    }
}
