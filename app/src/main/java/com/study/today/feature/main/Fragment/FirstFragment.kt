package com.study.today.feature.main.Fragment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.study.today.R
import com.study.today.databinding.FragmentFirstBinding
import com.study.today.databinding.FragmentFiveBinding
import com.study.today.feature.main.cos.AreaList
import com.study.today.feature.main.cos.Drive

class FirstFragment : Fragment() {

    private var _binding: FragmentFirstBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
        ): View? {
        _binding = FragmentFirstBinding.inflate(inflater, container, false)

        binding.drive.setOnClickListener {

        }
        binding.travel.setOnClickListener {

        }
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}