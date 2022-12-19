package ru.netology.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import ru.netology.databinding.ActivityMainBinding
import ru.netology.viewmodel.PostViewModel
import kotlin.math.floor

class MainActivity : AppCompatActivity() {
    private val viewModel: PostViewModel by viewModels()

    private fun numberDisplay(number: Int): String {
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        subscribe(binding)
        setupListeners(binding)
    }

    private fun subscribe(binding: ActivityMainBinding) {
        viewModel.data.observe(this) { post ->
            with(binding) {
                author.text = post.author
                published.text = post.published
                content.text = post.content
                likesCount.text = numberDisplay(post.likes)
                sharedCount.text = numberDisplay(post.share)
                viewsCount.text = numberDisplay(post.views)
                likes.setImageResource(if (post.likeByMe) ru.netology.R.drawable.ic_liked_24 else ru.netology.R.drawable.ic_like_24)
            }
        }
    }

    private fun setupListeners(binding: ActivityMainBinding) {
        binding.likes.setOnClickListener {
            viewModel.like()
        }
        binding.share.setOnClickListener {
            viewModel.share()
        }
    }
}
