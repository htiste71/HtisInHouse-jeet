package com.htistelecom.htisinhouse.activity.WFMS.marketing.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.htistelecom.htisinhouse.R
import com.htistelecom.htisinhouse.activity.WFMS.marketing.MarketingListModel
import com.htistelecom.htisinhouse.activity.WFMS.marketing.model.TravelModel
import kotlinx.android.synthetic.main.row_travel_adapter.view.*

class TravelAdapter (val activity: Context, val arrayList: ArrayList<TravelModel>) : RecyclerView.Adapter<TravelAdapter.MyHolder>() {

    companion object {
        var data= MarketingListModel()
    }

    override fun onCreateViewHolder(parent: ViewGroup, p1: Int): MyHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.row_travel_adapter, parent, false)
        return MyHolder(v)
    }

    override fun getItemCount(): Int {
        return  arrayList.size
    }

    override fun onBindViewHolder(holdr: MyHolder, pos: Int) {
        holdr.bind(arrayList.get(pos))
    }


    class MyHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(model: TravelModel) {
            itemView.tvDateTravelAdapter.text=" : "+model.TaskDate
            itemView.tvFromLocTravelAdapter.text=" : "+model.FromLoc
            itemView.tvToLocTravelAdapter.text=" : "+model.ToLoc
            itemView.tvRemarksTravelAdapter.text=" : "+model.Remarks





        }
    }
}