package ru.netology.repository

import androidx.lifecycle.LiveData
import ru.netology.dto.Post

interface PostRepository {
    fun getAll(): LiveData<List<Post>>
    fun likeById(id: Long)
    fun shareById(id: Long)
    fun save(post: Post)
    fun removeById(id: Long)
    fun getById(id: Long) : Post?
}