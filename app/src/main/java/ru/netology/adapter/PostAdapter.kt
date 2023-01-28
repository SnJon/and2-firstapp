package ru.netology.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import ru.netology.R
import ru.netology.databinding.CardPostBinding
import ru.netology.dto.Post
import kotlin.math.floor

//функция представления данных
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

class PostAdapter(
    private val onInteractionListener: OnInteractionListener
) :
    ListAdapter<Post, PostViewHolder>(PostDiffCallBack()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        val binding = CardPostBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PostViewHolder(binding, onInteractionListener)
    }

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        val post = getItem(position)
        holder.bind(post)
    }
}

interface OnInteractionListener {
    fun onLike(post: Post) {}
    fun onShare(post: Post) {}
    fun onRemove(post: Post) {}
    fun onEdit(post: Post) {}
}

class PostViewHolder(
    private val binding: CardPostBinding,
    private val onInteractionListener: OnInteractionListener
) : RecyclerView.ViewHolder(binding.root) {
    fun bind(post: Post) {
        binding.apply {
            author.text = post.author
            published.text = post.published
            content.text = post.content
            likesCount.text = numberDisplay(post.likes)
            sharedCount.text = numberDisplay(post.share)
            viewsCount.text = numberDisplay(post.views)
            likes.setImageResource(if (post.likeByMe) R.drawable.ic_liked_24 else R.drawable.ic_like_24)
            likes.setOnClickListener {
                onInteractionListener.onLike(post)
            }
            share.setOnClickListener {
                onInteractionListener.onShare(post)
            }

            menu.setOnClickListener {
                PopupMenu(it.context, it).apply {
                    inflate(R.menu.options_post)
                    setOnMenuItemClickListener { item ->
                        when (item.itemId) {
                            R.id.remove -> {
                                onInteractionListener.onRemove(post)
                                true
                            }
                            else -> false
                        }
                    }
                }.show()
            }
        }
    }
}

class PostDiffCallBack : DiffUtil.ItemCallback<Post>() {
    override fun areItemsTheSame(oldItem: Post, newItem: Post): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Post, newItem: Post): Boolean {
        return oldItem == newItem
    }
}