package com.study.today.feature.main.tour_map

import android.content.DialogInterface
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import androidx.fragment.app.DialogFragment
import com.study.today.databinding.FragmentRangeDialogBinding

class RangeDialogFragment(val rangeListener: (((Int)) -> Unit)? = null) : DialogFragment() {
    private var _binding : FragmentRangeDialogBinding?=null
    private val binding get() = _binding!!

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

        var searchRange= arguments?.getInt("range_key")
        var searchRangeNum = searchRange?.div(1000)
        if (searchRangeNum != null) {
            binding.rangeSeekbar.setProgress(searchRangeNum)
            binding.textSeekbar.text="$searchRangeNum"
        }
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

        //확인버튼 눌렀을 때
        binding.okBtn.setOnClickListener{
            rangeListener?.invoke(binding.rangeSeekbar.progress)
            dismiss()
        }
        return binding.root
    }

    //다른 화면 클릭했을 때
    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
    }
}