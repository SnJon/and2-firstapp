package ru.netology.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import ru.netology.dao.PostDao
import ru.netology.dto.Post

class PostRepositorySQLiteImpl(
    private val dao: PostDao
) : PostRepository {
    private var posts = emptyList<Post>()
    private val data = MutableLiveData(posts)

    init {
        posts = dao.getAll()
        data.value = posts
    }

    override fun getAll(): LiveData<List<Post>> = data

    override fun save(post: Post) {
        val id = post.id
        val saved = dao.save(post)
        posts = if (id == 0L) {
            listOf(saved) + posts
        } else {
            posts.map { post ->
                if (post.id != id) post else saved
            }
        }
        data.value = posts
    }

    override fun likeById(id: Long) {
        dao.likeById(id)
        posts = posts.map { post ->
            if (post.id != id) post else post.copy(
                likedByMe = !post.likedByMe,
                likes = if (post.likedByMe) post.likes - 1 else post.likes + 1
            )
        }
        data.value = posts
    }

    override fun shareById(id: Long) {
        dao.shareById(id)
        posts = posts.map { post ->
            if (post.id != id) {
                post
            } else {
                post.copy(sharedByMe = !post.sharedByMe, shares = post.shares + 1)
            }
        }
        data.value = posts
    }

    override fun removeById(id: Long) {
        dao.removeById(id)
        posts = posts.filter { it.id != id }
        data.value = posts
    }
}