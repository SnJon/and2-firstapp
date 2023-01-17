package ru.netology.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import ru.netology.dto.Post

class PostRepositoryInMemoryImpl : PostRepository {
    private var post = Post(
        id = 1,
        author = "Нетология, Университет интернет-профессий будущего",
        content = "Привет, это новая Нетология! Когда-то Нетология начиналась с интенсивов по онлайн-маркетингу. Затем появились курсы по дизайну, разработке, аналитике и управлению. Мы растём сами и помогаем расти студентам: от новичков до уверенных профессионалов. Но самое важное остаётся с нами: мы верим, что в каждом уже есть сила, которая заставляет хотеть больше, целиться выше, бежать быстрее. Наша миссия — помочь встать на путь роста и начать цепочку перемен → http://netolo.gy/fyb",
        published = "23 декабря в 00:20",
        likeByMe = false,
        shareByMe = false,
        likes = 1_099_999,
        share = 990,
        views = 2_300_000
    )

    private val data = MutableLiveData(post)

    override fun get(): LiveData<Post> = data
    override fun like() {
        post =
            if (!post.likeByMe) post.copy(
                likeByMe = !post.likeByMe,
                likes = post.likes + 1
            ) else post.copy(likeByMe = !post.likeByMe, likes = post.likes - 1)
        data.value = post
    }

    override fun share() {
        post = post.copy(shareByMe = !post.shareByMe, share = post.share + 1)
        data.value = post
    }
}