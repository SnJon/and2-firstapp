package ru.netology.dto

data class Post(
    val id: Long,
    val author: String,
    val published: String,
    val content: String,
    val video: String? = null,
    val likedByMe: Boolean,
    val sharedByMe: Boolean,
    val likes: Int,
    val shares: Int,
    val views: Int
) {
    companion object {
        val empty = Post(
            id = 0,
            content = "",
            author = "",
            likedByMe = false,
            sharedByMe = false,
            published = "",
            likes = 0,
            shares = 0,
            views = 0
        )
    }
}


