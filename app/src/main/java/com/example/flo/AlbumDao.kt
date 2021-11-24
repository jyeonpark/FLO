package com.example.flo

import androidx.room.*


@Dao
interface AlbumDao {
    @Insert
    fun insert(album: Album)

    @Update
    fun update(album: Album)

    @Delete
    fun delete(album: Album)

    @Query("SELECT * FROM AlbumTable") // 테이블의 모든 값을 가져와라
    fun getAlbums(): List<Album>

    @Query("SELECT * FROM AlbumTable WHERE id = :id")
    fun getAlbum(id: Int): Album

    @Insert
    fun likeAlbum(like: Like)

    @Query("SELECT id FROM LikeTable WHERE userId = :userID AND albumId = :albumID")
    fun isLikeAlbum(userID: Int, albumID: Int): Int?

    @Query("DELETE FROM LikeTable WHERE userId = :userID AND albumId = :albumID")
    fun disLikeAlbum(userID: Int, albumID: Int): Int?

    @Query("SELECT AT.* FROM LikeTable as LT LEFT JOIN AlbumTable as AT ON albumId = AT.id WHERE LT.userId = :userId")
    fun getLikedAlbums(userId: Int): List<Album>
}