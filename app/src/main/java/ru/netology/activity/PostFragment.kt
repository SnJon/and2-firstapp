package ru.netology.activity

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.activity.addCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import ru.netology.R
import ru.netology.activity.NewPostFragment.Companion.stringArg
import ru.netology.databinding.FragmentPostBinding
import ru.netology.dto.Post
import ru.netology.util.IdArg
import ru.netology.util.displayFormat
import ru.netology.util.setAllOnClickListener
import ru.netology.viewmodel.PostViewModel

class PostFragment : Fragment() {
    companion object {
        var Bundle.idArg: Long? by IdArg
    }

    private var _binding: FragmentPostBinding? = null
    private val binding: FragmentPostBinding
        get() = _binding!!

    private val viewModel: PostViewModel by viewModels(
        ownerProducer = ::requireParentFragment
    )

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPostBinding.inflate(
            inflater, container, false
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val postId = arguments?.idArg ?: -1
        viewModel.data.observe(viewLifecycleOwner) { posts ->
            val post = posts.find { it.id == postId } ?: return@observe
            with(binding) {
                if (post.video != null) {
                    videoGroup.visibility = View.VISIBLE
                } else {
                    videoGroup.visibility = View.GONE
                }

                binding.content.text = post.content
                author.text = post.author
                published.text = post.published
                likes.text = "${post.likes.displayFormat()}"
                share.text = "${post.shares.displayFormat()}"
                viewsCount.text = "${post.views.displayFormat()}"
                likes.isChecked = post.likedByMe


                likes.setOnClickListener {
                    viewModel.likeById(postId)
                }

                videoGroup.setAllOnClickListener {
                    val intent = Intent(Intent.ACTION_VIEW).apply {
                        data = Uri.parse(post.video)
                    }
                    context?.packageManager?.let { packageManager ->
                        intent.resolveActivity(packageManager)
                        startActivity(intent)
                    }
                }

                share.setOnClickListener {
                    val intent = Intent().apply {
                        action = Intent.ACTION_SEND
                        putExtra(Intent.EXTRA_TEXT, post.content)
                        type = "text/plain"
                    }

                    val shareIntent =
                        Intent.createChooser(intent, getString(R.string.chooser_share_post))
                    startActivity(shareIntent)
                    viewModel.shareById(postId)
                }

                requireActivity().onBackPressedDispatcher.addCallback() {
                    viewModel.selectedPost.value = Post.empty
                    findNavController().navigateUp()
                }

                menu.setOnClickListener {
                    PopupMenu(it.context, it).apply {
                        inflate(R.menu.options_post)
                        setOnMenuItemClickListener { item ->
                            when (item.itemId) {
                                R.id.remove -> {
                                    viewModel.removeById(post.id)
                                    viewModel.selectedPost.value = Post.empty
                                    findNavController().navigateUp()
                                    true
                                }
                                R.id.edit -> {
                                    viewModel.edit(post)
                                    findNavController().navigate(
                                        R.id.action_postFragment_to_newPostFragment,
                                        Bundle().apply { stringArg = post.content })
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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
