package com.study.today.feature.main.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.OnScrollListener
import com.study.today.databinding.FragmentSearchBinding

class SearchFragment : Fragment() {

    private lateinit var viewModel: SearchViewModel
    private var _binding: FragmentSearchBinding? =
        null // This property is only valid between onCreateView and onDestroyView.
    private val binding get() = _binding!!
    private val listAdapter = TourListAdapter()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        binding.search.setOnClickListener {
            viewModel.search(binding.word.text.toString())
        }
        binding.search.setOnLongClickListener {
            viewModel.search(37.56762539175941, 126.98092840228584)
            true
        }
        binding.recyclerView.adapter = listAdapter
        binding.recyclerView.addOnScrollListener(object : OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val lm = binding.recyclerView.layoutManager as LinearLayoutManager
                if (lm.itemCount <= lm.findLastCompletelyVisibleItemPosition() + 4) {
                    viewModel.searchNext(binding.word.text.toString())
                }

            }
        })
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(requireActivity()).get(SearchViewModel::class.java)
        viewModel.searchResult.observe(viewLifecycleOwner, { listAdapter.submitList(it) })
//        viewModel.resultText.observe(viewLifecycleOwner, { binding.result.text = it })
        viewModel.isLoading.observe(viewLifecycleOwner, { binding.progress.isVisible = it })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}