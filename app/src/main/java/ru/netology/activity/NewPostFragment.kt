package ru.netology.activity

import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import ru.netology.databinding.FragmentNewPostBinding
import ru.netology.util.hideKeyboard
import ru.netology.viewmodel.PostViewModel

class NewPostFragment() : Fragment() {

    private val viewModel: PostViewModel by viewModels(
        ownerProducer = ::requireParentFragment
    )

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding: FragmentNewPostBinding = FragmentNewPostBinding.inflate(
            inflater,
            container,
            false
        )

        viewModel.selectedPost.observe(viewLifecycleOwner) { post ->
            binding.edit.setText(post.content)
        }

        binding.ok.setOnClickListener {
            if (!TextUtils.isEmpty(binding.edit.text)) {
                viewModel.changeContent(binding.edit.text.toString())
                viewModel.save()
            }
            requireView().hideKeyboard()
            findNavController().navigateUp()
        }
        return binding.root
    }

    companion object {
        const val ARG_CONTENT = "ARG_CONTENT"
    }
}