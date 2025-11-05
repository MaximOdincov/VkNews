package com.example.vkapijob.domain


data class Post(
    val id: Long,
    val communityName: String,
    val communityId: Long,
    val publicationDate: String,
    val avatarUrl: String,
    val contentText: String,
    val contentImageUrl: String?,
    val statistics: List<StatisticItem>,
    val isLiked: Boolean
)
