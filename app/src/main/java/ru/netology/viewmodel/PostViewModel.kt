package ru.netology.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import ru.netology.dto.Post
import ru.netology.repository.PostRepository
import ru.netology.repository.PostRepositoryFileImpl
import ru.netology.repository.PostRepositoryInMemoryImpl

class PostViewModel(application: Application) : AndroidViewModel(application) {
        private val repository: PostRepository = PostRepositoryInMemoryImpl()
//    private val repository: PostRepository = PostRepositoryFileImpl(application)
    val data = repository.getAll()
    val selectedPost = MutableLiveData(Post.empty)

    fun save() {
        selectedPost.value?.let {
            repository.save(it)
        }
        selectedPost.value = Post.empty
    }

    fun changeContent(content: String) {
        selectedPost.value?.let {
            val text = content.trim()
            if (it.content == text) {
                return
            }
            selectedPost.value = it.copy(content = text)
        }
    }

    fun edit(post: Post) {
        selectedPost.value = post
    }

    fun likeById(id: Long) = repository.likeById(id)
    fun shareById(id: Long) = repository.shareById(id)
    fun removeById(id: Long) = repository.removeById(id)
}