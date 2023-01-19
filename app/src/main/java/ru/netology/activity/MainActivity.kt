package ru.netology.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import ru.netology.adapter.PostAdapter
import ru.netology.databinding.ActivityMainBinding
import ru.netology.viewmodel.PostViewModel

class MainActivity : AppCompatActivity() {
    private val viewModel: PostViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        subscribe(binding)
    }

    private fun subscribe(binding: ActivityMainBinding) {
        val adapter = PostAdapter({
            viewModel.likeById(it.id)
        },
            {
                viewModel.shareById(it.id)
            })
        binding.list.adapter = adapter
        viewModel.data.observe(this) { posts ->
            adapter.submitList(posts)
        }
    }
}
