package com.example.flo

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.media.MediaPlayer
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.widget.SeekBar
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContentProviderCompat.requireContext
import com.example.flo.databinding.ActivityMainBinding
import java.lang.Thread.sleep
import java.util.*
import kotlin.collections.ArrayList

class MainActivity : AppCompatActivity(), OnAlbumClickListener, OnAlbumSongClickListner {

    lateinit var binding: ActivityMainBinding
    lateinit var context: Context

    private var mediaPlayer: MediaPlayer? = null
    private var timer: MainActivity.Timer? = null

    private var songs = ArrayList<Song>()
    private var nowPos = 0
    private lateinit var songDB: SongDatabase

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        Log.d("oncreate", "main")

        context = applicationContext

        initNavigation()
        inputDummyAlbums()
        inputDummySongs()


        binding.mainPlayerLayout.setOnClickListener {
            Log.d("nowSongId", songs[nowPos].id.toString())
            val editor = getSharedPreferences("song", MODE_PRIVATE).edit()
            editor.putInt("songId", songs[nowPos].id)
            editor.apply()

            val intent = Intent(this@MainActivity, SongActivity::class.java)

            startActivity(intent)
        }

        binding.mainPlayerSb.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
                songs[nowPos].second = seekBar!!.progress * songs[nowPos].playTime / 1000
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                songs[nowPos].second = seekBar!!.progress * songs[nowPos].playTime / 1000
                mediaPlayer?.seekTo(songs[nowPos].second * 1000)
            }
        })

    }

    private fun initNavigation() {
        supportFragmentManager.beginTransaction().replace(R.id.main_frm, HomeFragment(this))
            .commitAllowingStateLoss()

        binding.mainBnv.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.homeFragment -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.main_frm, HomeFragment(this))
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
    }

    // interface ??????
    override fun onAlbumClick(album: Album) {

        if (timer != null) {
            timer!!.isPlaying = false
            timer!!.interrupt() // ???????????? ?????????
        }
        Log.d("thread ??????", "????????????")
        mediaPlayer?.release() // ???????????????????????? ????????? ?????? ???????????? ??????
        mediaPlayer = null // ????????????????????? ??????
        nowPos = 0

        songs.clear()
        songDB = SongDatabase.getInstance(this)!!
        songs.addAll(songDB.songDao().getSongsInAlbum(album.id))
        for (i in songs){
            i.second = 0;
            i.isPlaying = true
        }

        startTimer()
        setPlayer(songs[nowPos])
    }

    // ????????? ?????? ???????????????
    override fun onSongClick(song: Song) {

        Log.d("context", applicationContext.toString())

        if (timer != null) {
            timer!!.isPlaying = false
            timer!!.interrupt() // ???????????? ?????????
        }

        mediaPlayer?.release() // ???????????????????????? ????????? ?????? ???????????? ??????
        mediaPlayer = null // ????????????????????? ??????
        nowPos = 0

        songs.clear()
        songDB = SongDatabase.getInstance(this)!!
        song.second = 0
        song.isPlaying = true
        songs.add(song)

        startTimer()
        setPlayer(song)
    }



    // ??????????????? ??????????????? ???????????? ????????? ????????????.
    // onResume() ??? ??????????????? ???????????? ???????????? ?????????????????? ????????? ????????????.
    override fun onStart() {
        super.onStart()
        Log.d("onstart", "main")

        initPlayList()
        initSong()
        initClickListener()
    }

    override fun onPause() {
        super.onPause()
        Log.d("onPause", "main")

        songs[nowPos].second = (songs[nowPos].playTime * binding.mainPlayerSb.progress) / 1000
        timer!!.isPlaying = false

        timer!!.interrupt() // ???????????? ?????????

        mediaPlayer?.release() // ???????????????????????? ????????? ?????? ???????????? ??????
        mediaPlayer = null // ????????????????????? ??????

        Log.d("onpause : main second", songs[nowPos].second.toString())
        songDB = SongDatabase.getInstance(this)!!
        songDB.songDao().update(songs[nowPos])

        val sharedPreferences = getSharedPreferences("song", MODE_PRIVATE)
        val editor = sharedPreferences.edit()

        editor.putInt("songId", songs[nowPos].id)
        editor.apply()
    }

    override fun onDestroy() { // ????????? ????????? ???????????? ??????
        super.onDestroy()

        Log.d("onDestroy", "main")

    }

    private fun initPlayList() {
        songDB = SongDatabase.getInstance(this)!!
        songs.addAll(songDB.songDao().getSongs())

    }


    private fun startTimer() {
        timer = Timer(songs[nowPos].playTime, songs[nowPos].isPlaying)
        timer!!.start()
    }

    private fun initSong() {
        val spf = getSharedPreferences("song", MODE_PRIVATE)
        val songId = spf.getInt("songId", 1)

        nowPos = getPlayingSongPosition(songId)
        songs[nowPos].second = songDB.songDao().getSong(songId).second
        songs[nowPos].isPlaying = songDB.songDao().getSong(songId).isPlaying

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
        binding.mainMiniplayerTitleTv.text = song.title
        binding.mainMiniplayerSingerTv.text = song.singer

        binding.mainPlayerSb.progress = (song.second * 1000 / song.playTime)

        setPlayerStatus(song.isPlaying)

        mediaPlayer = MediaPlayer.create(this, music)
        mediaPlayer?.seekTo(song.second * 1000)
        if (song.isPlaying)
            mediaPlayer?.start()

    }

    private fun initClickListener() {

        binding.mainMiniplayerBtnIv.setOnClickListener {
            setPlayerStatus(true)
        }

        binding.mainPauseBtnIv.setOnClickListener {
            setPlayerStatus(false)
        }

        binding.mainPreviousBtnIv.setOnClickListener {
            moveSong(-1)
        }

        binding.mainNextBtnIv.setOnClickListener {
            moveSong(+1)
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

        timer!!.interrupt()
        startTimer()

        mediaPlayer?.release() // ???????????????????????? ????????? ?????? ???????????? ??????
        mediaPlayer = null // ????????????????????? ??????

        // ?????? ?????? second = 0, isplaying ??? true ????????? default ??????
        songs[nowPos].second = 0
        songs[nowPos].isPlaying = true

        setPlayer(songs[nowPos])
    }


    private fun setPlayerStatus(isPlaying: Boolean) {
        timer!!.isPlaying = isPlaying
        songs[nowPos].isPlaying = isPlaying

        if (isPlaying) {
            binding.mainMiniplayerBtnIv.visibility = View.GONE
            binding.mainPauseBtnIv.visibility = View.VISIBLE

            mediaPlayer?.start()
        } else {
            binding.mainMiniplayerBtnIv.visibility = View.VISIBLE
            binding.mainPauseBtnIv.visibility = View.GONE

            mediaPlayer?.pause()
        }
    }

    inner class Timer(private val playTime: Int = 0, var isPlaying: Boolean = false) : Thread() {
//        private var second: Long = songs[nowPos].second.toLong()

        @SuppressLint("SetTextI18n")
        override fun run() {
            try {
                while (true) {
                    if (songs.size != 0 && songs.isNotEmpty()) {
                        if (isPlaying && songs[nowPos].second >= playTime) {
                            songs[nowPos].second = 0
                            timer!!.interrupt()
                            mediaPlayer?.release()
                            mediaPlayer = null
                            runOnUiThread {
                                moveSong(+1)
                            }
                            break
                        }

                        if (isPlaying) {
                            sleep(1000)
                            songs[nowPos].second++

                            runOnUiThread {
                                binding.mainPlayerSb.progress =
                                    (songs[nowPos].second * 1000 / playTime).toInt()
                            }
                        }
                    }
                }
            } catch (e: InterruptedException) {
                Log.d("SONG", "???????????? ???????????????. ${e.message}")
            }
        }
    }

    //ROOM_DB
    private fun inputDummyAlbums() {
        val songDB = SongDatabase.getInstance(this)!!
        val albums = songDB.albumDao().getAlbums()

        if (albums.isNotEmpty()) return

        songDB.albumDao().insert(
            Album(
                1,
                "BUTTER", "??????????????? (BTS)", R.drawable.img_album_exp
            )
        )

        songDB.albumDao().insert(
            Album(
                2,
                "IU 5th Album 'LILAC'", "????????? (IU)", R.drawable.img_album_exp2
            )
        )

        songDB.albumDao().insert(
            Album(
                3,
                "Our Love is greate", "????????? (Yerin Baek)", R.drawable.img_album_exp3
            )
        )

        songDB.albumDao().insert(
            Album(
                4,
                "We Ride", "??????????????????", R.drawable.img_album_exp4
            )
        )

        songDB.albumDao().insert(
            Album(
                5,
                "FLYING HIGH WITH U", "??????", R.drawable.img_album_exp5
            )
        )

        songDB.albumDao().insert(
            Album(
                6,
                "DEAR OHMYGIRL", "???????????? (OH MY GIRL)", R.drawable.img_album_exp6
            )
        )

    }

    private fun inputDummySongs() {
        Log.d("dummy", "hi")
        val songDB = SongDatabase.getInstance(this)!!
        val songs = songDB.songDao().getSongs()
        Log.d("songsDB", songs.toString())

        // ?????? ?????????????????? ??????????????? ?????????
        if (songs.isNotEmpty()) {
            Log.d("is not empty", "hi")
            return
        }

        // ?????? ?????????????????? ?????? ?????????
        songDB.songDao().insert(
            Song(
                "Butter",
                "??????????????? (BTS)",
                0,
                171,
                false,
                "music_butter",
                R.drawable.img_album_exp,
                false,
                1
            )
        )
        songDB.songDao().insert(
            Song(
                "?????????",
                "????????? (IU)",
                0,
                244,
                false,
                "music_lilac",
                R.drawable.img_album_exp2,
                false,
                2
            )
        )

        songDB.songDao().insert(
            Song(
                "Flu",
                "????????? (IU)",
                0, 188,
                false,
                "music_flu",
                R.drawable.img_album_exp2,
                false,
                2
            )
        )
        songDB.songDao().insert(
            Song(
                "Coin",
                "????????? (IU)",
                0,
                223,
                false,
                "music_coin",
                R.drawable.img_album_exp2,
                false,
                2
            )
        )
        songDB.songDao().insert(
            Song(
                "??? ?????? ???",
                "????????? (IU)",
                0,
                325,
                false,
                "music_hispringbye",
                R.drawable.img_album_exp2,
                false,
                2
            )
        )
        songDB.songDao().insert(
            Song(
                "Celebrity",
                "????????? (IU)",
                0,
                195,
                true,
                "music_celebrity",
                R.drawable.img_album_exp2,
                false,
                2
            )
        )
        songDB.songDao().insert(
            Song(
                "???????????? (Feat. DEAN)",
                "????????? (IU)",
                0,
                189,
                true,
                "music_troll",
                R.drawable.img_album_exp2,
                false,
                2
            )
        )

        songDB.songDao().insert(
            Song(
                "????????????",
                "????????? (Yerin Baek)",
                0,
                223,
                false,
                "music_seeyouagain",
                R.drawable.img_album_exp3,
                false,
                3
            )
        )

        songDB.songDao().insert(
            Song(
                "???????????? (We Ride)",
                "??????????????????",
                0,
                188,
                false,
                "music_weride",
                R.drawable.img_album_exp4,
                false,
                4
            )
        )

        songDB.songDao().insert(
            Song(
                "?????????",
                "??????",
                0,
                133,
                false,
                "music_shower",
                R.drawable.img_album_exp5,
                false,
                5
            )
        )

        songDB.songDao().insert(
            Song(
                "?????????",
                "???????????? (OH MY GIRL)",
                0,
                184,
                false,
                "music_whocomeswhoknows",
                R.drawable.img_album_exp6,
                false,
                6
            )
        )

        val _songs = songDB.songDao().getSongs()
        Log.d("DB", _songs.toString())


    }


}

