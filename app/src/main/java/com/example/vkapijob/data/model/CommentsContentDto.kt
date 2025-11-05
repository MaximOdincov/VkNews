package com.example.vkapijob.data.model

import com.google.gson.annotations.SerializedName

data class CommentsContentDto(
    @SerializedName("items") val comments: List<CommentDto>,
    @SerializedName("profiles") val profiles: List<ProfileDto>,
    @SerializedName("real_offset") val realOffset: Int? = null,
    @SerializedName("count") val count: Int
)
