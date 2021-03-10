package com.htistelecom.htisinhouse.activity.WFMS.dialog

import android.app.Activity
import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.htistelecom.htisinhouse.R
import com.htistelecom.htisinhouse.activity.WFMS.Utils.ConstantsWFMS
import com.htistelecom.htisinhouse.activity.WFMS.adapters.HomeBottomSheetAdapterNew
import com.htistelecom.htisinhouse.activity.WFMS.marketing.model.TaskModel
import com.htistelecom.htisinhouse.activity.WFMS.models.TaskListModel
import com.htistelecom.htisinhouse.adapter.MarketingTaskAdapter
import com.htistelecom.htisinhouse.config.TinyDB
import kotlinx.android.synthetic.main.fragment_task_wfms.*
import kotlinx.android.synthetic.main.layout_bottom_sheet.*


class SheetDialogFragmentNew : BottomSheetDialogFragment() {
    private var rvSheetDialogFragment: RecyclerView?=null
    private var mContext: Context? =null
    var v: View? = null
    private var taskAL = ArrayList<TaskListModel>()
    private var taskALFilter = ArrayList<TaskListModel>()
    lateinit var tinyDB:TinyDB

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        v = inflater.inflate(R.layout.layout_bottom_sheet, container, false)
        rvSheetDialogFragment=v!!.findViewById(R.id.rvSheetDialogFragment)


        return v
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }


    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
    }
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        tinyDB=TinyDB(activity)
        rvSheetDialogFragment!!.layoutManager= LinearLayoutManager(mContext,LinearLayoutManager.VERTICAL,false)

        if(tinyDB.getString(ConstantsWFMS.TINYDB_USER_TYPE).equals("1")) {
            val taskList = arguments!!.getSerializable("marketing_list") as ArrayList<TaskModel>
            if (taskList == null || taskList.size == 0) {
                rvSheetDialogFragment!!.visibility = View.GONE
                tvNoTaskSheetDialogFragment.visibility = View.VISIBLE
                tvTodayLeadMarketing.visibility=GONE
            } else
            {
                tvTodayLeadMarketing.visibility= VISIBLE

                rvSheetDialogFragment!!.visibility=View.VISIBLE
                tvNoTaskSheetDialogFragment.visibility=GONE
                val adapter = MarketingTaskAdapter(activity!!, taskList)
                rvSheetDialogFragment!!.adapter = adapter
            }

        }
        else
        {
            tvTodayLeadMarketing.visibility=GONE

            taskAL = arguments!!.getSerializable("data") as ArrayList<TaskListModel>
            taskALFilter = arguments!!.getSerializable("data_filter") as ArrayList<TaskListModel>

            if(taskALFilter==null || taskALFilter.size==0)
            {
                rvSheetDialogFragment!!.visibility=View.GONE
                tvNoTaskSheetDialogFragment.visibility=View.VISIBLE
            }
            else{
                rvSheetDialogFragment!!.visibility=View.VISIBLE
                tvNoTaskSheetDialogFragment.visibility=GONE
                rvSheetDialogFragment!!.layoutManager= LinearLayoutManager(mContext,LinearLayoutManager.VERTICAL,false)
                val adapter= HomeBottomSheetAdapterNew(mContext,taskAL,taskALFilter,this)
                rvSheetDialogFragment!!.adapter=adapter
            }

        }



    }

    override fun onAttach(activity: Activity) {
        super.onAttach(activity)
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