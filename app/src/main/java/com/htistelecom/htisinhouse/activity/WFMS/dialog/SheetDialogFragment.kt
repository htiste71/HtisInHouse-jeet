package com.htistelecom.htisinhouse.activity.WFMS.dialog

import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.support.design.widget.BottomSheetDialogFragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.ViewGroup
import com.htistelecom.htisinhouse.R
import com.htistelecom.htisinhouse.activity.WFMS.adapters.HomeBottomSheetAdapter
import com.htistelecom.htisinhouse.activity.WFMS.models.TaskListModel
import kotlinx.android.synthetic.main.layout_bottom_sheet.*


class SheetDialogFragment : BottomSheetDialogFragment() {
    private var rvSheetDialogFragment: RecyclerView?=null
    private var mContext: Context? =null
    var v: View? = null
    private var taskAL = ArrayList<TaskListModel>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        v = inflater.inflate(R.layout.layout_bottom_sheet, container, false)
        rvSheetDialogFragment=v!!.findViewById(R.id.rvSheetDialogFragment)


        return v
    }

    override fun onDismiss(dialog: DialogInterface?) {
        super.onDismiss(dialog)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        taskAL = arguments!!.getSerializable("data") as ArrayList<TaskListModel>
        if(taskAL==null || taskAL.size==0)
        {
            rvSheetDialogFragment!!.visibility=View.GONE
            tvNoTaskSheetDialogFragment.visibility=View.VISIBLE
        }
        else{
            rvSheetDialogFragment!!.visibility=View.VISIBLE
            tvNoTaskSheetDialogFragment.visibility=GONE
            rvSheetDialogFragment!!.layoutManager= LinearLayoutManager(mContext,LinearLayoutManager.VERTICAL,false)
            val adapter= HomeBottomSheetAdapter(mContext,taskAL,this)
            rvSheetDialogFragment!!.adapter=adapter
        }



    }


    override fun onAttach(context: Context?) {
        super.onAttach(context)
        try {
            mContext=context
        } catch (e: ClassCastException) {
            throw ClassCastException(context!!.toString() + " must implement BottomSheetListener")
        }

    }

    override fun onDestroy() {
        super.onDestroy()

    }
}