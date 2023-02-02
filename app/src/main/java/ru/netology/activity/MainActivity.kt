package ru.netology.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import ru.netology.R
import ru.netology.adapter.OnInteractionListener
import ru.netology.adapter.PostAdapter
import ru.netology.databinding.ActivityMainBinding
import ru.netology.dto.Post
import ru.netology.util.hideKeyboard
import ru.netology.viewmodel.PostViewModel

class MainActivity : AppCompatActivity() {
    private val viewModel: PostViewModel by viewModels()
    private val interaction = object : OnInteractionListener {
        override fun onLike(post: Post) {
            viewModel.likeById(post.id)
        }

        override fun onShare(post: Post) {
            viewModel.shareById(post.id)
        }

        override fun onRemove(post: Post) {
            viewModel.removeById(post.id)
        }

        override fun onEdit(post: Post) {
            viewModel.edit(post)
            activityMainBinding?.cancelEditingView?.cancelEditingView?.visibility = View.VISIBLE

        }
    }

    private var activityMainBinding: ActivityMainBinding? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        activityMainBinding = binding
        setContentView(binding.root)
        subscribe(binding)

        binding.editingView.save.setOnClickListener {
            with(binding.editingView.content) {
                if (text.isNullOrBlank()) {
                    Toast.makeText(
                        this@MainActivity,
                        context.getString(R.string.error_empty_content),
                        Toast.LENGTH_SHORT
                    ).show()
                    return@setOnClickListener
                }
                viewModel.changeContent(text.toString())
                viewModel.save()

                setText("")
                clearFocus()
                hideKeyboard()

                binding.cancelEditingView.cancelEditingView.visibility = View.GONE
            }
        }

        binding.cancelEditingView.cancel.setOnClickListener {
            with(binding.editingView.content) {
                setText("")
                clearFocus()
                hideKeyboard()
                viewModel.save()
                binding.cancelEditingView.cancelEditingView.visibility = View.GONE
            }
        }
    }

    private fun subscribe(binding: ActivityMainBinding) {
        val adapter = PostAdapter(interaction)
        binding.list.adapter = adapter
        viewModel.data.observe(this) { posts ->
            adapter.submitList(posts)
        }

        viewModel.edited.observe(this) { post ->
            if (post.id == 0L) {
                return@observe
            }
            with(binding.editingView.content) {
                requestFocus()
                setText(post.content)
            }
        }
    }
}
