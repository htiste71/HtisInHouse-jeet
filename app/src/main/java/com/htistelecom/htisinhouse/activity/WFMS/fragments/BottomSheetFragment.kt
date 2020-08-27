package com.htistelecom.htisinhouse.activity.WFMS.fragments

import android.os.Bundle
import android.support.annotation.Nullable
import android.view.ViewGroup
import android.view.LayoutInflater
import android.support.design.widget.BottomSheetDialogFragment
import android.view.View
import com.htistelecom.htisinhouse.R


class BottomSheetFragment : BottomSheetDialogFragment() {

    @Nullable
    override fun onCreateView(inflater: LayoutInflater,
                              @Nullable container: ViewGroup?,
                              @Nullable savedInstanceState: Bundle?): View? {

// get the views and attach the listener

        return inflater.inflate(R.layout.layout_bottom_sheet, container,false)

    }

    companion object {

        fun newInstance(): BottomSheetFragment {
            return BottomSheetFragment()
        }
    }

    override fun onDestroy() {
        super.onDestroy()

    }
}