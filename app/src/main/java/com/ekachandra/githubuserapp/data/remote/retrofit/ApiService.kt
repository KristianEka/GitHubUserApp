package com.ekachandra.githubuserapp.data.remote.retrofit

import com.ekachandra.githubuserapp.data.remote.response.DetailUserResponse
import com.ekachandra.githubuserapp.data.remote.response.GithubResponse
import com.ekachandra.githubuserapp.data.remote.response.ItemsItem
import retrofit2.Call
import retrofit2.http.*

interface ApiService {

    @GET("search/users")
    fun searchUsers(@Query("q") query: String): Call<GithubResponse>

    @GET("users/{username}/followers")
    fun getFollowers(@Path("username") username: String): Call<List<ItemsItem>>

    @GET("users/{username}/following")
    fun getFollowing(@Path("username") username: String): Call<List<ItemsItem>>

    @GET("users/{username}")
    fun getUsername(@Path("username") username: String): Call<DetailUserResponse>

}