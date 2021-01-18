package com.htistelecom.htisinhouse.adapter

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.htistelecom.htisinhouse.R
import com.htistelecom.htisinhouse.activity.WFMS.marketing.MarketingListModel
import com.htistelecom.htisinhouse.activity.WFMS.marketing.model.OfficeModel
import kotlinx.android.synthetic.main.row_office_adapter.view.*


class OfficeAdapter(val activity: Context, val arrayList: ArrayList<OfficeModel>) : RecyclerView.Adapter<OfficeAdapter.MyHolder>() {

    companion object {
        var data=MarketingListModel()
    }

    override fun onCreateViewHolder(parent: ViewGroup, p1: Int): MyHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.row_office_adapter, parent, false)
        return MyHolder(v)
    }

    override fun getItemCount(): Int {
        return  arrayList.size
    }

    override fun onBindViewHolder(holdr: MyHolder, pos: Int) {
        holdr.bind(arrayList.get(pos))
    }


    class MyHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(model: OfficeModel) {
            itemView.tvDateOfficeAdapter.text=" : "+model.TaskDate
            itemView.tvTimeOfficeAdapter.text=" : "+model.TaskTime





        }
    }
}