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
import android.widget.SeekBar
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


        binding.rangeSeekbar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                binding.textSeekbar.text = "$progress"
            }
            override fun onStartTrackingTouch(seekBar: SeekBar?) {
                // TODO: "start"
            }
            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                // TODO: "stop"
            }
        })

        binding.okBtn.setOnClickListener{
            rangeListener?.invoke(binding.rangeSeekbar.progress)
            dismiss()
        }

        return binding.root

    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
    }
}