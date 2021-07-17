package com.study.today.feature.main.tour_map

import android.content.Context
import android.content.DialogInterface
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.media.session.PlaybackState
import android.os.Bundle
import android.text.Layout
import android.view.ContextMenu
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModel
import com.study.today.R
import com.study.today.databinding.FragmentRangeDialogBinding

class RangeDialogFragment(val rangeListener: (((Int)) -> Unit)? = null) : DialogFragment() {

    private var _binding : FragmentRangeDialogBinding?=null
    private val binding get() = _binding!!
    private var range : Int = 1000 //1km

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setStyle(STYLE_NORMAL, 0)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentRangeDialogBinding.inflate(inflater,container,false)
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))




/*
        binding.one.setOnClickListener{
            range=1000
            bundle.putInt("1km",range)
            fragmentTour.arguments = bundle


        }
        binding.two.setOnClickListener{
            range=2000
            bundle.putInt("2km",range)
        }
        binding.three.setOnClickListener{
            range=3000
            bundle.putInt("3km",range)
        }
*/


        //binding.rangeListview.adapter = createRangeList(requireContext())

        return binding.root

    }

    override fun onDismiss(dialog: DialogInterface) {
        rangeListener?.invoke(binding.seekbar.progress)
        super.onDismiss(dialog)
    }
}