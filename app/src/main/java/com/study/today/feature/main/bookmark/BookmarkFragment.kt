package com.study.today.feature.main.bookmark

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.study.today.databinding.FragmentBookmarkBinding
import com.study.today.feature.main.search.TourListAdapter
import com.study.today.model.Tour

class BookmarkFragment : Fragment() {

    private val viewModel by viewModels<BookmarkViewModel>()
    private var _binding: FragmentBookmarkBinding? =
        null // This property is only valid between onCreateView and onDestroyView.
    private val binding get() = _binding!!
    private val listAdapter = TourListAdapter(clickListener = { i, tour ->
        showDetail(tour)
    }) { i, tour, b ->
        viewModel.change(tour, b)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentBookmarkBinding.inflate(inflater, container, false)
        viewModel.loadBookMarks()
        viewModel.bookmarkList.observe(viewLifecycleOwner, { listAdapter.submitList(it) })
        viewModel.isLoading.observe(viewLifecycleOwner, { binding.progress.isVisible = it })
        viewModel.toastMsgResId.observe(viewLifecycleOwner, {
            Toast.makeText(
                requireContext(),
                getString(it),
                Toast.LENGTH_SHORT
            ).show()
        })
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.recyclerView.adapter = listAdapter
        super.onViewCreated(view, savedInstanceState)
    }

    private fun showDetail(tour: Tour) {
        // TODO: 상세페이지로 넘어가기
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}