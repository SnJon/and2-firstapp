package ru.netology

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import ru.netology.databinding.ActivityMainBinding
import ru.netology.dto.Post
import kotlin.math.floor

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        fun numberDisplay(number: Int): String {
            return when {
                ((number in 1000..9999) && (((number / 100) % 10) == 0)) -> (number / 1000).toString() + "K"
                (number in 1000..9999 && (((number / 100) % 10) != 0)) -> ((floor((number / 1000.0).toFloat() * 10.0)) / 10.0).toString() + "K"
                (number in 10000..999999) -> (number / 1000).toString() + "K"
                ((number > 999_999) && (((number / 100_000) % 10) == 0)) -> (number / 1_000_000).toString() + "M"
                ((number > 999_999) && (((number / 100_000) % 10) != 0)) -> ((floor(
                    (
                            number / 1_000_000.0).toFloat() * 10.0
                )) / 10.0).toString() + "M"
                else -> number.toString()
            }
        }

        val post = Post(
            id = 1,
            author = "Нетология. Университет интернет-профессий будущего",
            published = "10 декабря в 21:50",
            content = "Привет, это новая Нетология! Когда-то Нетология начиналась с интенсивов по онлайн-маркетингу. Затем появились курсы по дизайну, разработке, аналитике и управлению. Мы растём сами и помогаем расти студентам: от новичков до уверенных профессионалов. Но самое важное остаётся с нами: мы верим, что в каждом уже есть сила, которая заставляет хотеть больше, целиться выше, бежать быстрее. Наша миссия — помочь встать на путь роста и начать цепочку перемен → http://netolo.gy/fyb",
            likeByMe = false,
            shareByMe = false,
            likes = 1_099_999,
            share = 1_999,
            views = 2_300_000
        )

        with(binding) {
            author.text = post.author
            published.text = post.published
            content.text = post.content
            likesCount.text = numberDisplay(post.likes)
            sharedCount.text = numberDisplay(post.share)
            viewsCount.text = numberDisplay(post.views)

            if (post.likeByMe) {
                likes.setImageResource(R.drawable.ic_liked_24)
            }

            likes.setOnClickListener {
                post.likeByMe = !post.likeByMe
                if (post.likeByMe) post.likes++ else post.likes--
                likes.setImageResource(if (post.likeByMe) R.drawable.ic_liked_24 else R.drawable.ic_like_24)
                likesCount.text = numberDisplay(post.likes)
                viewsCount.text = numberDisplay(post.views)
            }

            share.setOnClickListener {
                post.shareByMe = !post.shareByMe
                if (post.shareByMe) post.share++ else post.share--
                sharedCount.text = numberDisplay(post.share)
            }
        }
    }
}