package com.example.flo

import android.annotation.SuppressLint
import android.content.Intent
import android.media.MediaPlayer
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.widget.SeekBar
import androidx.appcompat.app.AppCompatActivity
import com.example.flo.databinding.ActivityMainBinding
import java.lang.Thread.sleep
import java.util.*
import kotlin.collections.ArrayList

class MainActivity : AppCompatActivity(), OnAlbumClickListener {

    lateinit var binding: ActivityMainBinding

    private var mediaPlayer: MediaPlayer? = null
    lateinit var timer: MainActivity.Timer

    private var songs = ArrayList<Song>()
    private var nowPos = 0
    private lateinit var songDB: SongDatabase

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        Log.d("oncreate", "main")

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
//                Log.d("seekbar", "1")
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
//                Log.d("seekbar", "2")
                songs[nowPos].second = seekBar!!.progress * songs[nowPos].playTime / 1000
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
//                Log.d("seekbar", "3")
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

    // interface 구현
    override fun onAlbumClick(album: Album) {

        timer.isPlaying = false
        timer.interrupt() // 스레드를 해제함
        Log.d("thread 해제", "앨범클릭")
        mediaPlayer?.release() // 미디어플레이어가 가지고 있던 리소스를 해방
        mediaPlayer = null // 미디어플레이어 해제
        nowPos = 0

        songDB = SongDatabase.getInstance(this)!!
        songs.clear()
        songs.addAll(songDB.songDao().getSongsInAlbum(album.id))
        Log.d("album song : ", songs.toString())


        startTimer()
        setPlayer(songs[nowPos])


    }


    // 액티비티가 사용자에게 보여지기 직전에 호출된다.
    // onResume() 는 액티비티가 시작되고 사용자와 상호작용하기 직전에 호출된다.
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
//        songs[nowPos].isPlaying = false
//        setPlayerStatus(false)
        timer.isPlaying = false

        timer.interrupt() // 스레드를 해제함

        mediaPlayer?.release() // 미디어플레이어가 가지고 있던 리소스를 해방
        mediaPlayer = null // 미디어플레이어 해제

        Log.d("onpause : main second", songs[nowPos].second.toString())
//
        Log.d("onpause : main second", songs[nowPos].second.toString())
        songDB = SongDatabase.getInstance(this)!!
        songDB.songDao().update(songs[nowPos])

        val sharedPreferences = getSharedPreferences("song", MODE_PRIVATE)
        val editor = sharedPreferences.edit()

        editor.putInt("songId", songs[nowPos].id)
        editor.apply()
    }

    override fun onDestroy() { // 화면이 꺼질때 실행되는 함수
        super.onDestroy()

        Log.d("onDestroy", "main")

//        timer.interrupt() // 스레드를 해제함
//
//        mediaPlayer?.release() // 미디어플레이어가 가지고 있던 리소스를 해방
//        mediaPlayer = null // 미디어플레이어 해제
    }

    private fun initPlayList() {
        songDB = SongDatabase.getInstance(this)!!
        Log.d("ㅉㅈ",songDB.songDao().getSongs().toString())
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
        songs[nowPos].second = songDB.songDao().getSong(songId).second

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

        timer.interrupt()
        startTimer()

        mediaPlayer?.release() // 미디어플레이어가 가지고 있던 리소스를 해방
        mediaPlayer = null // 미디어플레이어 해제

        // 다음 곡의 second = 0, isplaying 은 true 값으로 default 설정
        songs[nowPos].second = 0
        songs[nowPos].isPlaying = true

        setPlayer(songs[nowPos])
    }


    private fun setPlayerStatus(isPlaying: Boolean) {
        timer.isPlaying = isPlaying
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
                            timer.interrupt()
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
                Log.d("SONG", "쓰레드가 죽었습니다. ${e.message}")
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
                "BUTTER", "방탄소년단 (BTS)", R.drawable.img_album_exp
            )
        )

        songDB.albumDao().insert(
            Album(
                2,
                "IU 5th Album 'LILAC'", "아이유 (IU)", R.drawable.img_album_exp2
            )
        )

        songDB.albumDao().insert(
            Album(
                3,
                "Our Love is greate", "백예린 (Yerin Baek)", R.drawable.img_album_exp3
            )
        )

        songDB.albumDao().insert(
            Album(
                4,
                "We Ride", "브레이브걸스", R.drawable.img_album_exp4
            )
        )

        songDB.albumDao().insert(
            Album(
                5,
                "FLYING HIGH WITH U", "빈첸", R.drawable.img_album_exp5
            )
        )

        songDB.albumDao().insert(
            Album(
                6,
                "DEAR OHMYGIRL", "오마이걸 (OH MY GIRL)", R.drawable.img_album_exp6
            )
        )

    }

    private fun inputDummySongs() {
        Log.d("dummy", "hi")
        val songDB = SongDatabase.getInstance(this)!!
        val songs = songDB.songDao().getSongs()
        Log.d("songsDB", songs.toString())

        // 이미 더미데이터가 들어있다면 끝내기
        if (songs.isNotEmpty()) {
            Log.d("is not empty", "hi")
            return
        }

        // 처음 더미데이터를 넣는 거라면
        songDB.songDao().insert(
            Song(
                "Butter",
                "방탄소년단 (BTS)",
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
                "라일락",
                "아이유 (IU)",
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
                "아이유 (IU)",
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
                "아이유 (IU)",
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
                "봄 안녕 봄",
                "아이유 (IU)",
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
                "아이유 (IU)",
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
                "돌림노래 (Feat. DEAN)",
                "아이유 (IU)",
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
                "지켜줄게",
                "백예린 (Yerin Baek)",
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
                "운전만해 (We Ride)",
                "브레이브걸스",
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
                "소나기",
                "빈첸",
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
                "초대장",
                "오마이걸 (OH MY GIRL)",
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

