package com.ekachandra.githubuserapp.data.remote.response

import com.google.gson.annotations.SerializedName

data class DetailUserResponse(

    @field:SerializedName("followers") val followers: Int,

    @field:SerializedName("following") val following: Int,

    @field:SerializedName("name") val name: String,

    @field:SerializedName("login") val login: String,

    @field:SerializedName("avatar_url") val avatarUrl: String,
)
