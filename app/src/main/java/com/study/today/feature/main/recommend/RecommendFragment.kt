package com.study.today.feature.main.recommend

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.study.today.databinding.FragmentRecommendBinding
import com.study.today.feature.cos.AreaList
import com.study.today.feature.cos.Drive

class RecommendFragment : Fragment() {

    private var _binding: FragmentRecommendBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
        ): View? {
        _binding = FragmentRecommendBinding.inflate(inflater, container, false)

        binding.drive.setOnClickListener {
            val intent = Intent(context, Drive::class.java)
            startActivity(intent)
//            activity?.finish()
        }
        binding.travel.setOnClickListener {
            val intent = Intent(context, AreaList::class.java)
            startActivity(intent)
//            activity?.finish()
        }
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}