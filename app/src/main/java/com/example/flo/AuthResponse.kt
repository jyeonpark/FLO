package com.example.flo

import com.google.gson.annotations.SerializedName

// SerializedName 으로 직접 key 값 정해줄 수 있음
data class Auth(@SerializedName("userIdx")val userIdx: Int, @SerializedName("jwt") val jwt: String)
data class AuthResponse(
    @SerializedName("isSuccess") val isSuccess: Boolean,
    @SerializedName("code")val code:Int,
    @SerializedName("message")val message : String,
    @SerializedName("result")val result:Auth?
    )
