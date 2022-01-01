package com.example.flo

import androidx.room.*


@Dao
interface SongDao {
    @Insert
    fun insert(song: Song)

    @Update
    fun update(song: Song)

    @Delete
    fun delete(song: Song)

    @Query("SELECT * FROM SongTable") // 테이블의 모든 값을 가져와라
    fun getSongs(): List<Song>

    @Query("SELECT * FROM SongTable WHERE id = :id")
    fun getSong(id: Int): Song

    @Query("SELECT * FROM SongTable WHERE albumIdx = :albumIdx")
    fun getSongsInAlbum(albumIdx: Int): List<Song>

    @Query("DELETE FROM LikeSongTable WHERE userId = :userID")
    fun updateDisLikeAll(userID: Int)

    @Insert
    fun likeSong(likedSongs: LikedSongs)

    @Query("SELECT id FROM LikeSongTable WHERE userId = :userID AND songId = :songID")
    fun isLikeSong(userID: Int, songID: Int): Int?

    @Query("DELETE FROM LikeSongTable WHERE userId = :userID AND songId = :songID")
    fun disLikeAlbum(userID: Int, songID: Int): Int?

    @Query("SELECT ST.* FROM LikeSongTable as LT LEFT JOIN SongTable as ST ON songId = ST.id WHERE LT.userId = :userId")
    fun getLikedSongs(userId: Int): List<Song>

}