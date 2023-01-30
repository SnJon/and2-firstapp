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
) {
    companion object {
        val empty = Post(
            id = 0,
            content = "",
            author = "",
            likeByMe = false,
            shareByMe = false,
            published = "",
            likes = 0,
            share = 0,
            views = 0
        )
    }
}


