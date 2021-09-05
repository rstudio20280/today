package com.study.today.feature.main.search

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.SearchView
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.OnScrollListener
import com.study.today.databinding.FragmentSearchBinding
import com.study.today.feature.cos.areas.SelectSec
import com.study.today.model.Tour
import timber.log.Timber
import java.util.Calendar.getInstance
import com.study.today.feature.main.bookmark.BookmarkRepo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class SearchFragment : Fragment() {

    private lateinit var viewModel: SearchViewModel
    private var _binding: FragmentSearchBinding? =
        null
    private val binding get() = _binding!!

    private val listAdapter = TourListAdapter(clickListener = object : (Int, Tour) -> Unit {
        override fun invoke(position: Int, tour: Tour) {
            Timber.d("Click Data : " + position + tour )

            //val repo = SearchViewFragment.getInstance(requireActivity().application)
            // repo.change(position, tour)
        }
    })
    private val listAdapter = TourListAdapter() { i, tour, b ->
        // TODO: 테스트용으로 작성한 부분이기때문에 나중에 수정해야함
        val repo = BookmarkRepo.getInstance(requireActivity().application)
        repo.change(tour, b)
    }

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
        binding.word.setOnEditorActionListener { text, actionId, keyEvent ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH)
                viewModel.search(text.toString())
            false
        }
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(requireActivity()).get(SearchViewModel::class.java)
        viewModel.searchResult.observe(viewLifecycleOwner, { listAdapter.submitList(it) })
//        viewModel.resultText.observe(viewLifecycleOwner, { binding.result.text = it })
        viewModel.isLoading.observe(viewLifecycleOwner, { binding.progress.isVisible = it })
        viewModel.toastMsgResId.observe(viewLifecycleOwner, {
            Toast.makeText(
                requireContext(),
                getString(it),
                Toast.LENGTH_SHORT
            ).show()
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}