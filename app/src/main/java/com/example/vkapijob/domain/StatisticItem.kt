package com.example.vkapijob.domain

data class StatisticItem(
    val type: StatisticType,
    val count: Int = 0,

)

enum class StatisticType{
    VIEWS, LIKES, SHARES, COMMENTS
}