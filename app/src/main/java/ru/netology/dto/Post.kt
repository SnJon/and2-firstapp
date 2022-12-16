package ru.netology.dto

data class Post(
    val id: Int,
    val author: String,
    val published: String,
    val content: String,
    var likeByMe: Boolean,
    var likes: Int,
    var share: Int,
    var views: Int
)


