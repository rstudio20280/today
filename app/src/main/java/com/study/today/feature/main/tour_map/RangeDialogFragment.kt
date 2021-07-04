package com.study.today.feature.main.tour_map

import android.content.Context
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

class RangeDialogFragment : DialogFragment() {

    private var _binding : FragmentRangeDialogBinding?=null
    private val binding get() = _binding!!
    private var range : Int = 1000 //1km

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentRangeDialogBinding.inflate(inflater,container,false)
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        var fragmentTour = TourMapFragment()
        var bundle = Bundle()


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
}
/*
    fun getInstance():DialogFragment{
        return DialogFragment()
    }
    override fun onClick(position: View?){
        dismiss()
    }

    private class createRangeList(context : Context) : BaseAdapter(){
        private val mContext : Context

        private val numbers = arrayListOf<Int>(
            1,2,3,4,5,6,7,8,9,10
        )

        init{
            mContext = context
        }

        override fun getCount(): Int {
            return numbers.size
        }
        override fun getItemId(position: Int): Long {
            return position.toLong()
        }
        override fun getItem(position: Int): Any {
            val selectItem = numbers.get(position)
            return selectItem
        }

        override fun getView(position: Int, view: View?, viewGroup: ViewGroup?): View {
            val layoutInflater = LayoutInflater.from(mContext)
            val rlist = layoutInflater.inflate(R.layout.range_area_list,viewGroup,false)

            return rlist
        }
    }

*/


