package com.example.vkapijob.data.network

import com.example.vkapijob.data.model.CommentsResponseDto
import com.example.vkapijob.data.model.LikesCountResponseDto
import com.example.vkapijob.data.model.NewsResponseDto
import com.vk.id.AccessToken
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {

    @GET("newsfeed.get?v=5.199")
    suspend fun loadPosts(
        @Query("access_token") token: String,
        @Query("filters") filters: String = "post"
    ): NewsResponseDto

    @GET("newsfeed.get?v=5.199")
    suspend fun loadPosts(
        @Query("access_token") token: String,
        @Query("filters") filters: String = "post",
        @Query("start_from") startFrom: String
    ): NewsResponseDto

    @GET("likes.add?v=5.199")
    suspend fun addLike(
        @Query("access_token") token: String,
        @Query("owner_id") ownerId: Long,
        @Query("item_id") postId: Long,
        @Query("type") type: String  = "post"
    ): LikesCountResponseDto

    @GET("likes.delete?v=5.199")
    suspend fun deleteLike(
        @Query("access_token") token: String,
        @Query("owner_id") ownerId: Long,
        @Query("item_id") postId: Long,
        @Query("type") type: String  = "post"
    ): LikesCountResponseDto


    @GET("wall.getComments?v=5.199")
    suspend fun loadComments(
        @Query("access_token") token: String,
        @Query("owner_id") ownerId: Long,
        @Query("post_id") postId: Long,
        @Query("count") count: Int = 30,
        @Query("offset") offset: Int = 0,
        @Query("extended") extended: Int = 1,
        @Query("fields") fields: String = "photo_50"
    ): CommentsResponseDto

}