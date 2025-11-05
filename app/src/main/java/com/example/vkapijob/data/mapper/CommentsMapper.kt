package com.example.vkapijob.data.mapper

import com.example.vkapijob.data.model.CommentsResponseDto
import com.example.vkapijob.domain.PostComment
import java.text.SimpleDateFormat
import java.util.Date
import javax.inject.Inject

class CommentsMapper @Inject constructor(){
    fun mapResponseToComments(response: CommentsResponseDto): List<PostComment>{
        val result = mutableListOf<PostComment>()
        val content = response.response
        val comments = content.comments
        val profiles = content.profiles

        for(comment in comments){
            val authorOfComment = profiles.find{it.id == comment.fromId} ?: continue
            val photo: String = authorOfComment.photo50 ?: continue
            result.add(
                PostComment(
                    id = comment.id,
                    author = "${authorOfComment.firstName} + ${authorOfComment.lastName}",
                    authorAvatarUrl = photo,
                    commentText = comment.text,
                    publicationDate = mapTimestampToDate(comment.date)
                )
            )
        }
        return result
    }

    private fun mapTimestampToDate(timestamp: Long): String {
        val date = Date(timestamp)
        return SimpleDateFormat("dd MMMM yyyy, hh:mm", java.util.Locale.getDefault()).format(date)
    }
}