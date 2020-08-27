package com.htistelecom.htisinhouse.activity.WFMS.advance_claims.adapters

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.htistelecom.htisinhouse.R
import com.htistelecom.htisinhouse.activity.WFMS.advance_claims.models.AdvanceClaimListModel
import kotlinx.android.synthetic.main.row_advance_status_adapter_wfms.view.*

class AdvanceStatusAdapterWFMS(val ctx: Context, val type: Int, val advanceClaimList: ArrayList<AdvanceClaimListModel>) : RecyclerView.Adapter<AdvanceStatusAdapterWFMS.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, p1: Int): AdvanceStatusAdapterWFMS.ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.row_advance_status_adapter_wfms, parent, false)
        return AdvanceStatusAdapterWFMS.ViewHolder(v)
    }

    override fun getItemCount(): Int {
        return advanceClaimList.size
    }

    override fun onBindViewHolder(holder: AdvanceStatusAdapterWFMS.ViewHolder, pos: Int) {
        holder.bind(advanceClaimList.get(pos), type)
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {


        fun bind(model: AdvanceClaimListModel, type: Int) {

            //Requested
            itemView.tvDateRowAdvanceStatusAdapterWFMS.text = model.requestDate
            itemView.tvAmountRowAdvanceStatusAdapterWFMS.text = model.amount
            itemView.tvRemarksRowAdvanceStatusAdapterWFMS.text = model.remarks
            itemView.tvStatusRowAdvanceStatusAdapterWFMS.text = model.requestStatus


        }


    }

}