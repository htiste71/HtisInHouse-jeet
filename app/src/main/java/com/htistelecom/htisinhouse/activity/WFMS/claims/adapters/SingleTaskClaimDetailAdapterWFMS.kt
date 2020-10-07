package com.htistelecom.htisinhouse.activity.WFMS.claims.adapters

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.htistelecom.htisinhouse.R
import com.htistelecom.htisinhouse.activity.WFMS.claims.models.SingleTaskDetailModel
import kotlinx.android.synthetic.main.row_single_task_claim_detail_adapter_wfms.view.*

class SingleTaskClaimDetailAdapterWFMS(private val mContext: Context, val taskList: ArrayList<SingleTaskDetailModel>) : RecyclerView.Adapter<SingleTaskClaimDetailAdapterWFMS.ViewHolder>() {
    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): ViewHolder {
        // create a new view
        val itemLayoutView = LayoutInflater.from(viewGroup.context).inflate(R.layout.row_single_task_claim_detail_adapter_wfms, viewGroup, false)
        // create ViewHolder
        return ViewHolder(itemLayoutView)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        viewHolder.bind(mContext, taskList.get(position))

    }

    override fun getItemCount(): Int {
        return taskList.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(mContext: Context, model: SingleTaskDetailModel) {
            itemView.tvLocationSingleTaskClaimDetailAdapterWFMS.text = model.fromLocation + " to " + model.toLocation
            itemView.tvDistanceSingleTaskClaimDetailAdapterWFMS.text = model.distanceTravelled + " KM"
            itemView.tvTransportModeSingleTaskClaimDetailAdapterWFMS.text = model.transportMode
            itemView.tvAmountSingleTaskClaimDetailAdapterWFMS.text = model.claimedAmount
            itemView.tvRemarksSingleTaskClaimDetailAdapterWFMS.text = "Remarks - "+model.remarks
            itemView.tvClaimDateSingleTaskClaimDetailAdapterWFMS.text="Claim Date - "+model.createdDate
            if (model.images.equals("")) {
                itemView.rlImagesSingleTaskClaimDetailAdapterWFMS.visibility = View.GONE

            } else {
                var parts = model.images.split(",").toTypedArray()



                itemView.rlImagesSingleTaskClaimDetailAdapterWFMS.visibility = View.VISIBLE
                for (item in parts.indices) {

                    if (item == 0) {
                        Glide.with(mContext).load(model.imagePath + parts.get(item)).into(itemView.iv1RowSingleTaskClaimDetailAdapter);

                    } else if (item == 1) {
                        Glide.with(mContext).load(model.imagePath + parts.get(item)).into(itemView.iv2RowSingleTaskClaimDetailAdapter);

                    } else if (item == 2) {
                        Glide.with(mContext).load(model.imagePath + parts.get(item)).into(itemView.iv3RowSingleTaskClaimDetailAdapter);

                    } else if (item == 3) {
                        Glide.with(mContext).load(model.imagePath + parts.get(item)).into(itemView.iv4RowSingleTaskClaimDetailAdapter);

                    }
                }
            }


        }


    }

}

