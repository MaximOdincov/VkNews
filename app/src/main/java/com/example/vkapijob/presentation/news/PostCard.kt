package com.example.vkapijob.presentation.news

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.MoreVert
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.example.vkapijob.R
import com.example.vkapijob.domain.Post
import com.example.vkapijob.domain.StatisticItem
import com.example.vkapijob.domain.StatisticType

@Composable
fun Post(modifier: Modifier = Modifier, post: Post,
         onLikeClickListener: (StatisticItem)-> Unit,
         onShareClickListener: (StatisticItem)-> Unit,
         onCommentClickListener: (StatisticItem)-> Unit,
         onViewsClickListener: (StatisticItem)-> Unit){
    Card(modifier = modifier.padding(8.dp))
    {
        Column(
            modifier = Modifier.fillMaxWidth().padding(10.dp)
        ) {
            UpperRow(post.communityName, post.publicationDate, post.avatarUrl)
            Spacer(modifier = Modifier.height(5.dp))
            Text(post.contentText, color = MaterialTheme.colorScheme.onPrimary)
            Spacer(modifier = Modifier.height(5.dp))
            AsyncImage(model = post.contentImageUrl, contentDescription = "mainPhoto", modifier = Modifier.fillMaxWidth().wrapContentHeight(), contentScale = ContentScale.FillWidth)
            Spacer(modifier = Modifier.height(5.dp))
            BottomRow(post, onLikeClickListener, onCommentClickListener, onShareClickListener, onViewsClickListener)
        }
    }
}

@Composable
fun UpperRow(nameOfTheGroup: String, time: String, imageUrl: String){
    Row(modifier = Modifier.fillMaxWidth().padding(5.dp), verticalAlignment = Alignment.CenterVertically){
        AsyncImage(model = imageUrl,
            modifier = Modifier.size(50.dp).clip(CircleShape),
            contentDescription = "image_of_the_group")
        Column(modifier = Modifier.weight(1f).padding(10.dp)){
            Text(nameOfTheGroup, color = MaterialTheme.colorScheme.onPrimary)
            Spacer(Modifier.height(2.dp))
            Text(time, color = MaterialTheme.colorScheme.onSecondary)
        }
        Icon(imageVector = Icons.Rounded.MoreVert, contentDescription = "three_dots_image", modifier = Modifier.padding(10.dp), tint = MaterialTheme.colorScheme.onSecondary)
    }
}

@Composable
fun BottomRow(post: Post, onLikeClickListener: (StatisticItem) -> Unit, onCommentClickListener: (StatisticItem) -> Unit, onShareClickListener: (StatisticItem) -> Unit, onViewsClickListener: (StatisticItem) -> Unit){
    Row(){
        val views = post.statistics.getItemByType(StatisticType.VIEWS)
        val shares = post.statistics.getItemByType(StatisticType.SHARES)
        val comments = post.statistics.getItemByType(StatisticType.COMMENTS)
        val likes = post.statistics.getItemByType(StatisticType.LIKES)
        Box(modifier = Modifier.weight(1f)){
            IconText(post.statistics.getItemByType(StatisticType.VIEWS).count.toString(), R.drawable.viewer,
                { onViewsClickListener(views) })
        }
        IconText(formatStatisticCount(shares.count), R.drawable.share, {onShareClickListener(shares)})
        IconText(formatStatisticCount(comments.count), R.drawable.comment, {onCommentClickListener(comments)})
        val icon = if(post.isLiked) R.drawable.heart_red
        else R.drawable.heart
        IconText(formatStatisticCount(likes.count), icon, {onLikeClickListener(likes)})
    }
}

private fun formatStatisticCount(count: Int): String{
    return if(count>=100_000){
        String.format("%sK", count/1000)
    } else if(count>1000){
        String.format("%.1f", count/1000f)
    }
    else{
        count.toString()
    }
}

private fun List<StatisticItem>.getItemByType(type: StatisticType): StatisticItem{
    return this.find{it.type == type} ?: throw IllegalStateException()
}

@Composable
fun IconText(text: String, icon: Int, onItemClickListener: () -> Unit){
    Row(verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.clickable{
            onItemClickListener()
        }){
        Icon(painter=painterResource(icon), contentDescription = "", modifier = Modifier.size(20.dp), tint = MaterialTheme.colorScheme.onSecondary)
        Spacer(modifier = Modifier.width(5.dp))
        Text(text, color = MaterialTheme.colorScheme.onSecondary)
        Spacer(modifier = Modifier.width(10.dp))
    }
}
