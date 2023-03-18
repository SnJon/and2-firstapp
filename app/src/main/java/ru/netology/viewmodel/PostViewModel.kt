package ru.netology.viewmodel

import android.app.Application
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import ru.netology.db.AppDb
import ru.netology.dto.Post
import ru.netology.repository.*
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class PostViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: PostRepository =
        PostRepositoryRoomImpl(AppDb.getInstance(context = application).postDao())

    //    private val repository: PostRepository = PostRepositoryInMemoryImpl()
//    private val repository: PostRepository = PostRepositoryFileImpl(application)
    val data = repository.getAll()
    val selectedPost = MutableLiveData(Post.empty)

    fun save() {
        selectedPost.value?.let {
            repository.save(it)
        }
        selectedPost.value = Post.empty
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun changeContent(content: String) {
        selectedPost.value?.let {
            val text = content.trim()
            if (it.content == text) {
                return
            }
            val time = LocalDateTime.now().format(
                DateTimeFormatter.ofPattern("k:mm")
            )
            selectedPost.value = it.copy(content = text, author = "Me", published = time)
        }
    }

    fun edit(post: Post) {
        selectedPost.value = post
    }

    fun likeById(id: Long) = repository.likeById(id)
    fun shareById(id: Long) = repository.shareById(id)
    fun removeById(id: Long) = repository.removeById(id)
}