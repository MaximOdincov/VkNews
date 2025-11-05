package com.example.vkapijob.data.mapper

import androidx.compose.ui.text.intl.Locale
import com.example.vkapijob.data.model.NewsResponseDto
import com.example.vkapijob.domain.Post
import com.example.vkapijob.domain.StatisticItem
import com.example.vkapijob.domain.StatisticType
import java.sql.Timestamp
import java.text.SimpleDateFormat
import java.util.Date
import javax.inject.Inject
import kotlin.math.absoluteValue

class NewsMapper @Inject constructor(){
    fun mapResponseToPosts(responseDto: NewsResponseDto): List<Post> {
        val result: MutableList<Post> = mutableListOf()

        val posts = responseDto.newsContent.posts
        val groups = responseDto.newsContent.groups

        for (post in posts) {
            val group = groups.find { it.id == post.sourceId.absoluteValue } ?: break
            if (post.views != null) {
                result.add(
                    Post(
                        id = post.id,
                        communityName = group.name,
                        publicationDate = mapTimestampToDate(post.date * 1000),
                        avatarUrl = group.imageUrl,
                        contentText = post.text,
                        contentImageUrl = post.attachments?.firstOrNull()?.photo?.photoUrls?.last()?.url,
                        statistics = listOf(
                            StatisticItem(type = StatisticType.LIKES, post.likes.count),
                            StatisticItem(type = StatisticType.COMMENTS, post.comments.count),
                            StatisticItem(type = StatisticType.VIEWS, post.views.count),
                            StatisticItem(type = StatisticType.SHARES, post.reposts.count)
                        ),
                        isLiked = post.likes.userLike > 0,
                        communityId = post.sourceId
                    )
                )
            }
        }
        return result
    }

    private fun mapTimestampToDate(timestamp: Long): String {
        val date = Date(timestamp)
        return SimpleDateFormat("dd MMMM yyyy, hh:mm", java.util.Locale.getDefault()).format(date)
    }
}