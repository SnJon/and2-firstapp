package ru.netology.dto

data class Post(
    val id: Long,
    val author: String,
    val published: String,
    val content: String,
    val likeByMe: Boolean,
    val shareByMe: Boolean,
    val likes: Int,
    val share: Int,
    val views: Int
)


