package ru.netology.activity

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import ru.netology.R
import ru.netology.activity.NewPostFragment.Companion.stringArg
import ru.netology.activity.PostFragment.Companion.idArg
import ru.netology.adapter.OnInteractionListener
import ru.netology.adapter.PostAdapter
import ru.netology.databinding.FragmentFeedBinding
import ru.netology.dto.Post
import ru.netology.viewmodel.PostViewModel


class FeedFragment : Fragment() {
    private var _binding: FragmentFeedBinding? = null
    private val binding: FragmentFeedBinding
        get() = _binding!!

    private val viewModel: PostViewModel by viewModels(
        ownerProducer = ::requireParentFragment
    )
    private val interaction = object : OnInteractionListener {

        override fun onLike(post: Post) {
            viewModel.likeById(post.id)
        }

        override fun onShare(post: Post) {
            val intent = Intent().apply {
                action = Intent.ACTION_SEND
                putExtra(Intent.EXTRA_TEXT, post.content)
                type = "text/plain"
            }

            val shareIntent = Intent.createChooser(intent, getString(R.string.chooser_share_post))
            startActivity(shareIntent)
            viewModel.shareById(post.id)
        }

        override fun onRemove(post: Post) {
            viewModel.removeById(post.id)
        }

        override fun onEdit(post: Post) {
            viewModel.edit(post)
            findNavController().navigate(
                R.id.action_feedFragment_to_newPostFragment,
                Bundle().apply { stringArg = post.content })
        }

        override fun onPlay(post: Post) {
            val intent = Intent(Intent.ACTION_VIEW).apply {
                data = Uri.parse(post.video)
            }
            context?.packageManager?.let { packageManager ->
                intent.resolveActivity(packageManager)
                startActivity(intent)
            }
        }

        override fun onOpen(post: Post) {
            viewModel.edit(post)
            findNavController().navigate(
                R.id.action_feedFragment_to_postFragment,
                Bundle().apply { idArg = post.id })
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFeedBinding.inflate(
            inflater,
            container,
            false
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        subscribe(binding)

        binding.fab.setOnClickListener {
            findNavController().navigate(R.id.action_feedFragment_to_newPostFragment)
        }
    }

    private fun subscribe(binding: FragmentFeedBinding) {
        val adapter = PostAdapter(interaction)
        binding.list.adapter = adapter
        viewModel.data.observe(viewLifecycleOwner) { posts ->
            adapter.submitList(posts)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
