package com.example.vkapijob.domain

import com.example.vkapijob.R

data class PostComment(
    val id: Long,
    val author: String,
    val authorAvatarUrl: String,
    val commentText: String,
    val publicationDate: String
)