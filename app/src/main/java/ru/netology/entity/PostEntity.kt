package ru.netology.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import ru.netology.dto.Post

@Entity
data class PostEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long,
    val author: String,
    val published: String,
    val content: String,
    val video: String? = null,
    val likedByMe: Boolean,
    val sharedByMe: Boolean,
    val likes: Int = 0,
    val shares: Int = 0,
    val views: Int = 0,
) {

    fun toDto() = Post(
        id,
        author,
        published,
        content,
        video,
        likedByMe,
        sharedByMe,
        likes,
        shares,
        views
    )

    companion object {
        fun fromDto(dto: Post) =
            PostEntity(
                dto.id,
                dto.author,
                dto.published,
                dto.content,
                dto.video,
                dto.likedByMe,
                dto.sharedByMe,
                dto.likes,
                dto.shares,
                dto.views
            )
    }
}