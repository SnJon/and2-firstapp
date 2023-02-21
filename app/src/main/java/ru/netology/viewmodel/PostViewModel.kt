package ru.netology.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ru.netology.dto.Post
import ru.netology.repository.PostRepository
import ru.netology.repository.PostRepositoryFileImpl
import ru.netology.repository.PostRepositoryInMemoryImpl

class PostViewModel(application: Application) : AndroidViewModel(application) {
//    private val repository: PostRepository = PostRepositoryInMemoryImpl()
    private val repository: PostRepository = PostRepositoryFileImpl(application)
    val data = repository.getAll()
    val edited = MutableLiveData(Post.empty)

    fun save() {
        edited.value?.let {
            repository.save(it)
        }
        edited.value = Post.empty
    }

    fun changeContent(content: String) {
        edited.value?.let {
            val text = content.trim()
            if (it.content == text) {
                return
            }
            edited.value = it.copy(content = text)
        }
    }

    fun edit(post: Post) {
        edited.value = post
    }

    fun likeById(id: Long) = repository.likeById(id)
    fun shareById(id: Long) = repository.shareById(id)
    fun removeById(id: Long) = repository.removeById(id)
}