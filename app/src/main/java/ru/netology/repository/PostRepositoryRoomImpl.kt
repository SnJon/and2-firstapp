package ru.netology.repository

import androidx.lifecycle.Transformations
import ru.netology.dao.PostDao
import ru.netology.dto.Post
import ru.netology.entity.PostEntity

class PostRepositoryRoomImpl(
    private val dao: PostDao,
) : PostRepository {
    override fun getAll() = Transformations.map(dao.getAll()) { list ->
        list.map {
            Post(
                it.id,
                it.author,
                it.published,
                it.content,
                it.video,
                it.likedByMe,
                it.sharedByMe,
                it.likes,
                it.shares,
                it.views
            )
        }
    }

    override fun likeById(id: Long) {
        dao.likeById(id)
    }

    override fun shareById(id: Long) {
        dao.shareById(id)
    }

    override fun save(post: Post) {
        dao.save(PostEntity.fromDto(post))
    }

    override fun removeById(id: Long) {
        dao.removeById(id)
    }
}