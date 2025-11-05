package com.example.vkapijob.data.model

import com.google.gson.annotations.SerializedName

data class PostDto(
    @SerializedName("id") val id: Long,
    @SerializedName("text") val text: String,
    @SerializedName("date") val date: Long,
    @SerializedName("likes") val likes: LikeDto,
    @SerializedName("comments") val comments: CommentsDto,
    @SerializedName("views") val views: ViewsDto?,
    @SerializedName("reposts") val reposts: RepostDto,
    @SerializedName("attachments") val attachments: List<AttachmentDto>?,
    @SerializedName("source_id") val sourceId: Long


    )