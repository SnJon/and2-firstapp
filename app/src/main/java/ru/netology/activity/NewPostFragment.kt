package ru.netology.activity

import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import ru.netology.databinding.FragmentNewPostBinding
import ru.netology.dto.Post
import ru.netology.util.StringArg
import ru.netology.util.hideKeyboard
import ru.netology.viewmodel.PostViewModel

class NewPostFragment() : Fragment() {
    companion object {
        var Bundle.stringArg: String? by StringArg
        var Bundle.intentArg: String? by StringArg
    }

    private var _binding: FragmentNewPostBinding? = null
    private val binding: FragmentNewPostBinding
        get() = _binding!!

    private val viewModel: PostViewModel by viewModels(
        ownerProducer = ::requireParentFragment
    )

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentNewPostBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val changeText = arguments?.stringArg ?: ""
        binding.edit.setText(changeText)

        val intentText = arguments?.intentArg ?: ""
        binding.edit.setText(intentText)

        binding.ok.setOnClickListener {
            if (!TextUtils.isEmpty(binding.edit.text)) {
                viewModel.changeContent(binding.edit.text.toString())
                viewModel.save()
            }
            requireView().hideKeyboard()
            findNavController().navigateUp()
        }

        requireActivity().onBackPressedDispatcher.addCallback(this) {
            viewModel.selectedPost.value = Post.empty
            findNavController().navigateUp()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}